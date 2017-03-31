package br.gov.ce.sefaz.deploy.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Target implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;

	@NotNull
	private String descricao;
	@ManyToOne
	private ServidorDeAplicacao server;
	@Transient
	private String fila;

	public Target() {

	}

	public String getFila() {
		if (this.server != null) {
			return this.server.getFila();
		} else {
			return "";
		}
	}

	public long getId() {
		return this.id;
	}

	public String getDescricao() {
		return descricao;
	}

	public ServidorDeAplicacao getServer() {
		return server;
	}

	public String getAlias() {
		if (this.server != null) {
			return this.server.getAlias() + " AND  @fields.target:\""
					+ this.descricao + "\"";
		} else {
			return "";
		}
	}
	
	public void setAlias(String alias) {
		
	}
}
