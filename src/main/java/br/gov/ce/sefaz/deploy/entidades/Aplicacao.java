package br.gov.ce.sefaz.deploy.entidades;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import br.gov.ce.sefaz.deploy.uteis.ArquivoUtil;

@Entity
public class Aplicacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private String descricao;
	
	@NotNull
	private String arquivo;
	
	@JsonIgnore
	@Transient
	private String arquivoTemporario;
	
	@NotNull
	@Basic
	private ArrayList<String> emails = new ArrayList<String>();
	private boolean estado;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Target> targets = new HashSet<Target>();
	
	@JsonManagedReference
	@Column(nullable = true)
	@OneToMany(mappedBy = "aplicacao" ,fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE })
	private List<Deploy> deploys = new ArrayList<Deploy>();
	
	@NotNull
	private String ambiente;
	
	public String getAmbiente() {
		return this.ambiente;
	}
	
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	
	public String getArquivoTemporario() {
		return this.arquivoTemporario;
	}
	
	public void setArquivoTemporario(String arquivo) {
		this.arquivoTemporario = arquivo;
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public ArrayList<String> getEmails() {
		return this.emails;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getDescricao() {
		return descricao;
	}

	public String getArquivo() {
		return arquivo;
	}

	public Set<Target> getTarget() {
		return this.targets;
	}
	
	public List<Deploy> getDeploys() {
		return this.deploys;
	}

	public void addDeploy(Deploy deploy) {
		this.deploys.add(deploy);
	}
	
	public boolean arquivoExiste() {
		File in = new File(this.arquivo);
		return in.exists();
	}
	
	public void copiarArquivoParaStorage(String destino) {
		this.arquivoTemporario = ArquivoUtil.copiaParaStorageEPegaNovoCaminho(this.arquivo, destino);
	}
}
