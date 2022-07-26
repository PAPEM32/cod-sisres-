/**
 * 
 */
package com.sisres.view;

import com.sisres.model.Divida;
import com.sisres.model.ExcDivMesSispagSvc;
import com.sisres.model.SispagException;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author user
 * @generated "UML para Java
 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ExcluirDividaMesSispagForm {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param divida
	 * @generated "UML para Java
	 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void excluirDivida(Divida divida) {
		ExcDivMesSispagSvc excDivSvc = new ExcDivMesSispagSvc();

		try {
			if (excDivSvc.excluirDivida(divida)) {
				// System.out.println("Dívida apagada com sucesso");
			} else {
				// System.out.println("Não foi possível realizar a operação");
			}
		} catch (SispagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}