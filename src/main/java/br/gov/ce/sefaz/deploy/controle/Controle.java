package br.gov.ce.sefaz.deploy.controle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;

@Singleton
public class Controle {
	private Map<String, Boolean> mapa = new HashMap<String, Boolean>();
	private Map<String, String> StringMapa = new HashMap<String, String>();
	
	@PostConstruct
    void atStartup() throws FileNotFoundException, IOException {
		lerConfiguracoes();
		
		unsetConfiguracaoString("endpoint");
		unsetConfiguracaoString("http.api");
		setConfiguracaoString("endpoint", "http://" + System.getProperty("jboss.host.name") + ":8080/deploy");
		setConfiguracaoString("http.api", "http://" + System.getProperty("jboss.host.name") + ":8080/deploy/rs");
		
		salvarConfiguracoes();
	}
	
	private void lerConfiguracoes() throws FileNotFoundException, IOException {
		System.out.println("Lendo configuracoes...");
		
		try (Reader reader = new FileReader("configuracoesString.csv")) {
			BufferedReader br = new BufferedReader(reader);
			String  line;
			
			while ((line = br.readLine()) != null) {
				String[] dados = line.split(",");
				setConfiguracaoString(dados[0], dados[1]);		
			}
			
			br.close();
			reader.close();
		}
		
		try (Reader reader = new FileReader("configuracoesBoolean.csv")) {
			BufferedReader br = new BufferedReader(reader);
			String  line;
			
			while ((line = br.readLine()) != null) {
				String[] dados = line.split(",");
				setConfiguracao(dados[0], Boolean.parseBoolean(dados[1]));		
			}
			
			br.close();
			reader.close();
		}
	}
	
	public void salvarConfiguracoes() {
		System.out.println("Salvando configuracoes...");
		
		String eol = System.getProperty("line.separator");

		try (Writer writer = new FileWriter("configuracoesBoolean.csv")) {
		  for (Map.Entry<String, Boolean> entry : mapa.entrySet()) {
		    writer.append(entry.getKey())
		          .append(',')
		          .append(entry.getValue().toString())
		          .append(eol);
		  }
		  
		  writer.close();
		  
		} catch (IOException ex) {
		  ex.printStackTrace(System.err);
		}
		
		try (Writer writer = new FileWriter("configuracoesString.csv")) {
			  for (Map.Entry<String, String> entry : StringMapa.entrySet()) {
				    writer.append(entry.getKey())
				          .append(',')
				          .append(entry.getValue())
				          .append(eol);
				  }
			  
			  writer.close();
			  
			} catch (IOException ex) {
			  ex.printStackTrace(System.err);
			}
	}
	
	public Map<String, Boolean> listarConfiguracoes() {
		return this.mapa;
	}
	
	public Map<String, String> listarConfiguracoesString() {
		return this.StringMapa;
	}

	public Boolean getKey(String key) {
		if(this.mapa.containsKey(key)) {
			return this.mapa.get(key);
		} else {
			setConfiguracao(key, false);
			return false;
		}
	} 
	
	public String getKeyString(String key) {
		if(this.StringMapa.containsKey(key)) {
			return this.StringMapa.get(key);
		} else {
			setConfiguracaoString(key, "");
			return "";
		}
	} 
	
	public void unsetConfiguracao(String key) {
		if(this.mapa.containsKey(key)) {
			this.mapa.remove(key);
		}
	}
	
	public void unsetConfiguracaoString(String key) {
		if(this.StringMapa.containsKey(key)) {
			this.StringMapa.remove(key);
		}
	}
	
	public void setConfiguracao(String key, Boolean valor) {
		if(!this.mapa.containsKey(key)) {
			this.mapa.put(key, valor);
		} else {
			this.mapa.remove(key);
			this.mapa.put(key, valor);
		}
	}
	
	public void setConfiguracaoString(String key, String valor) {
		if(!this.StringMapa.containsKey(key)) {
			this.StringMapa.put(key, valor);
		} else {
			this.StringMapa.remove(key);
			this.StringMapa.put(key, valor);
		}
	}
}
