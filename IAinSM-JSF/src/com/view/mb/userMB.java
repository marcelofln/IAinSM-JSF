package com.view.mb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.facade.RegraNomeFacade;
import com.facade.UserFacade;
import com.model.RegraNome;
import com.model.User;

@ManagedBean
@RequestScoped
public class userMB {

	@EJB
	private UserFacade userFacade;

	@EJB
	private RegraNomeFacade regraNomeFacade;

	private static final String CREATE_USER = "createUser";
	private static final String DELETE_USER = "deleteUser";
	private static final String UPDATE_USER = "updateUser";

	private User user;
	private RegraNome regraNome;

	public RegraNome getRegraNome() {
		if (regraNome == null) {
			regraNome = new RegraNome();
		}

		return regraNome;
	}

	public void setRegraNome(RegraNome regraNome) {
		this.regraNome = regraNome;
	}

	public User getUser() {
		if (user == null) {
			user = new User();
		}
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getAllUser() {
		return userFacade.findAll();
	}

	// ------------Cria Usuario-----------//

	public String createUserStart() {
		return CREATE_USER;
	}

	public void createUserEnd() {

		try {
			getRegraNome().setEmail(user.getEmail());
			getRegraNome().setNomeRegra("Algoritmo do Tipo STOP");

			userFacade.save(user);
			regraNomeFacade.update(regraNome);
			sendInfoMessageToUser("Usuário Cadastrado com Sucesso.");
		} catch (EJBException e) {
			sendErrorMessageToUser("E-mail já Existe!");
		}
		

	}

	// ------------Deleta Usuario-------//

	public String deleteUserStart() {
		return DELETE_USER;
	}

	public void deleteUserEnd() {
		try {
			userFacade.delete(user);
		} catch (EJBException e) {
			sendErrorMessageToUser("Erro ao Deletar.");
		}
		sendInfoMessageToUser("Deleção ocorrida com sucesso.");
	}

	// ------------Altera Usuario-------//

	public String updateUserStart() {
		return UPDATE_USER;
	}

	public void updateUserEnd() {
		try {
			userFacade.update(user);
		} catch (EJBException e) {
			sendErrorMessageToUser("Erro ao alterar Usuário.");
		}
		sendInfoMessageToUser("Alteração ocorrida com sucesso.");
	}

	// --------------Para enviar mensagens----------------------//

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
