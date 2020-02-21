package com.view.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.view.Navegacao.NavegacaoURL;


@ManagedBean
@RequestScoped
public class NavegacaoMB {

	public NavegacaoMB() {
	}

	public String getUrl(String id) {
		for (NavegacaoURL navegacaoEnum : NavegacaoURL.values()) {
			if (navegacaoEnum.getId().equals(id)) {
				return navegacaoEnum.getUrl();
			}
		}
		return null;
	}
}
