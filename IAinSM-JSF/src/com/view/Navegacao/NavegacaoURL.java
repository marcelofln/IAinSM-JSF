package com.view.Navegacao;

import javax.faces.bean.SessionScoped;



@SessionScoped
public enum NavegacaoURL {
	
	INICIO(			"inicio"		,"/index"),
	MENU(			"conteudo"		,"/paginas/contentMenu"),
	CADASTRA(		"cadastra"		,"/paginas/cadastraUsuario"),
	RELATORIOS(		"relatorios"	,"/paginas/elementChart"),
	ATIVOS(			"ativos"		,"/paginas/elementAcoes"),
	CONFIGURACAO(	"configuracao"	,"/paginas/elementConfiguracao"),
	REGRAS(			"regras"		,"/paginas/elementRegras"),
	TIMER(			"timer"			,"/paginas/elementTimer"),
	CREATE_TIMER(	"timerCreate"	,"/paginas/timerCreate"),
	DELETE_TIMER(	"timerDelete"	,"/paginas/timerDelete"),
	UPDATE_TIMER(	"timerUpdate" 	,"/paginas/timerUpdate"),
	UPLOAD_PAPEIS(	"uploadPapeis"	,"/paginas/elementUpload");
	
	private final String url;
	private final String id;
	
	NavegacaoURL(String id, String url){
		this.id = id;
		this.url = url;
	}
	
	public String getId(){
		return id;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUrl(String id) {
		for (NavegacaoURL navegacaoMB : NavegacaoURL.values()) {
			
			if(navegacaoMB.getId().equals(id)){
				return navegacaoMB.getUrl();
			}
		}
		return null;
	}
}
