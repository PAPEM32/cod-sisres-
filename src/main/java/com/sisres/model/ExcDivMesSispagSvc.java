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
public class ExcDivMesSispagSvc {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param divida
	 * @throws SispagException
	 * @generated "UML para Java
	 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean excluirDivida(Divida divida) throws SispagException {
		DAODivida daoDivida = new DAODivida();
		return daoDivida.deleteDivida(divida);
	}
}