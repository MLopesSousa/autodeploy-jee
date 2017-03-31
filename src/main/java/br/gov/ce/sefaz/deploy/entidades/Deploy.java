package br.gov.ce.sefaz.deploy.entidades;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Deploy implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@Transient
	private String aplicacaoDescricao;
	private Timestamp timestamp;
	private String arquivo;
	private String estado;
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="aplicacao_fk")
	private Aplicacao aplicacao;
	
	@ManyToOne
	private Target target;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Evento> eventos = new HashSet<Evento>();
	
	public Deploy() {
		this.timestamp = new Timestamp(System.currentTimeMillis());
	}

	public Deploy(Aplicacao aplicacao, Target target) {
		this();
		this.aplicacao = aplicacao;
		this.arquivo = aplicacao.getArquivoTemporario();
		this.target = target;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getArquivo() {
		return this.arquivo;
	}
	
	public Target getTarget() {
		return this.target;
	} 
	
	public Timestamp getTimestamp() {
		return this.timestamp;
	}

	public String getEstado() {
		return this.estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void adicionarEvento(Evento evento) {
		this.eventos.add(evento);	
	}
	
	public Set<Evento> getEventos() {
		return this.eventos;
	}
	
	public String toString() {
		return "[ id: " + this.id +
				", aplicacao: " + this.aplicacao.getDescricao() + 
				", timestamp: " + this.timestamp + 
				", arquivo: " + this.arquivo +  
				", eventos: " + this.eventos + " ]";
	}
	
	public String getAplicacaoDescricao() {
		return this.aplicacao.getDescricao();
	}

	@JsonIgnore
	public String getFila() {
		return this.target.getFila();
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Aplicacao getAplicacao() {
		return this.aplicacao;
	}
}
