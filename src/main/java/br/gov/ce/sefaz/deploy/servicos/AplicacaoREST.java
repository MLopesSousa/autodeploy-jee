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

import br.gov.ce.sefaz.deploy.ejbs.AplicacaoBean;
import br.gov.ce.sefaz.deploy.entidades.Aplicacao;

@Consumes("application/json")
@Produces("application/json")
@Path("/aplicacoes")
public class AplicacaoREST {

	@EJB
	private AplicacaoBean AplicacaoBean; 
	
	@GET
	public List<Aplicacao> listar() {
		return AplicacaoBean.listar();
	} 
	
	@POST
	public Aplicacao criar(Aplicacao aplicacao) {
		AplicacaoBean.inserir(aplicacao);
		return aplicacao;
	}
	
	@PUT
	public List<Aplicacao> atualizar(Aplicacao aplicacao) {
		AplicacaoBean.atualizar(aplicacao);
		return AplicacaoBean.listar();
	}
	
	@DELETE
	@Path("{id}")
	public List<Aplicacao> remove(@PathParam("id") Long id) {
		AplicacaoBean.deletar(id);
		return AplicacaoBean.listar();
	}
}
