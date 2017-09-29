package trafegoEspacial.servico.bean;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import trafegoEspacial.entidade.view.EntidadeFiltroSelecionados;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BeanSelecionados {

	protected EntidadeFiltroSelecionados filtroSelecionados;

	@PostConstruct
	public void postConstruct() {
		init();
	}

	protected void init() {
		filtroSelecionados = new EntidadeFiltroSelecionados();
	}

	public EntidadeFiltroSelecionados getFiltroSelecionados() {
		return filtroSelecionados;
	}

	public void setFiltroSelecionados(EntidadeFiltroSelecionados filtroSelecionados) {
		this.filtroSelecionados = filtroSelecionados;
	}

}