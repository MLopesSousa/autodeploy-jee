package br.gov.ce.sefaz.deploy.workers;

import java.util.List;

public interface WorkerRepositorioInterface {	
	public void registrarWorker(String workerLabel);
	public void desregistrarWorker(String workerLabel);
	public boolean oWorkerEstaRegistrado(String workerLabel);
	
	public List<String> listarWorkers();
}
