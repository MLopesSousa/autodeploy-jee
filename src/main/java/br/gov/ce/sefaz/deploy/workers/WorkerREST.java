package br.gov.ce.sefaz.deploy.workers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@RolesAllowed(value = "Admin")
@Path("/workers")
@Consumes("application/json")
@Produces("application/json")
public class WorkerREST {
	
	@EJB(beanName = "WorkerRepositorioSimples") WorkerRepositorioInterface workersRepositorio;
	
	@GET
	public List<String> listarWorkerRegistrados() {
		return workersRepositorio.listarWorkers();
	}
	
	@DELETE
	public void removerWorker(String workerLabel) {
		workersRepositorio.desregistrarWorker(workerLabel);
	}
}
