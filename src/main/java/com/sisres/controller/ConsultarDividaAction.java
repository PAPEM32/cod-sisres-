package com.sisres.controller;

//Front Controller
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sisres.model.ConsultaDividaSvc;
import com.sisres.model.Divida;
import com.sisres.model.Lancamento;
import com.sisres.model.Motivo;
import com.sisres.model.Pedido;
import com.sisres.model.Pessoa;
import com.sisres.model.SispagException;
import com.sisres.utilitaria.Pager;
import com.sisres.view.ConsultarDividaForm;

public class ConsultarDividaAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ServletContext context = getServlet().getServletContext();
		HttpSession session = request.getSession();
		// context.log("Action - passei por aqui 1");

		ActionErrors listaErros = new ActionErrors();

		try {

			// System.out.println("Entrou no action consultar divida agora!!!! e
			// testando...");
			ConsultaDividaSvc consultaDividaSvc = new ConsultaDividaSvc();
			ConsultarDividaForm consulta = (ConsultarDividaForm) form;
			Divida divida = new Divida();
			Pedido pedido;

			String[] queryParameters = new String[32];

			// popular o Array
			queryParameters[0] = consulta.getCodDivida();
			// só armazena o código da dívida se realmente houve algum valor para não gerar
			// erro
			if (!consulta.getCodDivida().equals("")) {
				divida.setCodigo(Integer.parseInt(consulta.getCodDivida()));
			}

			queryParameters[1] = consulta.getTipoDivida();
			divida.setTipo(consulta.getTipoDivida());

			queryParameters[2] = consulta.getOrigemDocInclusao();
			Lancamento lanc = new Lancamento();
			lanc.setOrigemDocAutorizacao(consulta.getOrigemDocInclusao());
			divida.setLancamento(lanc);

			queryParameters[3] = consulta.getMatfin();
			Pessoa pes = new Pessoa();
			pes.setMatFin(consulta.getMatfin());
                        
                        
                        /*Filtros novos*/
                        queryParameters[22] = consulta.getCpf();
                        pes.setCpf(consulta.getCpf());
                        
                                              
			/*queryParameters[23] = consulta.getOc();
			queryParameters[24] = consulta.getEstado();
			queryParameters[25] = consulta.getPeriodo();
			queryParameters[26] = consulta.getValorAtual();*/
                        
                        

			queryParameters[21] = consulta.getMotivo();
			Motivo motivo = new Motivo();
			motivo.setNome(consulta.getMotivo());

			queryParameters[4] = consulta.getNome();
			pes.setNome(consulta.getNome());
			divida.setPessoa(pes);

			queryParameters[5] = consulta.getNumDocInclusao();
			lanc.setNumeroDocAutorizacao(consulta.getNumDocInclusao());

			queryParameters[6] = consulta.getTipoDocInclusao();
			lanc.setTipoDocAutorizacao(consulta.getTipoDocInclusao());

			queryParameters[7] = consulta.getDtDocInclusao();

//			DateFormat dfS = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dfS = new SimpleDateFormat("dd/MM/yyyy");

			// só armazena a data se realmente houve algum valor para não gerar erro
			if (!consulta.getDtDocInclusao().equals("")) {
				try {
					// incluia a data no formato date.SQL

					lanc.setDataDocAutorizacao(dfS.parse(consulta.getDtDocInclusao()));
				} catch (ParseException edf) {
					edf.printStackTrace();
				}
			} // end do if para verificar se data está vazia

			// Filtra dívida com número específico de Reversao
			if (consulta.getPedidoReversao().equals("0")) {
				queryParameters[8] = "";
				queryParameters[13] = consulta.getPedidoReversao();

				pedido = new Pedido();
				pedido.setCodigo(consulta.getPedidoReversao());
				divida.setPedido(pedido);
			}

			else if (!(consulta.getPedidoReversao() == null) || !(consulta.getPedidoReversao().equals(""))) {
				queryParameters[8] = consulta.getPedidoReversao();
				queryParameters[13] = "";
				pedido = new Pedido();
				pedido.setCodigo(consulta.getPedidoReversao());
				divida.setPedido(pedido);
			}
                        
                        
                        
                        
                        
                        
                        
			// Caso queira filtrar somente dívida que não tenham origem em algum pedido de
			// reversão

			queryParameters[9] = consulta.getSistemaOrigem();
			divida.setCgs(consulta.getSistemaOrigem());

			// Da posição 10 ao 17 serão usadas para DAO de dívida histórica

			queryParameters[10] = consulta.getStatusDivida();
			divida.setEstado(consulta.getStatusDivida());

			// Parametros do campo
			queryParameters[11] = consulta.getOpvalor();
			session.setAttribute("opValor", consulta.getOpvalor());

			queryParameters[12] = consulta.getPvalor();
			session.setAttribute("pValor", consulta.getPvalor());

			queryParameters[14] = "";
			queryParameters[15] = "";
			queryParameters[16] = "";
			queryParameters[17] = "";

			queryParameters[14] = "";
			queryParameters[15] = "";
			queryParameters[16] = "";
			queryParameters[17] = "";
			// queryParameters[21] = consulta.getMotivo();
                        
                        String x = (String) request.getSession().getAttribute("role");

			/*
			 * Passo o código da OC como parâmetro para consulta caso o usuário logado tenha
			 * perfil diferente de 'ADMINISTRADOR' OU 'PAPEM-23'.
			 */
			if (!(request.getSession().getAttribute("role").equals("ADMINISTRADOR")
					|| request.getSession().getAttribute("role").equals("PAPEM-23"))) {
				// Recuperando o código da OC do usuário logado.
				queryParameters[18] = (String) request.getSession().getAttribute("codOc");
				 System.out.println("parametro 18:"+ queryParameters[18]);
			} else {
				// Caso o perfil do usuário seja 'ADMINISTRADOR' OU 'PAPEM-23'
				queryParameters[18] = "";
			}

			int totalRegistros = consultaDividaSvc.getQtdTotalDividas(queryParameters);

			session.setAttribute("dividaFiltro", divida);

			Pager pager = null;
			Object objectPager = session.getAttribute("pager");
			if (objectPager == null) {
				pager = new Pager();
			} else {
				pager = (Pager) objectPager;
			}

			pager.setTotalRegistros(totalRegistros);

			/*
			 * if (!"".equals(consulta.getPagina())) {
			 * pager.setPagSelecionada(Integer.valueOf( consulta.getPagina() )); }
			 */

//			session.setAttribute("pager", pager );

			pager.setQtdPaginas(Integer.valueOf(pager.getQtdPaginas()));

			/*
			 * //System.out.println("Consulta registros por pagina :" +
			 * consulta.getRegistrosPorPagina());
			 * 
			 * 
			 * pager.setRegistrosPorPagina(Integer.valueOf(consulta.getRegistrosPorPagina())
			 * );
			 */

			queryParameters[19] = String.valueOf(pager.getLinhaInicio());
			queryParameters[20] = String.valueOf(pager.getLinhaFim());
			
			
			
			/*
			 * for (int i = 0; i < queryParameters.length; i++) { System.out.print(i + " " +
			 * queryParameters[i].toString().trim());. }
			 */

			ArrayList<Divida> listDivida = consultaDividaSvc.getDividaList(queryParameters);

			// seta o atributo que sera lido pelo browser

			request.getSession().setAttribute("listDivida", listDivida);

			return mapping.findForward("success");

		} catch (SispagException e) {
			listaErros.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			if (listaErros.size() > 0) {
				saveErrors(request, listaErros);
			}
			return mapping.findForward("error.filtro");
		}

	}// fim do metodo

} // fim da classe
