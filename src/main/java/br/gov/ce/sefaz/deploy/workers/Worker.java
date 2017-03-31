package br.gov.ce.sefaz.deploy.workers;

import java.io.IOException;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ce.sefaz.deploy.controle.Controle;
import br.gov.ce.sefaz.deploy.ejbs.DeployBean;
import br.gov.ce.sefaz.deploy.entidades.Deploy;
import br.gov.ce.sefaz.deploy.filas.GerenciadorDeFilasInterface;
import br.gov.ce.sefaz.deploy.uteis.ArquivoUtil;
import br.gov.ce.sefaz.deploy.uteis.ExecutarComando;
import br.gov.ce.sefaz.deploy.uteis.HTTPUtil;

@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Stateless
public class Worker {

	@EJB(beanName = "WorkerRepositorioSimples") WorkerRepositorioInterface workersRepositorio;
	@EJB(beanName = "GerenciadorDeFilasRedis") GerenciadorDeFilasInterface gerenciadorDeFilas;
	@EJB Controle controle;
	@Inject ExecutarComando ec;
	@EJB DeployBean deployBean;

	@Asynchronous
	public void build(String fila) throws NumberFormatException, InterruptedException {
		System.out.println("Iniciando worker: " + fila);
		
		while(workersRepositorio.oWorkerEstaRegistrado(fila)) {
			try {
				if (gerenciadorDeFilas.temMensagemNaFila(fila)) {
					runDeploy(gerenciadorDeFilas.removerMensagenNaFila(fila));
				}
			} catch (RuntimeException e) {
				System.out.println("ERRO NO ACESSO AO REDIS !!!");
				System.out.println("DESREGISTRANDO O WORKER: " + fila);
				workersRepositorio.desregistrarWorker(fila);
				return;
			}
			
			Thread.sleep(Long.parseLong(controle.getKeyString("worker.timeout")));
		}
		
		System.out.println("Finalizando worker: " + fila);
	}
	
	private void runDeploy(Deploy deploy) {
		if (deploy != null) {
			System.out.println("INICIALIZANDO O DEPLOY: " + deploy);

			try {
				HTTPUtil.POST(controle.getKeyString("http.api") + "/deploys/" + deploy.getId() + "/EXECUTANDO");
				ec.run("curl -XGET " 
						+ controle.getKeyString("http.api")
						+ "/deploys/" + deploy.getId()
						+ "/comandoScript |bash");
				HTTPUtil.POST(controle.getKeyString("http.api") + "/deploys/" + deploy.getId() + "/FINALIZADO");

			} catch (IOException e) {
				System.out.println("ERRO AO EXECUTAR COMANDOS !!!");
				HTTPUtil.POST(controle.getKeyString("http.api") + "/deploys/" + deploy.getId() + "/ERRO");
			}

			deployBean.notificarFimDoDeploy(deploy);
			ArquivoUtil.renomear(deploy.getAplicacao().getArquivo() + "_DETECTADO", "_DEPLOYED");
			System.out.println("FINALIZANDO O DEPLOY: " + deploy);
		}
	}

}
