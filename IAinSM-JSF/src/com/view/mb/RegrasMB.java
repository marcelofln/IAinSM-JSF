package com.view.mb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.facade.CodNegociacaoFacade;
import com.facade.RegraFacade;
import com.facade.RegraNomeFacade;
import com.model.CodNegociacao;
import com.model.Regra;
import com.model.RegraNome;
import com.model.RegraPK;
import com.util.FacesUtils;
import com.view.Navegacao.NavegacaoURL;


@ManagedBean(name = "regrasMB")
@RequestScoped
public class RegrasMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 562090580584470406L;

	private String nomeSelecionado;
	private String codigoSelecionado;
	private String apelido;
	private String ganhoSelecionado;
	private String perdaSelecionado;
	private String periodo;
	private String valorCompra;
	private boolean mvc;
	private boolean nlg;
	private boolean nlp;
	private boolean tRegra;
	
	
	

	public String getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(String valorCompra) {
		this.valorCompra = valorCompra;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public boolean isMvc() {
		return mvc;
	}

	public void setMvc(boolean mvc) {
		this.mvc = mvc;
	}

	public boolean isNlg() {
		return nlg;
	}

	public void setNlg(boolean nlg) {
		this.nlg = nlg;
	}

	public boolean isNlp() {
		return nlp;
	}

	public void setNlp(boolean nlp) {
		this.nlp = nlp;
	}

	public boolean istRegra() {
		return tRegra;
	}

	public void settRegra(boolean tRegra) {
		this.tRegra = tRegra;
	}

	public String getGanhoSelecionado() {
		return ganhoSelecionado;
	}

	public void setGanhoSelecionado(String ganhoSelecionado) {
		this.ganhoSelecionado = ganhoSelecionado;
	}

	public String getPerdaSelecionado() {
		return perdaSelecionado;
	}

	public void setPerdaSelecionado(String perdaSelecionado) {
		this.perdaSelecionado = perdaSelecionado;
	}


	
	@EJB
	private static RegraNomeFacade regraNomeFacade;

	@EJB
	private static CodNegociacaoFacade codNegociacaoFacade;

	@EJB
	private static RegraFacade regraFacade;

	private RegraNome regraNome;
	private CodNegociacao codNegociacao;
	private RegraPK regraPK;
	private Regra regra;

	// ------------Getters e Setters-------------//

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getNomeSelecionado() {
		return nomeSelecionado;
		
	}

	public void setNomeSelecionado(String nomeSelecionado) {
		this.nomeSelecionado = nomeSelecionado;
	}

	public String getCodigoSelecionado() {
		return codigoSelecionado;
	}

	public void setCodigoSelecionado(String codigoSelecionado) {
		this.codigoSelecionado = codigoSelecionado;
	}

	public Regra getRegra() {

		if (regra == null) {
			regra = new Regra();
		}

		return regra;
	}

	public void setRegra(Regra regra) {
		this.regra = regra;
	}

	public RegraPK getRegraPK() {

		if (regraPK == null) {
			regraPK = new RegraPK();
		}
		return regraPK;
	}

	public void setRegraPK(RegraPK regraPK) {
		this.regraPK = regraPK;
	}

	public RegraNome getRegraNome() {
		if (regraNome == null) {
			regraNome = new RegraNome();
		}
		return regraNome;
	}

	public void setRegraNome(RegraNome regraNome) {
		this.regraNome = regraNome;
	}

	// ----------Pego os dados de UserLoginMB via DI----------//

	@ManagedProperty(value = "#{userLogin}")
	private UserLoginMB userLoginMB;

	public UserLoginMB getUserLoginMB() {
		return userLoginMB;
	}

	public void setUserLoginMB(UserLoginMB userLoginMB) {
		this.userLoginMB = userLoginMB;
	}

	// ------------Listas----------//

	private List<SelectItem> nomes;

	public List<SelectItem> getNomesRegrasMarcelo() {

		nomes = new ArrayList<SelectItem>();

		for (RegraNome i : regraNomeFacade.findRegraByEmail(userLoginMB.getEmail().toString())) {
			nomes.add(new SelectItem(i.getNomeRegra()));
		}
		return nomes;

	};

	private List<SelectItem> codigos;

	public List<SelectItem> getCodigosMarcelo() {

		codigos = new ArrayList<SelectItem>();

		for (CodNegociacao i : codNegociacaoFacade.findAll()) {
			codigos.add(new SelectItem(i.getCodNegociacao()));
		}
		return codigos;
	};

	
	public List<Regra> getListRegrasByEmail(){
		
		return regraFacade.FindRegraListByEmail(userLoginMB.getEmail());
		
	}
		
	public void retornoChanged(){
		
		
		regra = regraFacade.findRegraByApelido(nomeSelecionado);

		codigoSelecionado = regra.getRegraPK().getCodNegociacao().trim().toString();	
		apelido=regra.getApelido().trim().toString();
		ganhoSelecionado = Double.toString(regra.getStopGanho());
		perdaSelecionado = Double.toString( regra.getStopPerda());
		periodo =  Double.toString(regra.getPeriodo());	
		mvc = regra.ismValorCompra();
		nlg = regra.isnLimiteGanho();
		nlp = regra.isnLimitePerda();
		tRegra = regra.isTestarRegra();
		
	}

	
	
//	public String doEfetuarLogin() {
//		try {
//			user = userFacade.findUserByEmail(email);
//			if (email.equals(user.getEmail())
//					&& password.equals(user.getPassword())) {
//				
//				sendInfoMessageToUser(user.getName().toString() + " Bem vindo ao IAinSM");
//				return NavegacaoURL.MENU.getUrl();				
//			}
//		} catch (NullPointerException e) {
//			sendErrorMessageToUser("Usuario n‹o existe!");
//			return null;		
//		}
//		sendErrorMessageToUser("N‹o foi possivel conex‹o!");
//		return null;
//	}
	
	
	
	
	
	public String doGravar() {

		try {
			
			regra = regraFacade.findRegraByApelido(nomeSelecionado + " # " + apelido);
			
			if((nomeSelecionado + " # " + apelido).equals(regra.getApelido()))
				{					
				sendErrorMessageToRegras("Regra J‡ Existe.");
				}
			}catch (NullPointerException e) {
				
				double ganhoS = Double.parseDouble(getGanhoSelecionado());
				double perdaS = Double.parseDouble(getPerdaSelecionado());
				double peridoS = Double.parseDouble(getPeriodo());
				double compraS = Double.parseDouble(getValorCompra());
				
				BigDecimal valor = new BigDecimal(compraS);  
				valor.setScale(3, BigDecimal.ROUND_UP); 
				double compraSFormatado = valor.doubleValue();  
				
				
				getRegraPK().setCodNegociacao(codigoSelecionado);
				getRegraPK().setNomeRegra(nomeSelecionado);
				getRegra().setRegraPK(regraPK);
				getRegra().setEmail_usuario(userLoginMB.getEmail());
				getRegra().setApelido(nomeSelecionado + " # " + apelido);
				getRegra().setStopGanho(ganhoS);
				getRegra().setStopPerda(perdaS);
				getRegra().setPeriodo(peridoS);
				getRegra().setmValorCompra(mvc);
				getRegra().setnLimiteGanho(nlg);
				getRegra().setnLimitePerda(nlp);
				getRegra().setTestarRegra(tRegra);
				getRegra().setValorCompra(compraSFormatado);
				getRegra().setValorGanho(compraSFormatado*(1+(ganhoS/100)));
				getRegra().setValorPerda(compraSFormatado*(1-(perdaS/100)));

				getRegraNome().setNomeRegra(nomeSelecionado + " # " + apelido);
				getRegraNome().setEmail(userLoginMB.getEmail());

				regraNomeFacade.update(regraNome);
				regraFacade.update(regra);			
				
				sendInfoMessageToRegras("Sucesso ao criar regra.");	
			}
			return null;

	}

	// ---------------------------------------//

	public String getPeriodo() {
		return periodo;
	}

	private void sendInfoMessageToRegras(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				message, message));
	}

	private void sendErrorMessageToRegras(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				message, message));
	}

	private FacesContext getContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context;
	}

}
