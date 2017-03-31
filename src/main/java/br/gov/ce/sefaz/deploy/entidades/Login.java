package br.gov.ce.sefaz.deploy.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UsersTable")
public class Login implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "username")
	private String username;
	
	@Id
	@Column(name = "password")
	private String password;

	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
