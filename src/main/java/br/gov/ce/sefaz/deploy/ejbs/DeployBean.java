package br.gov.ce.sefaz.deploy.ejbs;

import java.text.ParseException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ce.sefaz.deploy.controle.Controle;
import br.gov.ce.sefaz.deploy.entidades.Aplicacao;
import br.gov.ce.sefaz.deploy.entidades.Deploy;
import br.gov.ce.sefaz.deploy.entidades.Evento;
import br.gov.ce.sefaz.deploy.entidades.Target;
import br.gov.ce.sefaz.deploy.filas.GerenciadorDeFilasInterface;
import br.gov.ce.sefaz.deploy.uteis.Email;


@Stateless
public class DeployBean {

	@PersistenceContext(unitName = "AD122016") private EntityManager entityManager;
	@EJB(beanName = "GerenciadorDeFilasRedis") GerenciadorDeFilasInterface gerenciadorDeFilas;
	@EJB AplicacaoBean aplicacaoBean;
	@EJB Controle controle;
	@EJB Email email;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void executar(Aplicacao aplicacao) throws ParseException {
		if (aplicacao.arquivoExiste() && aplicacaoBean.verificarHorarioParaAExecucao(aplicacao)) {
			System.out.println("ARQUIVO: " + aplicacao.getDescricao() + " DETECTADO !!!");

			aplicacao
					.copiarArquivoParaStorage(controle.getKeyString("storage"));
			for (Target target : aplicacao.getTarget()) {
				criarDeploy(aplicacao, target);
			}
		}
	}
	
	
	private void criarDeploy(Aplicacao aplicacao, Target target) {
		Deploy deploy = new Deploy(aplicacao, target);
		
		try {
			deploy.setEstado("DEPLOY NA FILA");
			entityManager.persist(deploy);
			gerenciadorDeFilas.adicionarMensagenNaFila(deploy);
			deploy.adicionarEvento(new Evento("DEPLOY ENVIADO PARA FILA !!!"));
		} catch(Exception e) {
			System.out.println("ERRO AO CRIAR O DEPLOY !!! " + e);
		}
	}
	
	public void redeploy(Deploy deploy) {
		Aplicacao aplicacao = deploy.getAplicacao();
		aplicacao.setArquivoTemporario(deploy.getArquivo());
		criarDeploy(aplicacao, deploy.getTarget());
	}

	@SuppressWarnings("unchecked")
	public List<Deploy> listar() {
		Query q = entityManager
				.createQuery("SELECT DISTINCT c FROM Deploy c JOIN FETCH c.eventos");
		return q.getResultList();
	}

	public Deploy selecionar(long id) {
		return entityManager.find(Deploy.class, id);
	}

	public void mudaEstadoParaExecutando(Long id) {
		Deploy deploy = selecionar(id);
		if (deploy != null) {
			deploy.setEstado("EXECUTANDO");

		} else {
			System.out.println("DEPLOY DE ID: " + id + " NAO EXISTE !!!");
		}
	}
	
	public void mudaEstadoParaFinalizado(Long id) {
		Deploy deploy = selecionar(id);
		if (deploy != null) {
			deploy.setEstado("FINALIZADO");

		} else {
			System.out.println("DEPLOY DE ID: " + id + " NAO EXISTE !!!");
		}
	}

	public void mudaEstadoParaFinalizadoComErro(Long id) {
		Deploy deploy = selecionar(id);
		if (deploy != null) {
			deploy.setEstado("ERRO");

		} else {
			System.out.println("DEPLOY DE ID: " + id + " NAO EXISTE !!!");
		}
	}

	public void adicionarEvento(Long id, Evento evento) {
		Deploy deploy = selecionar(id);
		if (deploy != null) {
			deploy.adicionarEvento(new Evento(evento.getDescricao()));
		} else {
			System.out.println("DEPLOY DE ID: " + id + " NAO EXISTE !!!");
		}
	}
	
	private String gerarScriptHelper(Long id, String comando, String label) {
		String apiEventos = controle.getKeyString("http.api") + "/deploys/"
				+ id + "/eventos";

		return comando
				+ " && curl -XPOST -H 'Content-Type: application/json' '"
				+ apiEventos + " ' -d '{\"descricao\": \" " + label
				+ ": SUCESSO\" }' "
				+ " || curl -XPOST -H 'Content-Type: application/json' '"
				+ apiEventos + " ' -d '{\"descricao\": \" " + label
				+ ": FALHA\" }' ";
	}

	public String gerarScript(Deploy deploy) {
		String apiEventos = controle.getKeyString("http.api") + "/deploys/"
				+ deploy.getId() + "/eventos";
		String arquivoRemotoTemporario = "/tmp/" + deploy.getId() + "/"
				+ deploy.getAplicacaoDescricao();

		String ac1 = gerarScriptHelper(deploy.getId(), "mkdir -p /tmp/"
				+ deploy.getId(), "CRIAR DIRETORIO REMOTO");

		String ac2 = gerarScriptHelper(deploy.getId(),
				"wget -q " + controle.getKeyString("http.api") + "/deploys/"
						+ deploy.getId() + "/arquivo -O /tmp/" + deploy.getId()
						+ "/" + deploy.getAplicacaoDescricao(),
				"DOWNLOAD DO ARTEFATO");

		String ac3 = gerarScriptHelper(
				deploy.getId(),
				deploy.getTarget().getServer().getComando()
						.replaceAll("#ARQUIVO", arquivoRemotoTemporario)
						.replace("#TARGET", deploy.getTarget().getDescricao()),
				"DEPLOY DO ARTEFATO");

		String ac4 = gerarScriptHelper(
				deploy.getId(),
				"rm -rf /tmp/" + deploy.getId() + "/"
						+ deploy.getAplicacaoDescricao(), "REMOVER ARTEFATO");

		return ac1 + ";" + ac2 + ";" + ac3 + ";" + ac4
				+ "; curl -XPOST -H 'Content-Type: application/json' '"
				+ apiEventos
				+ " ' -d '{\"descricao\": \"DEPLOY FINALIZADO\" }'";
	}

	public String getComando(Deploy deploy) {
		String comando = "curl -XGET '" + controle.getKeyString("http.api")
				+ "/deploys/" + deploy.getId() + "/script' |bash";
		return gerarSSHComando(deploy, comando);
	}

	private String gerarSSHComando(Deploy deploy, String comando) {
		return "ssh -p " + deploy.getTarget().getServer().getPorta() + 
				" -l " + deploy.getTarget().getServer().getSshUsuario() + " "
				+ deploy.getTarget().getServer().getHost() + " \"" + comando
				+ "\";";
	}

	public void notificarFimDoDeploy(Deploy deploy) {
		String URLDeploy = controle.getKeyString("endpoint") + "/#/deploys/"
				+ deploy.getId();
		for (String destinatario : deploy.getAplicacao().getEmails()) {
			email.send(
					destinatario,
					"[ "
							+ " APLICAÇÃO: "
							+ deploy.getAplicacaoDescricao()
							+ " AMBIENTE: "
							+ deploy.getTarget().getServer().getFila()
									.split("-")[deploy.getTarget().getServer()
									.getFila().split("-").length - 1]
							+ " TARGET: " + deploy.getTarget().getDescricao()
							+ " SERVIDOR DE APLICAÇÂO: "
							+ deploy.getTarget().getServer().getDescricao()
							+ " DATA: " + deploy.getTimestamp() + " ]",
					"Dados do deploy: " + URLDeploy);
		}
	}
}
