package trafegoEspacial.view.bean;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import trafegoEspacial.comportamento.InterfaceViewBean;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public final class BeanTripulacao implements InterfaceViewBean {

	private final String chave = "tripulacoes";

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@PostConstruct
	public void postConstruct() {
		init();
	}

	protected void init() {
		pesquisar();
	}

	public List<EntidadeViagem> pesquisar() {
		try {
			return armazenamento.filtraViagens("status", filtroViagem.getStatus());
		} catch (JsonProcessingException e) {
			getLogger().info(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	public String getChave() {
		return chave;
	}

}