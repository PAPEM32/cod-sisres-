package com.sisres.sec;

public class AdmSegPwHash {
	
	public char[] decodificaURL(String url) {
		char[] buffer = new char[255];
		char[] value = buffer;
		buffer = url.toCharArray();
		for (int i = 0; i <= buffer.length - 1; i++) {
			value[i] = (char) (16 ^ ((int) ((char) (23 ^ (int) buffer[i]))));
		} // end do for*/
		return value;
	}

	public String codificaSenha(String senha) {
		double bufferSenha = 0;
		for (int i = 0; i < senha.length(); i++) {
			bufferSenha = bufferSenha + (Math.floor((int) Math.pow(((int) senha.charAt(i)), 2) / (i + 1)));
		}
		return "S" + (int) bufferSenha;
	}

}
