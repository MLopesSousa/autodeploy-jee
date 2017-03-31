package br.gov.ce.sefaz.deploy;

import java.text.ParseException;
import java.util.Calendar;

public class Teste {
	public static void main(String[] args) throws ParseException {
		String propriedadeTempoInicial = "00:00:00";
        String propriedadeTempoFinal = "12:59:00";

        if(propriedadeTempoInicial != null && propriedadeTempoFinal != null) {
                Calendar tempoCorrente = Calendar.getInstance();
                Calendar tempoInicial = Calendar.getInstance();
                Calendar tempoFinal = Calendar.getInstance();

                tempoCorrente.set(Calendar.HOUR_OF_DAY, tempoCorrente.get(Calendar.HOUR_OF_DAY));
                tempoCorrente.set(Calendar.MINUTE, tempoCorrente.get(Calendar.MINUTE));
                tempoCorrente.set(Calendar.SECOND, tempoCorrente.get(Calendar.SECOND));
                
                tempoInicial.set(Calendar.HOUR_OF_DAY, Integer.parseInt(propriedadeTempoInicial.split(":")[0]));
                tempoInicial.set(Calendar.MINUTE, Integer.parseInt(propriedadeTempoInicial.split(":")[0]));
                tempoInicial.set(Calendar.SECOND, Integer.parseInt(propriedadeTempoInicial.split(":")[0]));
                
                tempoFinal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(propriedadeTempoFinal.split(":")[0]));
                tempoFinal.set(Calendar.MINUTE, Integer.parseInt(propriedadeTempoFinal.split(":")[1]));
                tempoFinal.set(Calendar.SECOND, Integer.parseInt(propriedadeTempoFinal.split(":")[2]));
                
                //tempoCorrente.before(tempoFinal) && tempoCorrente.after(tempoInicial);
       
        }
	}
}
