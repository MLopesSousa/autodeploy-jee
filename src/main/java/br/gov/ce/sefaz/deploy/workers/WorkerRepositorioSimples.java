package br.gov.ce.sefaz.deploy.workers;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;

@Singleton
public class WorkerRepositorioSimples implements WorkerRepositorioInterface {
	private List<String> workers = new ArrayList<>();

	@Override
	public void registrarWorker(String workerLabel) {
		if(!oWorkerEstaRegistrado(workerLabel)) {
			System.out.println("Registrando worker: " + workerLabel);
			workers.add(workerLabel);
		}
	}

	@Override
	public void desregistrarWorker(String workerLabel) {
		if(oWorkerEstaRegistrado(workerLabel)) {
			System.out.println("Desregistrando worker: " + workerLabel);
			workers.remove(workerLabel);
		}
		
	}

	@Override
	public boolean oWorkerEstaRegistrado(String workerLabel) {
		return workers.contains(workerLabel);
	}

	@Override
	public List<String> listarWorkers() {
		return workers;
	}


}
