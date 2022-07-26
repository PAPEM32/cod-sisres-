package com.sisres.utilitaria;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class Utilitaria {
	/*
	 * formata uma data recebida no padrao americano para o padrão brasileiro
	 * especificado
	 */
	/*
	 * dd - dia MM - mes yy- ano (2 dígitos) / yyyy (4 dígitos)
	 */
	public static String formatarData(String data, String padrao) {
		java.util.Date dataFormatada = new java.util.Date(data);
		Locale brasil = new Locale("pt", "BR");
		SimpleDateFormat formatador = new SimpleDateFormat(padrao);
		formatador.getDateInstance(DateFormat.SHORT, brasil);

		data = formatador.format(dataFormatada);

		return data;
	}

	public static String formatarData(java.util.Date data, String padrao) {
		java.util.Date dataSemFormato = data;
		String dataFormatada;
		Locale brasil = new Locale("pt", "BR");
		SimpleDateFormat formatador = new SimpleDateFormat(padrao);
		formatador.getDateInstance(DateFormat.SHORT, brasil);
		dataFormatada = formatador.format(dataSemFormato);
		return dataFormatada;
	}

	/* formata um valor recebida para a formatacao especificada */
	/*
	 * "#,###,##0.00" - casa decimal com virgula e ponto como separador de milhar
	 * "######0.00" - sem separador de milhar
	 */

	public static String formatarValor(String padraoMoeda, Double valor) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DecimalFormat dff = new DecimalFormat(padraoMoeda, dfs);
		return dff.format(valor);
	}

	public static String formatarValor(String padraoMoeda, String valor) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(',');
		DecimalFormat dff = new DecimalFormat(padraoMoeda, dfs);
		return dff.format(Double.parseDouble(valor.replace(",", ".")));
	}

	public static boolean validaData(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Date testDate = null;
		try {
			testDate = sdf.parse(data);
			// System.out.println(sdf.format(testDate));
		} catch (ParseException e) {
			return false;

		}
		return true;
	}

	public static List criarZip(File arquivoZip, File[] arquivos) throws ZipException, IOException {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		// setArquivoZipAtual( null );
		try {
			// adiciona a extensão .zip no arquivo, caso não exista
			if (!arquivoZip.getName().toLowerCase().endsWith(".zip")) {
				arquivoZip = new File(arquivoZip.getAbsolutePath() + ".zip");
			}
			fos = new FileOutputStream(arquivoZip);
			bos = new BufferedOutputStream(fos, 2048);
			List listaEntradasZip = criarZip(bos, arquivos);
			// setArquivoZipAtual( arquivoZip );
			return listaEntradasZip;
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static List criarZip(OutputStream os, File[] arquivos) throws ZipException, IOException {
		if (arquivos == null || arquivos.length < 1) {
			throw new ZipException("Adicione ao menos um arquivo ou diretório");
		}
		List<ZipEntry> listaEntradasZip = new ArrayList<ZipEntry>();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(os);
			for (int i = 0; i < arquivos.length; i++) {
				String caminhoInicial = arquivos[i].getParent();
				List<ZipEntry> novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
				if (novasEntradas != null) {
					listaEntradasZip.addAll(novasEntradas);
				}
			}
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (Exception e) {
				}
			}
		}
		return listaEntradasZip;
	}

	private static List<ZipEntry> adicionarArquivoNoZip(ZipOutputStream zos, File arquivo, String caminhoInicial)
			throws IOException {
		List<ZipEntry> listaEntradasZip = new ArrayList<ZipEntry>();
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		byte buffer[] = new byte[2048];
		try {
			// diretórios não são adicionados
			if (arquivo.isDirectory()) {
				// recursivamente adiciona os arquivos dos diretórios abaixo
				File[] arquivos = arquivo.listFiles();
				for (int i = 0; i < arquivos.length; i++) {
					List<ZipEntry> novasEntradas = adicionarArquivoNoZip(zos, arquivos[i], caminhoInicial);
					if (novasEntradas != null) {
						listaEntradasZip.addAll(novasEntradas);
					}
				}
				return listaEntradasZip;
			}
			String caminhoEntradaZip = null;
			int idx = arquivo.getAbsolutePath().indexOf(caminhoInicial);
			if (idx >= 0) {
				// calcula os diretórios a partir do diretório inicial
				// isso serve para não colocar uma entrada com o caminho completo
				caminhoEntradaZip = arquivo.getAbsolutePath().substring(idx + caminhoInicial.length());
			}
			ZipEntry entrada = new ZipEntry(caminhoEntradaZip);
			zos.putNextEntry(entrada);
			zos.setMethod(ZipOutputStream.DEFLATED);
			fis = new FileInputStream(arquivo);
			bis = new BufferedInputStream(fis, 2048);
			int bytesLidos = 0;
			while ((bytesLidos = bis.read(buffer, 0, 2048)) != -1) {
				zos.write(buffer, 0, bytesLidos);
			}
			listaEntradasZip.add(entrada);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
		return listaEntradasZip;
	}

}
