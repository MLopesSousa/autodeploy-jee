package br.gov.ce.sefaz.deploy.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ce.sefaz.deploy.entidades.ServidorDeAplicacao;

@Stateless
public class ServidorDeAplicacaoBean {
	
	@PersistenceContext(unitName = "AD122016")
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<ServidorDeAplicacao> listar() {
		Query q = entityManager.createQuery("select c from ServidorDeAplicacao c");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ServidorDeAplicacao> listarHabilitados() {
		Query q = entityManager.createQuery("select c from ServidorDeAplicacao c where c.estado = true");
		return q.getResultList();
	}
	
	public ServidorDeAplicacao selecionar(long id) {
		return entityManager.find(ServidorDeAplicacao.class, id);
	}
	
	public void inserir(ServidorDeAplicacao servidorDeAplicacao) {
		entityManager.persist(servidorDeAplicacao);
	}

	public void atualizar(ServidorDeAplicacao servidorDeAplicacao) {
		entityManager.merge(servidorDeAplicacao);
	}
	
	public void deletar(Long id) {
		ServidorDeAplicacao servidorDeAplicacao = selecionar(id);
		if(servidorDeAplicacao != null) {
			entityManager.remove(servidorDeAplicacao);
		}	
	}
	
	public boolean estaHabilitado(ServidorDeAplicacao servidorDeAplicacao) {
		ServidorDeAplicacao servidor = selecionar(servidorDeAplicacao.getId());
		return servidor.estaHabilitado();
	}
}
