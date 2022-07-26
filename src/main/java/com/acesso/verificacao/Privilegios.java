package com.acesso.verificacao;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Objetos do HashMap:
 * - "OPERACAO"
 * - "PRIVILEGIO"
 * - "CAMINHO"
 * - "NOMEITEM" 
 */
public class Privilegios {
	private ArrayList<HashMap> menuUsuario;

	public void setMenuUsuario(ArrayList<HashMap> menuUsuario) {
		this.menuUsuario = menuUsuario;
	}

	public ArrayList<HashMap> getMenuUsuario() {
		return menuUsuario;
	}

	public boolean verificaNomeItem(String item) {
		boolean flag = false;
		String nomeitem = null;
		for (HashMap itemHash : menuUsuario) {
			nomeitem = (String) itemHash.get("NOMEITEM");
			if (nomeitem.equalsIgnoreCase(item)) {
				flag = true;
			}
		}
		return flag;
	}

	public String toString() {
		return menuUsuario.toString();

	}
}
