package br.gov.ce.sefaz.deploy.rotinas;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import br.gov.ce.sefaz.deploy.controle.Controle;
import br.gov.ce.sefaz.deploy.ejbs.ServidorDeAplicacaoBean;
import br.gov.ce.sefaz.deploy.entidades.ServidorDeAplicacao;
import br.gov.ce.sefaz.deploy.workers.Worker;
import br.gov.ce.sefaz.deploy.workers.WorkerRepositorioInterface;

@Singleton
public class ScanerConsumidores {

	@EJB Worker worker;
	@EJB(beanName = "WorkerRepositorioSimples") WorkerRepositorioInterface workersRepositorio;
	@EJB ServidorDeAplicacaoBean servidorDeAplicacao;
	@EJB Controle controle;

	@Schedule(second = "*/25", minute = "*", hour = "*")
	public void executar() throws NumberFormatException, InterruptedException {
		if (!controle.getKey("ScanerConsumidores"))
			return;

		for (ServidorDeAplicacao servidor : servidorDeAplicacao.listarHabilitados()) {
			if (!workersRepositorio.oWorkerEstaRegistrado(servidor.getLabel())) {
				workersRepositorio.registrarWorker(servidor.getLabel());
				worker.build(servidor.getLabel());
				
			}
		}
	}
}
