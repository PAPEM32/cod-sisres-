package com.sisres.model;

public class OC {

	private int id;
	private String oc;
	private String nome;
	private String omBp;

	public OC() {
	}

	public OC(int id, String oc, String nome, String omBp) {
		this.id = id;
		this.oc = oc;
		this.nome = nome;
		this.omBp = omBp;
	}

	public OC(String oc, String nome) {
		// TODO Auto-generated constructor stub
		this.oc = oc;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;

	}

	public void setNome(String nome) {

		this.nome = nome;

	}

	public String getOc() {
		return oc;
	}

	public void setOc(String oc) {
		this.oc = oc;
	}

//****************************************************************************	

	public String getOmBp() {
		return omBp;
	}

	public void setOmBp(String omBp) {
		this.omBp = omBp;
	}
}