package trafegoEspacial.servico.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.ProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.TabelaSwapi;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;
import trafegoEspacial.entidade.view.EntidadeFiltroDjango;
import trafegoEspacial.view.componente.DatamodelEntidade;

@Component
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class ServicoBuscaDadosSwapi {

	@Autowired
	private MessageSource mensagens;

	@Autowired
	private ServicoSwabi servicoSwabi;

	private Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	public void pesquisar(TabelaSwapi tabela) {
		DatamodelEntidade datamodelEntidade = new DatamodelEntidade(tabela);
		tabela.setDatamodelEntidade(datamodelEntidade);
		datamodelEntidade.init(tabela.getFiltro(), servicoSwabi.createEntidadeDadosServicoSwapi());
		tabela.limpaDetalhes();
	}

	public Map<String, InterfaceEntidade> load(TabelaSwapi tabela, EntidadeDadosServicoSwapi dadosBusca,
			EntidadeFiltroDjango filtro) throws IOException {
		String mensagem = mensagens.getMessage(ServicoSwabi.CHAVE_SERVICE_PROBLEMADESCONHECIDO, new Object[0], null);
		Map<String, InterfaceEntidade> retorno = new HashMap<>();
		Map<String, String> jsonMap = tabela.getConversor().montaMapa(filtro);
		dadosBusca.getQueryParameters().putAll(jsonMap);
		try {
			InterfaceEntidade[] dados = tabela.getBuscadorDados().buscaDados(dadosBusca);
			List<InterfaceEntidade> lista = tabela.verificaDados(new ArrayList<>(Arrays.asList(dados)));
			for (InterfaceEntidade entidade : lista) {
				retorno.put(entidade.getChaveEntidade(), entidade);
			}
		} catch (ProcessingException ex) {
			getLogger().warn(mensagem, ex);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} catch (Exception ex) {
			getLogger().warn(ex.getMessage(), ex);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
		}
		return retorno;
	}

	public MessageSource getMensagens() {
		return mensagens;
	}

	public void setMensagens(MessageSource mensagens) {
		this.mensagens = mensagens;
	}

	public ServicoSwabi getServicoSwabi() {
		return servicoSwabi;
	}

	public void setServicoSwabi(ServicoSwabi servicoSwabi) {
		this.servicoSwabi = servicoSwabi;
	}

}
