/**
 * 
 */
package com.sisres.model;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author user
 * @generated "UML para Java
 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class AltDivMesSispagSvc {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param getDivida
	 * @generated "UML para Java
	 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void getDivida(Divida getDivida) {
		// begin-user-code
		// TODO Stub de método gerado automaticamente

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param divida
	 * @generated "UML para Java
	 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setDivida(Divida divida, String usuario) throws Exception {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.updateDivida(divida, usuario);
	}

	public boolean setLancamento(Lancamento lancamento, String usuario) throws Exception {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.updateLancamento(lancamento, usuario);
	}
}