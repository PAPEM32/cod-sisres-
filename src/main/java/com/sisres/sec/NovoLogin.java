package com.sisres.sec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acesso.verificacao.Privilegios;
import com.acesso.verificacao.VerificarPerfil;

import papem32.UsuarioService;

public class NovoLogin extends Action {


	final Logger logger = LoggerFactory.getLogger(NovoLogin.class);
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {

		ActionMessages messages = new ActionMessages();		
		LoginForm formBean = (LoginForm) form;		
		String usuario = formBean.getUsuario();
		String senha = formBean.getSenha();
		
		
		///Invalida sessao aberta
		HttpSession sessao = request.getSession(false);
		if (sessao != null)
			SecUtils.closeSession(request, response);
			
		//Autentica
		messages.add(autenticaUsuario(usuario, senha));

		if (messages.size() > 0) {
			saveErrors(request, messages);
			return mapping.findForward("erro");
		}

		
		try {
			//Configuracao inicial de sessao
			sessao = request.getSession();
			sessao.setAttribute("usuario", usuario);	
                        UsuarioService usuarioService = new UsuarioService();
                        String role = usuarioService.getRole(usuario);
			//sessao.setAttribute("role", new UsuarioService().getRole(usuario));
                        sessao.setAttribute("role", role);
			sessao.setAttribute("autenticado", true);

			//carrega perfil na sessao
			carregaPerfilUsuario(usuario, sessao);

			return mapping.findForward("sucesso");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.genericError", e.getMessage()));
			saveErrors(request, messages);
			return mapping.findForward("erro");
		}

	}


	private void carregaPerfilUsuario(String usuario, HttpSession sessao) {
		VerificarPerfil infoUsuario = new VerificarPerfil();
		Privilegios privilegios = infoUsuario.getRetornaPrivilegioUsuario(usuario);

		//String codOc = infoUsuario.getOc(usuario).trim(); // TODO: P32 pega op nip pelo usuario, deve-se incluir NIP
		String codOc = "004"; 
		//String codOm = infoUsuario.getOm(usuario).trim(); // TODO: P32 pega op nip pelo usuario, deve-se incluir NIP
		String codOm = "094"; 
		
		String perfilUsuario = infoUsuario.getPerfil(usuario);
		String nomeUsuario = infoUsuario.getNome(usuario);
		// senhaCriptografada = infoUsuario.codificaSenha(senhaURL);
		//String grupo = infoUsuario.getIdGrupoUsuario(usuario);
		//String idUsuario = infoUsuario.getIdUsuario(usuario);

		// setando os atributos da sessão
		sessao.setAttribute("perfilUsuario", perfilUsuario);
		sessao.setAttribute("codOc", codOc);
		sessao.setAttribute("codOm", codOm);
		sessao.setAttribute("privilegios", privilegios);
		sessao.setAttribute("nomeUsuario", nomeUsuario);
		sessao.setAttribute("usuarioSispag", usuario);
		//sessao.setAttribute("usuarioSisres", "SISRES");
		//sessao.setAttribute("passwordSisres", "SISRES");
		//sessao.setAttribute("usuarioSispag", usuario);
		//sessao.setAttribute("passwordSispag", senha);
	}


	private ActionMessages autenticaUsuario(String usuario, String senha) {
		
		ActionMessages messages = new ActionMessages();
		senha = new AdmSegPwHash().codificaSenha(senha);
		
		//TODO: teste de Senha		
		//senha = "S28662";
		
		
		UsernamePasswordToken token = new UsernamePasswordToken(usuario, senha);
		
		
		Subject currentUser = SecurityUtils.getSubject();

		try {
			currentUser.login(token);
		} catch (UnknownAccountException uae) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.usu_senha_invalida"));
		} catch (IncorrectCredentialsException ice) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.usu_senha_invalida"));
		} catch (LockedAccountException lae) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.conta_bloqueada"));
		} catch (ExcessiveAttemptsException eae) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.max_tent_login"));
		} catch (AuthenticationException ae) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.nao_esperado"));
			logger.error(ae.getMessage());
		}
		return messages; 
	}

	
}
