package br.gov.ce.sefaz.deploy.ejbs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ce.sefaz.deploy.entidades.Target;

@Stateless
public class TargetBean {
	
	@PersistenceContext(unitName = "AD122016")
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public List<Target> listar() {
		Query q = entityManager.createQuery("select c from Target c");
		return q.getResultList();
	}
	
	public Target selecionar(long id) {
		return entityManager.find(Target.class, id);
	}
	
	public void inserir(Target target) {
		entityManager.persist(target);
	}

	public void atualizar(Target target) {
		entityManager.merge(target);
	}
	
	public void deletar(Long id) {
		Target target = selecionar(id);
		if(target != null) {
			entityManager.remove(target);
		}
		
	}
	
}
