package com.sisres.utilitaria;

public class Pager {

	// Setado fixamente na classe Pager
	private int registrosPorPagina = 50;

	// Setado após um SELECT(COUNT) no DAO
	private int totalRegistros;

	// Setado na JSP após clique em uma página
	private int pagSelecionada = 1;

	private int QtdPaginas = 400;

	public int getRegistrosPorPagina() {
		return registrosPorPagina;
	}

	public void setRegistrosPorPagina(int registrosPorPagina) {
		this.registrosPorPagina = registrosPorPagina;
	}

	public int getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public int getPagSelecionada() {
		return pagSelecionada;
	}

	public void setQtdPaginas(int QtdPaginas) {
		this.QtdPaginas = QtdPaginas;
	}

	public void setPagSelecionada(int pagSelecionada) {
		this.pagSelecionada = pagSelecionada;
	}

	// Método chamado como primeiro parâmetro do ROWNUM BETWEEN no DAO
	public int getLinhaInicio() {
		return (registrosPorPagina * (pagSelecionada - 1)) + 1;
	}

	// Método chamado como segundo parâmetro do ROWNUM BETWEEN no DAO
	public int getLinhaFim() {
		return registrosPorPagina * (pagSelecionada);
	}

	// Método chamado na JSP para montar a paginação
	public int getQtdPaginas() {
		if (totalRegistros % registrosPorPagina == 0) {
			return Integer.valueOf(totalRegistros / registrosPorPagina);
		} else {
			return Integer.valueOf(totalRegistros / registrosPorPagina) + 1;
		}
	}

	public boolean getExistePaginaAnterior() {
		return this.pagSelecionada > 1;
	}

}

// Método main para teste
/*
 * public static void main(String[] args) { Pager pager = new Pager();
 * 
 * pager.setPagSelecionada(2); pager.setTotalRegistros(500);
 * 
 * //System.out.println(pager.getLinhaInicio());
 * //System.out.println(pager.getLinhaFim());
 * //System.out.println(pager.getQtdPaginas()); } }
 * 
 */
