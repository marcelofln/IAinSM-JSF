package com.util;


	import java.util.Iterator;

	import javax.faces.application.FacesMessage;
	import javax.faces.bean.RequestScoped;
	import javax.faces.component.EditableValueHolder;
	import javax.faces.component.UIComponent;
	import javax.faces.context.FacesContext;


	@RequestScoped
	public class FacesUtils {

		private final FacesContext facesContext;

		
		public FacesUtils(FacesContext facesContext) {
			this.facesContext = facesContext;
		}

		public void adicionaMensagemDeErro(String msg) {
			facesContext.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}

		public void adicionaMensagemDeInformacao(String msg) {
			facesContext.addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
		}

		public boolean possuiMensagem(String msg) {
			Iterator<FacesMessage> messages = facesContext.getMessages();
			while (messages.hasNext()) {
				FacesMessage message = messages.next();
				boolean confere = message.getDetail().equals(msg);
				if (confere)
					return true;
			}
			return false;
		}

		/**
		 * Limpa os dados dos componentes de edi��o e de seus filhos,
		 * recursivamente. Checa se o componente � inst�ncia de EditableValueHolder
		 * e 'reseta' suas propriedades.
		 * <p>
		 * Quando este m�todo, por algum motivo, n�o funcionar, parta para ignor�ncia
		 * e limpe o componente assim:
		 * <p><blockquote><pre>
		 * 	component.getChildren().clear()
		 * </pre></blockquote>
		 * :-)
		 */
		public void cleanSubmittedValues(UIComponent component) {
			if (component instanceof EditableValueHolder) {
				EditableValueHolder evh = (EditableValueHolder) component;
				evh.setSubmittedValue(null);
				evh.setValue(null);
				evh.setLocalValueSet(false);
				evh.setValid(true);
			}
			if(component.getChildCount()>0){
				for (UIComponent child : component.getChildren()) {
					cleanSubmittedValues(child);
				}
			}
		}

	}

