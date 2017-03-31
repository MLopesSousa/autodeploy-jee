package br.gov.ce.sefaz.deploy.servicos;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.gov.ce.sefaz.deploy.ejbs.LoginBean;
import br.gov.ce.sefaz.deploy.entidades.Login;

@Consumes("application/json")
@Produces("application/json")
@Path("/login")
public class LoginREST {
	
	@EJB
	private LoginBean loginBean;
	
	@POST
	public Response logar(Login login) {
		if(loginBean.logar(login)) {
			System.out.println("Logado !");
			
			return Response.ok(loginBean.roles(login)).build();
		} else {
			return Response.status(403).build();
		}
	}
}
