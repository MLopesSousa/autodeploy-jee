package br.gov.ce.sefaz.deploy.controle;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@RolesAllowed(value = "Admin")
@Consumes("application/json")
@Produces("application/json")
@Path("/controle")
public class ControleREST {
	@EJB
	Controle controle;
	
	@GET
	@Path("/configuracoes")
	public Map<String, Object> listar() {
		Map<String, Object> retorno = new HashMap<String, Object>();
	
		retorno.putAll(controle.listarConfiguracoes());
		retorno.putAll(controle.listarConfiguracoesString());
		
		return retorno;
	}
	
	@GET
	@Path("/configuracoes/salvar")
	public void salvar() {
		controle.salvarConfiguracoes(); 
	}
	
	@POST
	@Path("/configuracoes/{key}/{valor}")
	public Map<String, Boolean> configurar(@PathParam("key") String key, @PathParam("valor") Boolean valor) {
		controle.setConfiguracao(key, valor);
		controle.salvarConfiguracoes();
		return controle.listarConfiguracoes();
	}
	
	@POST
	@Path("/configuracoes/string/{key}/{valor}")
	public Map<String, Object> configurarString(@PathParam("key") String key, @PathParam("valor") String valor) {
		System.out.println("Requisi��o: configurarString POST");
		
		controle.setConfiguracaoString(key, valor);
		controle.salvarConfiguracoes();
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		retorno.putAll(controle.listarConfiguracoes());
		retorno.putAll(controle.listarConfiguracoesString());
		
		return retorno;
	}
}
