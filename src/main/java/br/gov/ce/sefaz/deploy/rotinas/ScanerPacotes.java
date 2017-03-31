package br.gov.ce.sefaz.deploy.rotinas;

import java.text.ParseException;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import br.gov.ce.sefaz.deploy.controle.Controle;
import br.gov.ce.sefaz.deploy.ejbs.AplicacaoBean;
import br.gov.ce.sefaz.deploy.ejbs.DeployBean;
import br.gov.ce.sefaz.deploy.entidades.Aplicacao;

@Singleton
public class ScanerPacotes {
	@EJB AplicacaoBean aplicacaoBean;
	@EJB DeployBean deployManager;
	@EJB Controle controle;
	
	@Schedule(second="1", minute="*/5", hour="*") 
	public void executar() throws ParseException {
		if(!controle.getKey("ScanerPacotes"))
			return;
		
		for(Aplicacao aplicacao :aplicacaoBean.listarHabilitados()) {
			deployManager.executar(aplicacao);
		}
	}
}
