package br.gov.ce.sefaz.deploy.entidades;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Evento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private long id;
	private String descricao;
	private Timestamp timestamp;
	
	public Evento() {
		
	}
	
	public Evento(String descricao) {
		this.descricao = descricao;
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public Timestamp getTimestamp() {
		return this.timestamp;
	}
	
	public String toString() {
		return "[ Descricao: " + this.descricao +
				", Timestamp: " + this.timestamp +
				" ]";
	}
}
