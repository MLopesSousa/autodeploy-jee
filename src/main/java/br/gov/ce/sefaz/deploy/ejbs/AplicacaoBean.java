package br.gov.ce.sefaz.deploy.ejbs;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ce.sefaz.deploy.controle.Controle;
import br.gov.ce.sefaz.deploy.entidades.Aplicacao;

@Stateless
public class AplicacaoBean {
	
	@PersistenceContext(unitName = "AD122016")
	private EntityManager entityManager;
	
	@EJB Controle controle;

	@SuppressWarnings("unchecked")
	public List<Aplicacao> listar() {
		Query q = entityManager.createQuery("select c from Aplicacao c ");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Aplicacao> listarHabilitados() {
		Query q = entityManager.createQuery("select c from Aplicacao c where c.estado = true");
		return q.getResultList();
	}

	
	public Aplicacao selecionar(long id) {
		return entityManager.find(Aplicacao.class, id);
	}
	
	public void inserir(Aplicacao aplicacao) {
		entityManager.persist(aplicacao);
	}

	public void atualizar(Aplicacao aplicacao) {
		entityManager.merge(aplicacao);
	}
	
	public void deletar(Long id) {
		Aplicacao aplicacao = selecionar(id);
		if(aplicacao != null) {
			entityManager.remove(aplicacao);
		}
		
	}

	public Aplicacao selecionarPorDescricao(String descricao) {
		Aplicacao aplicacao = null;
		
		Query q = entityManager.createQuery("select c from Aplicacao c where c.descricao = '" + descricao + "' ");
		for(Object app : q.getResultList()) {
			aplicacao = (Aplicacao) app;
		}
		
		return aplicacao;
	}

	public boolean verificarHorarioParaAExecucao(Aplicacao aplicacao) throws ParseException {
		String propriedadeTempoInicial = controle.getKeyString(aplicacao.getAmbiente() + ".horario.inicial");
		String propriedadeTempoFinal = controle.getKeyString(aplicacao.getAmbiente() + ".horario.final");
		
        if(propriedadeTempoInicial != null && propriedadeTempoFinal != null) {
                Calendar tempoCorrente = Calendar.getInstance();
                Calendar tempoInicial = Calendar.getInstance();
                Calendar tempoFinal = Calendar.getInstance();

                tempoCorrente.set(Calendar.HOUR_OF_DAY, tempoCorrente.get(Calendar.HOUR_OF_DAY));
                tempoCorrente.set(Calendar.MINUTE, tempoCorrente.get(Calendar.MINUTE));
                tempoCorrente.set(Calendar.SECOND, tempoCorrente.get(Calendar.SECOND));
                
                tempoInicial.set(Calendar.HOUR_OF_DAY, Integer.parseInt(propriedadeTempoInicial.split(":")[0]));
                tempoInicial.set(Calendar.MINUTE, Integer.parseInt(propriedadeTempoInicial.split(":")[0]));
                tempoInicial.set(Calendar.SECOND, Integer.parseInt(propriedadeTempoInicial.split(":")[0]));
                
                tempoFinal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(propriedadeTempoFinal.split(":")[0]));
                tempoFinal.set(Calendar.MINUTE, Integer.parseInt(propriedadeTempoFinal.split(":")[1]));
                tempoFinal.set(Calendar.SECOND, Integer.parseInt(propriedadeTempoFinal.split(":")[2]));
                
                return tempoCorrente.before(tempoFinal) && tempoCorrente.after(tempoInicial);
       
        }

		return false;
	}
	
}

