package trafegoEspacial.view.bean;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import trafegoEspacial.servico.ProcessadorMensagem;

@Component
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class BeanBreadcrumb {

	@Autowired
	private ProcessadorMensagem processadorBreadcrumb;

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	public String buscaNomeBreadcrumb(String chaveAtual) {
		Map<String, Object> map = new HashMap<>();
		map.put("chave", chaveAtual);
		String chaveLinkName = processadorBreadcrumb
				.resolveExpressaoChave(ProcessadorMensagem.CHAVE_BREADCRUMBLINK_NOME, map);
		String linkName = processadorBreadcrumb.resolveChave(chaveLinkName);
		return linkName;
	}

	public String buscaViewBreadcrumb(String chaveAtual) {
		Map<String, Object> map = new HashMap<>();
		map.put("chave", chaveAtual);
		String chaveLinkView = processadorBreadcrumb
				.resolveExpressaoChave(ProcessadorMensagem.CHAVE_BREADCRUMBLINK_VIEW, map);
		String linkView = processadorBreadcrumb.resolveChave(chaveLinkView);
		return linkView;
	}

	public ProcessadorMensagem getProcessadorBreadcrumb() {
		return processadorBreadcrumb;
	}

	public void setProcessadorBreadcrumb(ProcessadorMensagem processadorBreadcrumb) {
		this.processadorBreadcrumb = processadorBreadcrumb;
	}

}