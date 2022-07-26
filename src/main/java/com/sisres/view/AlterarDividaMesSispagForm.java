/**
 * 
 */
package com.sisres.view;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sisres.utilitaria.Utilitaria;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author user
 * @generated "UML para Java
 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class AlterarDividaMesSispagForm extends ActionForm {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param divida
	 * @generated "UML para Java
	 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */

	private String pessoaMatricula = "";
	private String pessoaNome = "";
	private String OC;
	private String banco;
	private String agencia;
	private String conta;
	/*
	 * private String pessoaCpf; private String pessoaSituacao; private String
	 * valor;
	 */

	private String origemDoc;
	private String tipoDoc;
	private String numeroDoc;
	private String dataDoc;
	private String dataMotivo;
	private String motivo;

	private String tipoDocEnvio;
	private String numDocEnvio;
	private String dataDocEnvio;

	private String dtInicio;
	private String dtFim;

	private String observacao;
	private String tipoDivida;

	public void setPessoaMatricula(String pessoaMatricula) {
		this.pessoaMatricula = pessoaMatricula;
	}

	public String getPessoaMatricula() {
		return pessoaMatricula;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public String getPessoaNome() {
		return pessoaNome;
	}

	public void setOC(String oC) {
		OC = oC;
	}

	public String getOC() {
		return OC;
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

	public void setTipoDocEnvio(String tipoDocEnvio) {
		this.tipoDocEnvio = tipoDocEnvio;
	}

	public String getTipoDocEnvio() {
		return tipoDocEnvio;
	}

	public void setNumDocEnvio(String numDocEnvio) {
		this.numDocEnvio = numDocEnvio;
	}

	public String getNumDocEnvio() {
		return numDocEnvio;
	}

	public void setDataDocEnvio(String dataDocEnvio) {
		this.dataDocEnvio = dataDocEnvio;
	}

	public String getDataDocEnvio() {
		return dataDocEnvio;
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

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setTipoDivida(String tipoDivida) {
		this.tipoDivida = tipoDivida;
	}

	public String getTipoDivida() {
		return tipoDivida;
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
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();

		ServletContext context = getServlet().getServletContext();

		if (origemDoc.length() == 0) {
			errors.add("origemDocAut", new ActionMessage("error.campoOrigemDocAut.required"));
		}

		if (tipoDoc.length() == 0) {
			errors.add("tipoDocAut", new ActionMessage("error.campoTipoDocAut.required"));
		}

		if (numeroDoc.length() == 0) {
			errors.add("numeroDocAut", new ActionMessage("error.campoNumeroDocAut.required"));
		}

		if (dataDoc.length() == 0) {
			errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.required"));

//			context.log("Data Doc Aut não informada");
		} else {
			try {
				dataDoc = Utilitaria.formatarData(dataDoc, "dd/MM/yyyy");
				// System.out.println(dataDoc);
			} catch (Exception e) {
				errors.add("dataDocAut", new ActionMessage("error.campoDataDocAut.range"));
			}

		}

		// System.out.println("Verifica tipo divida no form");

		if (tipoDivida.equals("HISTÓRICA ")) {

			if (tipoDocEnvio.length() == 0) {
				errors.add("tipoDocEnvio", new ActionMessage("error.campotpDocEnvio.required"));
			}

			if (numDocEnvio.length() == 0) {
				errors.add("numDocEnvio", new ActionMessage("error.camponrDocEnvio.required"));
			}

			if (dataDocEnvio.length() == 0) {
				errors.add("dtDocEnvio", new ActionMessage("error.campodtDocEnvio.required"));
			} else {
				try {
					dataDocEnvio = Utilitaria.formatarData(dataDocEnvio, "dd/MM/yyyy");

				} catch (Exception e) {
					errors.add("dtDocEnvio", new ActionMessage("error.campodtDocEnvio.range"));
				}
			}

			/*
			 * if (dtInicio.length() == 0) { errors.add("dtInicio", new ActionMessage(
			 * "error.campodtInicio.required")); } if (dtFim.length() == 0) {
			 * errors.add("dtFim", new ActionMessage( "error.campodtFim.required")); }
			 */
		} // fim historica

		// System.out.println("Verifica motivo no form");
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
		// System.out.println("fim Verifica form");
		return errors;

	}

}