package com.view.mb;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.facade.UserFacade;
import com.model.User;
import com.view.Navegacao.NavegacaoURL;


@ManagedBean(name="userLogin")
@SessionScoped
public class UserLoginMB implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1862986218906533534L;

	@EJB
	private UserFacade userFacade;

	private User user;
	private String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String doEfetuarLogin() {
		try {
			user = userFacade.findUserByEmail(email);
			if (email.equals(user.getEmail())
					&& password.equals(user.getPassword())) {
				
				sendInfoMessageToUser(user.getName().toString() + " Bem vindo ao IAinSM");
				return NavegacaoURL.MENU.getUrl();				
			}
		} catch (NullPointerException e) {
			sendErrorMessageToUser("Usuario n‹o existe!");
			return null;		
		}
		sendErrorMessageToUser("N‹o foi possivel conex‹o!");
		return null;
	}

	public String doCadastrarUsuario() {
		return NavegacaoURL.CADASTRA.getUrl();
	}	
		
	
	//--------------Lindando com as sessoes----------------------//

	public String logOut() {
		getRequest().getSession().invalidate();
		sendInfoMessageToUser(user.getName().toString() + " Logout efetuado com sucesso!");
		return NavegacaoURL.INICIO.getUrl();
	}
	
	private HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance()
				.getExternalContext().getRequest();
	}
	
	private void sendInfoMessageToUser(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				message, message));
	}

	private void sendErrorMessageToUser(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				message, message));
	}

	private FacesContext getContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}
	
	

}
