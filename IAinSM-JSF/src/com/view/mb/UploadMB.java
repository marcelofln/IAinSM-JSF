package com.view.mb;

import java.io.DataInputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.facade.HistoricoControlFacade;
import com.facade.HistoricoFacade;
import com.facade.UserFacade;
import com.model.Historico;
import com.model.HistoricoControl;
import com.model.User;


@ManagedBean(name = "uploadMB")
@RequestScoped
public class UploadMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7485776264635348436L;

	private UploadedFile file;

	@EJB
	private HistoricoControlFacade historicoControlFacade;

	@EJB
	private HistoricoFacade historicoFacade;

	@EJB
	private UserFacade userFacade;

	private Historico historico;
	private HistoricoControl historicoControl;
	private User user;
	boolean flagGravar = false;
	

	public HistoricoControl getHistoricoControl() {
		if (historicoControl == null) {
			historicoControl = new HistoricoControl();
		}
		return historicoControl;
	}

	public Historico getHistorico() {
		if (historico == null) {
			historico = new Historico();
		}
		return historico;
	}

	@ManagedProperty(value = "#{userLogin}")
	private UserLoginMB userLoginMB;

	public UserLoginMB getUserLoginMB() {
		return userLoginMB;
	}

	public void setUserLoginMB(UserLoginMB userLoginMB) {
		this.userLoginMB = userLoginMB;
	}

	public User getUser() {

		if (user == null) {
			String userEmail = userLoginMB.getEmail();
			user = userFacade.findUserByEmail(userEmail);
		}
		return user;
	}

	public void setHistorico(Historico historico) {
		this.historico = historico;
	}

	public void setHistoricoControl(HistoricoControl historicoControl) {
		this.historicoControl = historicoControl;
	}

	public List<Historico> getAllHistoricos() {
		return historicoFacade.findAll();
	}

	public List<HistoricoControl> getAllHistoricoControl() {
		return historicoControlFacade.findAll();
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void handleFileUpload(FileUploadEvent event) {

		file = event.getFile();
		
		try {

			DataInputStream ds = new DataInputStream(file.getInputstream());
			Scanner scanner = new Scanner(ds);
			user = getUser();
			
			while (scanner.hasNext()) {

				String readLine = scanner.nextLine();
				String nOARQU = readLine.substring(2, 15);
				String nUSERCONTROL = user.getEmail();


				if (readLine.trim().length() <= 32) {					
					
					if(doEfetuarConsulta(nOARQU, nUSERCONTROL) == "gravar"){
						
						flagGravar=true;
						
						int tIPREG = Integer.parseInt(readLine.substring(0, 2).trim());
						String nOARQUI = readLine.substring(2, 15).trim();
						String cODORI = readLine.substring(15, 23).trim();
						String dATGER = readLine.substring(23, 31).trim();
						
						getHistoricoControl().setNome_user_Control(user.getEmail());
						getHistoricoControl().setTipreg(tIPREG);
						getHistoricoControl().setNome_Arquivo(nOARQUI);
						getHistoricoControl().setCod_Origem(cODORI);
						getHistoricoControl().setData_Geracao(dATGER);						
						
					}

					historicoControlFacade.update(historicoControl);

				}

				
				if(flagGravar==true){
					
					if (readLine.trim().length() >= 43) {

						Calendar dATAPR = new GregorianCalendar(getTimeZone());

						int ano = Integer.parseInt(readLine.substring(2, 6));
						int mes = Integer.parseInt(readLine.substring(6, 8));
						int dia = Integer.parseInt(readLine.substring(8, 10));

						dATAPR.set(Calendar.YEAR, ano);
						dATAPR.set(Calendar.MONTH, mes);
						dATAPR.set(Calendar.DAY_OF_MONTH, dia);

						Date data_pregao = dATAPR.getTime();

						String cODNEG = readLine.substring(12, 24);
						String nOME = readLine.substring(27, 39);
						Float pREABE = Float.parseFloat(readLine.substring(56, 69));
						Float pREMAX = Float.parseFloat(readLine.substring(69, 82));
						Float pREMIN = Float.parseFloat(readLine.substring(82, 95));
						Float pREMED = Float.parseFloat(readLine.substring(95, 108));
						Float pREOFC = Float.parseFloat(readLine.substring(121, 134));
						Float pREOFV = Float.parseFloat(readLine.substring(134, 147));
						Float qUATOT = Float.parseFloat(readLine.substring(152, 170));
						Float vOLTOT = Float.parseFloat(readLine.substring(170, 188));

						getHistorico().setData_pregao(data_pregao);
						getHistorico().setCod_negociacao(cODNEG);
						getHistorico().setNome_empresa(nOME);
						getHistorico().setPreco_abertura(pREABE);
						getHistorico().setPreco_maximo(pREMAX);
						getHistorico().setPreco_minimo(pREMIN);
						getHistorico().setPreco_medio(pREMED);
						getHistorico().setPreco_melhor_oferta_compra(pREOFC);
						getHistorico().setPreco_melhor_oferta_venda(pREOFV);
						getHistorico().setQuantidade_negociada(qUATOT);
						getHistorico().setVolume_total(vOLTOT);
						getHistorico().setNome_user_Historico(nUSERCONTROL);
						
						historicoFacade.update(historico);

					}

					if (readLine.trim().length() > 32 && readLine.trim().length() <= 43) {
						sendInfoMessageToUpload("Arquivo gravado com sucesso");
						flagGravar=false;						
					}				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	
	
	
	
	public String doEfetuarConsulta(String nome_Arquivo, String nome_user_Control) {
		try {
			historicoControl = historicoControlFacade.findAllControle(nome_Arquivo);		
			
			if (nome_Arquivo.equals(historicoControl.getNome_Arquivo()) && nome_user_Control.equals(historicoControl.getNome_user_Control())) {				
				sendInfoMessageToUpload("Hist—rico j‡ existe para o usuario " + user.getName());
			}
		} catch (NullPointerException e) {
			sendInfoMessageToUpload("Hist—rico ser‡ gravado para o usu‡rio " + user.getName());
			nome_Arquivo = "gravar";
			return nome_Arquivo;
		}			
		return nome_Arquivo;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getDefault();
	}

	// ---------------------------------------//

	private void sendInfoMessageToUpload(String message) {
		FacesContext context = getContext();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				message, message));
	}


	private FacesContext getContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println(context);
		return context;
	}

}
