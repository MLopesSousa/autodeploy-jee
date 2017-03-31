package br.gov.ce.sefaz.deploy.filas;

import java.util.Map;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import br.gov.ce.sefaz.deploy.filas.GerenciadorDeFilasInterface;

@RolesAllowed(value = "Admin")
@Consumes("application/json")
@Produces("application/json")
@Path("/filas")
public class FilasREST {

	@EJB(beanName = "GerenciadorDeFilasRedis")
	GerenciadorDeFilasInterface gerenciadorDeFilas;
	
	@GET()
	public Set<String> listar() {
		return gerenciadorDeFilas.listarFilas();
	}
	
	@GET()
	@Path("/all")
	public Map<String, Long> listarComMensagens() {
		return gerenciadorDeFilas.listarComMensagens();
	}
	
	@GET
	@Path("{id}")
	public long selecionar(@PathParam("id") String id) {
		return gerenciadorDeFilas.quantasMensagensTemNaFila(id);
	}
}
