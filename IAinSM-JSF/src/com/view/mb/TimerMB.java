package com.view.mb;

import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import javax.faces.context.FacesContext;

import com.facade.TimerFacade;
import com.model.TimerIA;

@ManagedBean
@RequestScoped
public class TimerMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1917253090519579669L;

	// Crio um EJB do Facade para ter acesso ao Save.
	@EJB
	private TimerFacade timerFacade;

	// Variavel do Timer
	private TimerIA timerIA;

	// instancia o TimerIA para evitar NullPointerException
	public TimerIA getTimerIA() {
		if (timerIA == null) {
			timerIA = new TimerIA();
		}
		return timerIA;
	}

	public void setTimerIA(TimerIA timerIA) {
		this.timerIA = timerIA;
	}

	public List<TimerIA> getAllTimer() {
		return timerFacade.findAll();
	}
	
	public List<TimerIA> getListTimerByMail(){
		return timerFacade.findListTimerByEmail(userLoginMB.getEmail());
	}
	

	// Pego os dados de UserLoginMB via DI
	@ManagedProperty(value = "#{userLogin}")
	private UserLoginMB userLoginMB;

	public UserLoginMB getUserLoginMB() {
		return userLoginMB;
	}

	public void setUserLoginMB(UserLoginMB userLoginMB) {
		this.userLoginMB = userLoginMB;
	}

	// Metodo que devolve a timeZone correta
	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	// --------------------//

	public void updateTimerEnd() {
		try {

			if (timerIA.getDateInicial().compareTo(timerIA.getDateFinal()) > 0) {

				sendErrorMessageToTimer("Data final esta antes da data Inicial");

			} else if (timerIA.getDateInicial().compareTo(
					timerIA.getDateFinal()) < 0) {
				
				getTimerIA().setEmailIA(userLoginMB.getEmail());
				
				timerFacade.update(timerIA);

			} else if (timerIA.getDateInicial().compareTo(
					timerIA.getDateFinal()) == 0) {
				sendErrorMessageToTimer("As datas s‹o iguais");
			} else {
			}
		} catch (EJBException e) {
			sendErrorMessageToTimer("J‡ existe um Timer setado!");
		}
	}

	public void deleteTimerEnd() {
		try {
			timerFacade.delete(timerIA);
		} catch (EJBException e) {
			sendErrorMessageToTimer("Nao foi possivel dele‹o");

		}
		sendInfoMessageToTimer("Dele‹o realizada com sucesso");

	}

	// ---------------------------------------//

	private void sendInfoMessageToTimer(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				message, message));
	}

	private void sendErrorMessageToTimer(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				message, message));
	}

	private FacesContext getContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

}
