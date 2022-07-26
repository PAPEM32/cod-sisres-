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
public class EstornarLancamentoRevDividaSvc {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @throws SispagException
	 * @generated "UML para Java
	 *            (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String estornarLancamentoReversao(Lancamento lancamento) throws SispagException {
		String resultado;
		DAODivida daoDivida = new DAODivida();
		if (daoDivida.verificarEstorno(lancamento.getIdLancamento()) == true) {
			daoDivida.incluirLancamentoEstornoReversao(lancamento);
			resultado = "true";
		} else {
			resultado = "false";

			// //System.out.println("Já há um estorno para esse lançamento");
		}
		return resultado;
	}

}