
package com.sisres.sec;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;



public class AdmSegPwHashTest{

	AdmSegPwHash credMat = new AdmSegPwHash();
	
	@Test
	public void testCodificaSenha() {
		assertEquals("S6241",credMat.codificaSenha("123456"));
	}

	
	
}
