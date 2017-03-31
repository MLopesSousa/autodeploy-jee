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

import br.gov.ce.sefaz.deploy.ejbs.ServidorDeAplicacaoBean;
import br.gov.ce.sefaz.deploy.entidades.ServidorDeAplicacao;

@Consumes("application/json")
@Produces("application/json")
@Path("/servidorDeAplicacao")
public class ServidorDeAplicacaoREST {
	
	@EJB
	private ServidorDeAplicacaoBean ServidorDeAplicacaoBean; 
	
	@GET()
	public List<ServidorDeAplicacao> listar() {
		return ServidorDeAplicacaoBean.listar();
	}
	
	@GET
	@Path("{id}")
	public ServidorDeAplicacao selecionar(@PathParam("id") Long id) {
		return ServidorDeAplicacaoBean.selecionar(id);
	}
	
	@POST
	public void criar(ServidorDeAplicacao servidorDeAplicacao) {
		ServidorDeAplicacaoBean.inserir(servidorDeAplicacao);
	}
	
	@PUT
	public void atualizar(ServidorDeAplicacao servidorDeAplicacao) {
		ServidorDeAplicacaoBean.atualizar(servidorDeAplicacao);
	}
	
	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Long id) {
		ServidorDeAplicacaoBean.deletar(id);
	}
}
