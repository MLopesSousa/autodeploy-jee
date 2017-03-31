package br.gov.ce.sefaz.deploy.uteis;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPUtil {
	public static void POST(String str) {
		try {
			URL url = new URL(str);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.getResponseCode();
			
		} catch (IOException e) {
			System.out.println("Erro ao enviar requisicao ao endpoint: " + str);
		}
	}
}
