package br.gov.ce.sefaz.deploy.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ce.sefaz.deploy.entidades.Evento;

@Stateless
public class EventoBean {
	
	@PersistenceContext(unitName = "AD122016")
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Evento> listar() {
		Query q = entityManager.createQuery("select c from Evento c");
		return q.getResultList();
	}
	
	public Evento selecionar(long id) {
		return entityManager.find(Evento.class, id);
	}
}
