package br.gov.ce.sefaz.deploy.servicos;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.gov.ce.sefaz.deploy.ejbs.TargetBean;
import br.gov.ce.sefaz.deploy.entidades.Target;

@Consumes("application/json")
@Produces("application/json")
@Path("/targets")
public class TargetREST {

	@EJB
	private TargetBean TargetBean; 
	
	@GET()
	public List<Target> listar() {
		return TargetBean.listar();
	}
	
	@GET
	@Path("{id}")
	public Target selecionar(@PathParam("id") Long id) {
		return TargetBean.selecionar(id);
	}
	
	@POST
	public void criar(Target target) {
		TargetBean.inserir(target);
	}
	
	@PUT
	public void atualizar(Target target) {
		TargetBean.atualizar(target);
	}
	
	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Long id) {
		TargetBean.deletar(id);
	}
}
