package com.sisres.sec;
        
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//TODO: Remover esta classe para usar hash (sha-256 ??) e salt -> alterar banco
public class AdmSegCredMatcher extends SimpleCredentialsMatcher {
	         
	//Logger logger = LoggerFactory.getLogger(AdmSegCredMatcher.class);

	


	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		//logger.debug(((UsernamePasswordToken)token.getCredentials()).getPassword());
		//logger.debug((String) info.getCredentials());
		//boolean ret = super.equals(codificaSenha((String)token.getCredentials()), info.getCredentials());
		Object tcred = token.getCredentials();
		Object icred = info.getCredentials();
		boolean ret = super.equals(tcred, icred);
		return ret;
	}
	
	public AdmSegCredMatcher() {
		super();
	}

}
