package br.gov.ce.sefaz.deploy.servicos;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import br.gov.ce.sefaz.deploy.ejbs.AplicacaoBean;
import br.gov.ce.sefaz.deploy.ejbs.DeployBean;
import br.gov.ce.sefaz.deploy.entidades.Deploy;
import br.gov.ce.sefaz.deploy.entidades.Evento;

@Consumes("application/json")
@Produces("application/json")
@Path("/deploys")
public class DeployREST {

	@EJB
	private DeployBean Deploybean; 
	
	@EJB
	private AplicacaoBean AplicacaoBean; 
	
	@GET()
	public List<Deploy> listar() {
		return Deploybean.listar();
	}
	
	@POST
	@Path("{deployId}/redeploy")
	public void redeploy(@PathParam("deployId") Long deployId) {
		Deploy deploy = Deploybean.selecionar(deployId);
		
		if(deploy != null) {
			Deploybean.redeploy(deploy);
		}
	}
	
	@POST
	@Path("{id}/eventos/")
	public void adicionarEvento(@PathParam("id") Long id, Evento evento) {
		System.out.println("criando evento: " + evento.getDescricao());
		Deploybean.adicionarEvento(id, evento);
	}
	
	@POST
	@Path("{id}/FINALIZADO")
	public void mudaEstadoParaFinalizado(@PathParam("id") Long id) {
		Deploybean.mudaEstadoParaFinalizado(id);
	}
	
	@POST
	@Path("{id}/EXECUTANDO")
	public void mudaEstadoParaExecutando(@PathParam("id") Long id) {
		Deploybean.mudaEstadoParaExecutando(id);
	}
	
	@POST
	@Path("{id}/ERRO")
	public void mudaEstadoParaFinalizadoComErro(@PathParam("id") Long id) {
		Deploybean.mudaEstadoParaFinalizadoComErro(id);
	}
	
	@GET
	@Path("{id}/arquivo")
	public Response arquivo(@PathParam("id") Long id) {
		Deploy deploy = Deploybean.selecionar(id);
		
		if(deploy != null) {
			File arquivo = new File(deploy.getArquivo());
			
			ResponseBuilder response = Response.ok((Object) arquivo);
			response.header("Content-Disposition", "attachment; filename=" + deploy.getAplicacaoDescricao());
			return response.build();
		} else {
			return Response.serverError().build();
		}	
	}
	
	@Produces("text/html")
	@GET
	@Path("{id}/comandoScript/")
	public String comandoScript(@PathParam("id") Long id) {
		Deploy deploy = Deploybean.selecionar(id);
		return Deploybean.getComando(deploy);
	}
	
	
	@Produces("text/html")
	@GET
	@Path("{id}/script/")
	public String gerarScript(@PathParam("id") Long id) {
		Deploy deploy = Deploybean.selecionar(id);
		return Deploybean.gerarScript(deploy);
	}
}
