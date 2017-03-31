package br.gov.ce.sefaz.deploy.uteis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExecutarComando {
	
    public BufferedReader[] run(String comando) throws IOException {
            BufferedReader retorno[] = new BufferedReader[2];

            System.out.println("Executando o comando: " + comando);
            Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", comando });

            retorno[0] = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));

            retorno[1] = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));

            return retorno;
    }
    
    public String bufferedReaderToString(BufferedReader buffer) throws IOException {
        String retorno = "";
        String s;

        while ((s = buffer.readLine()) != null) {
            retorno += s;
        }

        return retorno;
    }
}