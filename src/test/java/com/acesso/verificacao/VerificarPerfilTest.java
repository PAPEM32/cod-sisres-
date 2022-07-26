/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acesso.verificacao;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.Before;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.Test;

/**
 *
 * @author mariana-cruz
 */
public class VerificarPerfilTest {

    VerificarPerfil vf = new VerificarPerfil();
    DataSource dataSource = null;
    
    public VerificarPerfilTest() {

		

	}

       
    
    
   // @Test
    public void getIdUsuarioTest(){
        try {
			Context ic = new InitialContext();
			dataSource = (DataSource) ic.lookup("java:/jdbc/sisres2"); //jdbc/admseg
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}
        assertEquals("9243", vf.getIdUsuario("094-30-19148771"));
    }

   

   
}
