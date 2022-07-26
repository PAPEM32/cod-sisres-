/**
 * 
 */
package com.sisres.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.sisres.view.CadastrarDivHistoricaSispagForm;

public class Divida implements Serializable{

	private int id;
	private int codigo;
	private String mesProcessoGeracao; // Formato do dado: 'mm'
	private Double valor;
	private String tipo;
	private Date dataMotivo;
	private Pessoa pessoa;

	// private String codigoPedido;
	private OC oc;
	private List<Lancamento> lancamento;
	private Pedido pedido;
	private String anoProcessoGeracao; // Formato do dado: 'aaaa'
	private String estado;
	private String cgs; // Formato do dado: 'S' | 'N'
	private String motivo;
	private int idMotivo;
	private String banco;
	private String agencia;
	private String contaCorrente;
	private String anoTermino; // Formato do dado: 'aaaa'
	private String mesTermino; // Formato do dado: 'mm'
	private String docEnvio;
	private String numeroDocEnvio;
	private Date dataDocEnvio;
	private String processoInicioStr;
	private String processoTerminoStr;
	// private int idPedidoRev;
	// private String numrev;
        private List <DividaEmAndamento> dividasEmAndamento;
        private DividaEmAndamento dividaEmAndamento;
        
        public DividaEmAndamento getDividaEmAndamento(){
            return this.dividaEmAndamento;
        }
        
        public void setDividaEmAndamento(DividaEmAndamento dividaEmAndamento){
            this.dividaEmAndamento = dividaEmAndamento;
        }

	public Divida() {
		this.oc = new OC();
		this.pessoa = new Pessoa();
		this.pedido = new Pedido();
		this.lancamento = new ArrayList<Lancamento>();
		this.pedido = new Pedido();

	}

	public Divida(String matFin, String mes, String ano, String cpf, String nom, String pt, String sit, int idOc,
			String codOc, String nomOc, String idbco, String age, String cc, Double valdiv, String oridoc, String tpdoc,
			String nrdoc, String dtDocAutStr, String dtMotStr, String idMot, String obs, String sistemaOrigem) {

		this();
		this.cgs = sistemaOrigem;
		Lancamento lancamentoDivida = new Lancamento();

		OC ocDivida = new OC();
		Pessoa pessoaDivida = new Pessoa();

		mesProcessoGeracao = mes;
		anoProcessoGeracao = ano;

		agencia = age;
		contaCorrente = cc;
		banco = idbco;
		valor = valdiv;

		// OC
		ocDivida.setId(idOc); // busca IDOC pelo cod
		ocDivida.setOc(codOc);
		ocDivida.setNome(nomOc);
		this.setOc(ocDivida);

		motivo = idMot; // id

		// Pega somente a data sem o time retornado quando a dívida vem do banco de
		// dados
		// System.out.println(dtMotStr);
		// System.out.println(dtMotStr.substring(0, 10));
		Date dataMot = Date.valueOf(dtMotStr.substring(0, 10));

		dataMotivo = dataMot;

		// Pessoa
		pessoaDivida.setCpf(cpf);
		if (sistemaOrigem.equals("S")) // S --> Divida oriunda do SISPAG | N --> Divida oriunda do SIAPE
			pessoaDivida.setMatFin(matFin);
		else
			pessoaDivida.setMatSIAPE(matFin);
		pessoaDivida.setNome(nom);
		pessoaDivida.setPosto(pt);
		pessoaDivida.setSituacao(sit);
		// pessoa.setId("SRESPES_ID");
		this.setPessoa(pessoaDivida);

		// Lancamento
		lancamentoDivida.setOrigemDocAutorizacao(oridoc);
		lancamentoDivida.setTipoDocAutorizacao(tpdoc);
		lancamentoDivida.setNumeroDocAutorizacao(nrdoc);

		// Pedido

		// Pega somente a data sem o time retornado quando a dívida vem do banco de
		// dados
		Date dataDocAut = Date.valueOf(dtDocAutStr.substring(0, 10));

		lancamentoDivida.setDataDocAutorizacao(dataDocAut);

		lancamentoDivida.setObservacao(obs);

		this.setLancamento(lancamentoDivida);
	}

	public Divida(int codDivida, String matFin, Double valorDivida, String origemDocAut, String tipoDocAut,
			String numeroDocAut, String dataDocAut, String tipoDocEnvio, String numeroDocEnvio, String DataDocEnvio,
			String dataMotivo2, int idMotivo, int idPessoa) {

		this();

		// OC ocDivida = new OC();
		Pessoa pessoaDivida = new Pessoa();
		Lancamento lancamentoDivida = new Lancamento();
		// pessoaDivida.setMatFin(matFin);
		this.valor = valorDivida;

		// Lancamento
		lancamentoDivida.setOrigemDocAutorizacao(origemDocAut);
		lancamentoDivida.setTipoDocAutorizacao(tipoDocAut);
		lancamentoDivida.setNumeroDocAutorizacao(numeroDocAut);
		Date dataDate = Date.valueOf(dataDocAut.substring(0, 10));
		lancamentoDivida.setDataDocAutorizacao(dataDate);
		this.setLancamento(lancamentoDivida);

		this.docEnvio = tipoDocEnvio;
		this.numeroDocEnvio = numeroDocEnvio;

		// Pega somente a data sem o time retornado quando a dívida vem do banco de
		// dados
		this.dataDocEnvio = Date.valueOf(DataDocEnvio.substring(0, 10));
		// Pega somente a data sem o time retornado quando a dívida vem do banco de
		// dados
		Date dataMot = Date.valueOf(dataMotivo2.substring(0, 10));

		this.dataMotivo = dataMot;
		this.idMotivo = idMotivo;
		pessoaDivida.setId(idPessoa);
		this.setPessoa(pessoaDivida);
	}

	public Divida(String sistemaOrigem, String codigo, String matFin, String processoInicioStr, String processoTermino,
			String cpf, String nom, String pt, String sit, int idOc, String codOc, String nomOc, String idbco,
			String age, String cc, double valdiv, String tipodiv, String oridoc, String tpdoc, String nrdoc,
			String dtDocAutStr, String dtMotStr, String idMot, String docEnvio, String numeroDocEnvio,
			Date dataDocEnvio, String motivo, int idPessoa, int LancDivId, int LancCodigo) {

		this();

		OC oc = new OC();
		Pessoa pessoa = new Pessoa();
		Lancamento lancamento = new Lancamento();

		this.codigo = Integer.parseInt(codigo);
		this.cgs = sistemaOrigem;
		this.setProcessoInicioStr(processoInicioStr);
		this.setProcessoTerminoStr(processoTermino);

		this.agencia = age;
		this.contaCorrente = cc;
		this.banco = idbco;
		this.valor = valdiv;
		this.tipo = tipodiv;

		this.docEnvio = docEnvio;
		this.numeroDocEnvio = numeroDocEnvio;
		this.dataDocEnvio = dataDocEnvio;

		// OC
		oc.setId(idOc); // busca IDOC pelo cod
		oc.setOc(codOc);
		oc.setNome(nomOc);
		this.setOc(oc);

		this.idMotivo = Integer.parseInt(idMot);
		this.motivo = motivo; // id

		// Pega somente a data sem o time retornado quando a dívida vem do banco de
		// dados

		Date dataMot = Date.valueOf(dtMotStr.trim().substring(0, 10));
		this.dataMotivo = dataMot;

		// Pessoa
		pessoa.setCpf(cpf);
		pessoa.setMatFin(matFin);
		pessoa.setNome(nom);
		pessoa.setPosto(pt);
		pessoa.setSituacao(sit);
		// pessoa.setId(idPessoa);
		this.setPessoa(pessoa);

		// Lancamento
		lancamento.setDividaId(LancDivId);
		lancamento.setCodigo(LancCodigo);
		lancamento.setOrigemDocAutorizacao(oridoc);
		lancamento.setTipoDocAutorizacao(tpdoc);
//		lancamento.setNumeroDocAutorizacao(Integer.parseInt(nrdoc));
		lancamento.setNumeroDocAutorizacao(nrdoc);

		// Pega somente a data sem o time retornado quando a dívida vem do banco de
		// dados
		Date dataDocAut = Date.valueOf(dtDocAutStr.trim().substring(0, 10));
		lancamento.setDataDocAutorizacao(dataDocAut);

//		lancamento.setObservacao(obs);

		this.setLancamento(lancamento);
	}

	public Divida(String matFin, CadastrarDivHistoricaSispagForm myForm, int idOC) {

		this();

		// System.out.println("Entrei no MOdel");

		Lancamento lancamentoDivida = new Lancamento();

		OC ocDivida = new OC();
		Pessoa pessoaDivida = new Pessoa();

		// System.out.println("Valor: "+myForm.getValorDivida());

		// System.out.println("Dados d da Divida");

		// System.out.println(myForm.getMesIni());
		// System.out.println(myForm.getAnoIni());
		// System.out.println(myForm.getMesTerm());
		// System.out.println(myForm.getAnoTerm());

		// System.out.println("Dados do Banco");

		// System.out.println(myForm.getAgencia());
		// System.out.println(myForm.getContaCorrente());
		// System.out.println(myForm.getBanco());
		// System.out.println(myForm.getValorDivida());

		// System.out.println("Dados do Motivo");

		// System.out.println(myForm.getDataMotivo());
		// System.out.println(myForm.getMotivo());

		// System.out.println("Dados da OC");

		// OC
		// System.out.println(idOC); // busca IDOC pelo cod
		// System.out.println(myForm.getOcCodigo());
		// System.out.println(myForm.getOcNome());
		// System.out.println(ocDivida);

		// System.out.println("Dados da Pessoa");
		// Pessoa
		// System.out.println(myForm.getCpf());
		// System.out.println(matFin);
		// System.out.println(myForm.getNome());
		// System.out.println(myForm.getPosto());
		// System.out.println(myForm.getSituacao());

		// System.out.println("Dados do Lancamento");
		// Lancamento
		// System.out.println(myForm.getOrigemDocAut());
		// System.out.println(myForm.getTipoDocAut());
		// System.out.println(myForm.getNumeroDocAut());
		// System.out.println(myForm.getDataDocAut());
		// System.out.println(myForm.getObservacao());
		// System.out.println(myForm.getNumDocEnvio());
		// System.out.println(myForm.getDataDocEnvio());
		// System.out.println(myForm.getTipoDocEnvio());

		this.mesProcessoGeracao = myForm.getMesIni();
		this.anoProcessoGeracao = myForm.getAnoIni();
		this.mesTermino = myForm.getMesTerm();
		this.anoTermino = myForm.getAnoTerm();

		this.agencia = myForm.getAgencia();
		this.contaCorrente = myForm.getContaCorrente();
		this.banco = myForm.getBanco();
		this.valor = myForm.getValorDivida();
		this.docEnvio = myForm.getTipoDocEnvio();
		this.numeroDocEnvio = myForm.getNumDocEnvio();

		/*
		 * try{ this.numeroDocEnvio = Integer.parseInt(myForm.getNumDocEnvio());
		 * }catch(NumberFormatException n){
		 * 
		 * //System.out.println("Dados invalidos.");
		 * 
		 * }
		 * 
		 */
		try {
			Date dataEnvio = Date.valueOf(myForm.getDataDocEnvio());
			this.dataDocEnvio = dataEnvio;

		} catch (Exception ex) {

			// System.out.println("Dados Invalidos");
		}

		Date dataMot = Date.valueOf(myForm.getDataMotivo());
		this.dataMotivo = dataMot;
		this.motivo = myForm.getMotivo();

		// OC
		ocDivida.setId(idOC); // busca IDOC pelo cod
		ocDivida.setOc(myForm.getOcCodigo());
		ocDivida.setNome(myForm.getOcNome());
		this.setOc(ocDivida);

		// Pessoa
		pessoaDivida.setCpf(myForm.getCpf());
		pessoaDivida.setMatFin(matFin);
		pessoaDivida.setNome(myForm.getNome());
		pessoaDivida.setPosto(myForm.getPosto());
		pessoaDivida.setSituacao(myForm.getSituacao());
		// pessoa.setId("SRESPES_ID");
		this.setPessoa(pessoaDivida);

		// Lancamento
		lancamentoDivida.setOrigemDocAutorizacao(myForm.getOrigemDocAut());
		lancamentoDivida.setTipoDocAutorizacao(myForm.getTipoDocAut());
//		lancamentoDivida.setNumeroDocAutorizacao(Integer.parseInt(myForm.getNumeroDocAut()));
		lancamentoDivida.setNumeroDocAutorizacao(myForm.getNumeroDocAut());
		Date dataDate = Date.valueOf(myForm.getDataDocAut());
		lancamentoDivida.setDataDocAutorizacao(dataDate);
		lancamentoDivida.setObservacao(myForm.getObservacao());

		this.setLancamento(lancamentoDivida);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMesProcessoGeracao() {
		return mesProcessoGeracao;
	}

	public void setMesProcessoGeracao(String mesProcessoGeracao) {
		this.mesProcessoGeracao = mesProcessoGeracao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getDataMotivo() {
		return dataMotivo;
	}

	public void setDataMotivo(Date dataMotivo) {
		this.dataMotivo = dataMotivo;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public OC getOc() {
		return oc;
	}

	public void setOc(OC oc) {
		this.oc = oc;
	}

	public String getAnoProcessoGeracao() {
		return anoProcessoGeracao;
	}

	public void setAnoProcessoGeracao(String anoProcessoGeracao) {
		this.anoProcessoGeracao = anoProcessoGeracao;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCgs() {
		return cgs;
	}

	public void setCgs(String cgs) {
		this.cgs = cgs;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {

		this.motivo = motivo;

	}

	public String getBanco() {

		return banco;

	}

	public void setBanco(String banco) {

		this.banco = banco;

	}

	public String getAgencia() {

		return agencia;

	}

	public void setAgencia(String agencia) {

		this.agencia = agencia;

	}

	public String getContaCorrente() {

		return contaCorrente;

	}

	public void setContaCorrente(String contaCorrente) {

		this.contaCorrente = contaCorrente;

	}

	public List<Lancamento> getLancamentos() {
		return lancamento;

	}

	public void setLancamento(Lancamento lanc) {
		lancamento.add(lanc);
	}

	public Lancamento getLancamentoInicial() {
		// Lancamento lancamento = new Lancamento();
		if (!lancamento.isEmpty())
			return lancamento.get(0);
		else
			return null;
	}

	public void setAnoTermino(String anoTermino) {
		this.anoTermino = anoTermino;
	}

	public String getAnoTermino() {
		return anoTermino;
	}

	public void setMesTermino(String mesTermino) {
		this.mesTermino = mesTermino;
	}

	public String getMesTermino() {
		return mesTermino;
	}

	public void setDocEnvio(String docEnvio) {
		this.docEnvio = docEnvio;
	}

	public String getDocEnvio() {
		return docEnvio;
	}

	public void setNumeroDocEnvio(String numeroDocEnvio) {
		this.numeroDocEnvio = numeroDocEnvio;
	}

	public String getNumeroDocEnvio() {
		return numeroDocEnvio;
	}

	public void setDataDocEnvio(Date dataDocEnvio) {
		this.dataDocEnvio = dataDocEnvio;
	}

	public Date getDataDocEnvio() {
		return dataDocEnvio;
	}

	public int getIdMotivo() {
		return idMotivo;
	}

	public void setIdMotivo(int idMotivo) {
		this.idMotivo = idMotivo;
	}

	public void setProcessoInicioStr(String processoInicioStr) {
		this.processoInicioStr = processoInicioStr;
	}

	public String getProcessoInicioStr() {
		return processoInicioStr;
	}

	public void setProcessoTerminoStr(String processoTerminoStr) {
		this.processoTerminoStr = processoTerminoStr;
	}

	public String getProcessoTerminoStr() {
		return processoTerminoStr;
	}
}
/*
 * public int getIdPedidoRev() { return idPedidoRev; }
 * 
 * public void setIdPedidoRev(int idPedidoRev) { this.idPedidoRev = idPedidoRev;
 * } }
 */