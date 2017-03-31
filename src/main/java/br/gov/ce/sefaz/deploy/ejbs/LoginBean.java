package br.gov.ce.sefaz.deploy.ejbs;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ce.sefaz.deploy.entidades.Login;

@Stateless
public class LoginBean {
	
	@PersistenceContext(unitName = "AD122016LOGIN")
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public boolean logar(Login login) {
		
		Query q = entityManager.createQuery("select c from Login c where c.username = '" 
				+ login.getUsername() 
				+ "' AND c.password = '" 
				+ login.getPassword() 
				+ "'");

		List<Login> ll = q.getResultList();
		for(Login l : ll) {
			if(l != null) {
				return true;
			}
		}
		
		return false;
	}
	
public List<String> roles(Login login) {
	List<String> retorno = new ArrayList<String>();
	
		if(logar(login)) {
			Query q = entityManager.createNativeQuery("select role from  RoleTable where username =:username");
			q.setParameter("username", login.getUsername());
			for(Object object: q.getResultList()) {
				retorno.add((String) object);
			}
			
			return retorno;
		}
		
		return retorno;
	}

}
