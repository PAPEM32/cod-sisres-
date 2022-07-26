package com.sisres.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sisres.utilitaria.Utilitaria;

public class AlterarDividaHistSispagForm extends ActionForm {

	private String id;
	private String codigo;
	private String tipo;
	private String pessoaMatricula;
	private String sistemaOrigem;
	private String estado;

	private String dtInicio;
	private String dtFim;

	private String pessoaNome;
	private String pessoaCpf;
	private String pessoaPosto;
	private String pessoaOC;
	private String pessoaSituacao;

	private String banco;
	private String agencia;
	private String conta;
	private String valor;

	private String origemDoc;
	private String tipoDoc;
	private String numeroDoc;
	private String dataDoc;

	private String tpDocEnvio;
	private String nrDocEnvio;
	private String dtDocEnvio;

	private String dataMotivo;
	private String motivo;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setPessoaMatricula(String pessoaMatricula) {
		this.pessoaMatricula = pessoaMatricula;
	}

	public String getPessoaMatricula() {
		return pessoaMatricula;
	}

	public void setSistemaOrigem(String sistemaOrigem) {
		this.sistemaOrigem = sistemaOrigem;
	}

	public String getSistemaOrigem() {
		return sistemaOrigem;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setDtInicio(String dtInicio) {
		this.dtInicio = dtInicio;
	}

	public String getDtInicio() {
		return dtInicio;
	}

	public void setDtFim(String dtFim) {
		this.dtFim = dtFim;
	}

	public String getDtFim() {
		return dtFim;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public String getPessoaNome() {
		return pessoaNome;
	}

	public void setPessoaCpf(String pessoaCpf) {
		this.pessoaCpf = pessoaCpf;
	}

	public String getPessoaCpf() {
		return pessoaCpf;
	}

	public void setPessoaPosto(String pessoaPosto) {
		this.pessoaPosto = pessoaPosto;
	}

	public String getPessoaPosto() {
		return pessoaPosto;
	}

	public void setPessoaOC(String pessoaOC) {
		this.pessoaOC = pessoaOC;
	}

	public String getPessoaOC() {
		return pessoaOC;
	}

	public void setPessoaSituacao(String pessoaSituacao) {
		this.pessoaSituacao = pessoaSituacao;
	}

	public String getPessoaSituacao() {
		return pessoaSituacao;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getBanco() {
		return banco;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getConta() {
		return conta;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}

	public void setOrigemDoc(String origemDoc) {
		this.origemDoc = origemDoc;
	}

	public String getOrigemDoc() {
		return origemDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setNumeroDoc(String numeroDoc) {
		this.numeroDoc = numeroDoc;
	}

	public String getNumeroDoc() {
		return numeroDoc;
	}

	public void setDataDoc(String dataDoc) {
		this.dataDoc = dataDoc;
	}

	public String getDataDoc() {
		return dataDoc;
	}

	public void setTpDocEnvio(String tpDocEnvio) {
		this.tpDocEnvio = tpDocEnvio;
	}

	public String getTpDocEnvio() {
		return tpDocEnvio;
	}

	public void setNrDocEnvio(String nrDocEnvio) {
		this.nrDocEnvio = nrDocEnvio;
	}

	public String getNrDocEnvio() {
		return nrDocEnvio;
	}

	public void setDtDocEnvio(String dtDocEnvio) {
		this.dtDocEnvio = dtDocEnvio;
	}

	public String getDtDocEnvio() {
		return dtDocEnvio;
	}

	public void setDataMotivo(String dataMotivo) {
		this.dataMotivo = dataMotivo;
	}

	public String getDataMotivo() {
		return dataMotivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getMotivo() {
		return motivo;
	}
	/*
	 * public void confirmaAltDivMesSispag(Divida divida) { // begin-user-code //
	 * TODO Stub de método gerado automaticamente
	 * 
	 * // end-user-code }
	 * 
	 * public void validaMatFin(String matricula) { // begin-user-code // TODO Stub
	 * de método gerado automaticamente
	 * 
	 * // end-user-code }
	 * 
	 */

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		ServletContext context = getServlet().getServletContext();

		if (dtInicio.length() == 0) {
			errors.add("dtInicio", new ActionMessage("error.campodtInicio.required"));
		}
		if (dtFim.length() == 0) {
			errors.add("dtFim", new ActionMessage("error.campodtFim.required"));
		}
		if (origemDoc.length() == 0) {
			errors.add("origemDocAut", new ActionMessage("error.campoOrigemDocAut.required"));
		}

		if (tipoDoc.length() == 0) {
			errors.add("tipoDocAut", new ActionMessage("error.campoTipoDocAut.required"));
		}

		if (numeroDoc.length() == 0) {
			errors.add("numeroDocAut", new ActionMessage("error.campoNumeroDocAut.required"));
		} else {
			try {
				int numeroDocAut = Integer.parseInt(numeroDoc.trim());
			} catch (NumberFormatException e) {
				errors.add("numeroDocAut", new ActionMessage("error.campoNumeroDocAut.range"));
			}
		}
		if (dataDoc.length() == 0) {
			errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.required"));

//				context.log("Data Doc Aut não informada");	
		} else {
			try {
				dataDoc = Utilitaria.formatarData(dataDoc, "dd/MM/yyyy");
				// System.out.println(dataDoc);
			} catch (Exception e) {
				errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.range"));
			}

		}
		if (tpDocEnvio.length() == 0) {
			errors.add("tpDocEnvio", new ActionMessage("error.campotpDocEnvio.required"));
		}

		if (nrDocEnvio.length() == 0) {
			errors.add("nrDocEnvio", new ActionMessage("error.camponrDocEnvio.required"));
		}

		if (dtDocEnvio.length() == 0) {
			errors.add("dtDocEnvio", new ActionMessage("error.campodtDocEnvio.required"));
		} else {
			try {
				dtDocEnvio = Utilitaria.formatarData(dtDocEnvio, "dd/MM/yyyy");

			} catch (Exception e) {
				errors.add("dtDocEnvio", new ActionMessage("error.campodtDocEnvio.range"));
			}
		}
		if (motivo.length() == 0) {
			errors.add("motivo", new ActionMessage("error.campoMotivo.required"));
		}

		if (dataMotivo.length() == 0) {
			errors.add("dataMotivo", new ActionMessage("error.campoDataMotivo.required"));
		} else {
			try {
				dataMotivo = Utilitaria.formatarData(dataMotivo, "dd/MM/yyyy");

			} catch (Exception e) {
				errors.add("dataMotivo", new ActionMessage("error.campoDataMotivo.range"));
			}
		}

		return errors;

	}

}
