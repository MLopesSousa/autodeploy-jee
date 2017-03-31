package br.gov.ce.sefaz.deploy.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class ServidorDeAplicacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private String descricao;
	
	@NotNull
	private String host;
	
	@NotNull
	private int porta;
	
	@NotNull
	private String comando;
	
	@NotNull
	private String fila;
	private boolean estado;
	private String alias;
	private String sshUsuario;
	
	
	public String getAlias() {
		return this.alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public ServidorDeAplicacao() {
		
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public boolean estaHabilitado() {
		return this.estado;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getHost() {
		return host;
	}

	public int getPorta() {
		return porta;
	}

	public String getComando() {
		return comando;
	}

	public String getFila() {
		return fila;
	}
	
	@JsonIgnore
	public String getLabel() {
		return fila;
	}

	public String getSshUsuario() {
		return sshUsuario;
	}
	
	public void setSshUsuario(String sshUsuario) {
		this.sshUsuario = sshUsuario;
	}

}
