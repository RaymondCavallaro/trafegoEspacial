package trafegoEspacial.view.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.ws.rs.ProcessingException;

import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import trafegoEspacial.comportamento.InterfaceBuscadorDados;
import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.InterfaceViewBean;
import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;
import trafegoEspacial.entidade.view.EntidadeFiltroDjango;
import trafegoEspacial.servico.ConversorNaveSwapi;
import trafegoEspacial.servico.ServicoSwabi;
import trafegoEspacial.view.componente.DatamodelEntidade;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BeanNave implements InterfaceViewBean {

	private final String chave = "naves";

	@Autowired
	private BeanSelecionados beanSelecionados;

	@Autowired
	private BeanBreadcrumb beanBreadcrumb;

	@Autowired
	private ServicoSwabi servicoSwabi;

	@Autowired
	private ConversorNaveSwapi conversorNave;

	@Autowired
	private MessageSource mensagens;

	private EntidadeFiltroDjango filtro;

	private DatamodelEntidade datamodelNave;

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@PostConstruct
	public void postConstruct() {
		init();
	}

	private void init() {
		filtro = new EntidadeFiltroDjango();
		pesquisar();
	}

	public void pesquisar() {
		datamodelNave = new DatamodelEntidade((InterfaceBuscadorDados) new InterfaceBuscadorDados() {

			@Override
			public Map<String, InterfaceEntidade> buscaDados(DatamodelEntidade model, int first, int pageSize,
					String sortField, SortOrder sortOrder, Map<String, Object> filters) throws Exception {
				return BeanNave.this.load(model.getDadosBuscaNave(), model.getFiltro());
			}
		});
		datamodelNave.init(filtro, servicoSwabi.createEntidadeDadosServicoSwapi());
		limpaDetalhes();
	}

	private void limpaDetalhes() {
	}

	public Map<String, InterfaceEntidade> load(EntidadeDadosServicoSwapi dadosBusca, EntidadeFiltroDjango filtroNave)
			throws IOException {
		String mensagem = mensagens.getMessage(ServicoSwabi.CHAVE_SERVICENAVES_PROBLEMADESCONHECIDO, new Object[0],
				null);
		Map<String, InterfaceEntidade> retorno = new HashMap<>();
		Map<String, String> jsonMap = conversorNave.montaMapa(filtroNave);
		dadosBusca.getQueryParameters().putAll(jsonMap);
		try {
			InterfaceEntidade[] naves = servicoSwabi.buscaNavesPagina(dadosBusca);
			List<InterfaceEntidade> lista = verificaNaves(new ArrayList<>(Arrays.asList(naves)));
			for (InterfaceEntidade nave : lista) {
				retorno.put(nave.getChaveEntidade(), nave);
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

	private List<InterfaceEntidade> verificaNaves(List<InterfaceEntidade> naves) {
		return naves;
	}

	public void selecionaNave(EntidadeNave nave) {
		beanSelecionados.getFiltroSelecionados().setNave(nave);
	}

	public DatamodelEntidade getDatamodelNave() {
		return datamodelNave;
	}

	public void setDatamodelNave(DatamodelEntidade datamodelNave) {
		this.datamodelNave = datamodelNave;
	}

	public ServicoSwabi getServicoSwabi() {
		return servicoSwabi;
	}

	public void setServicoSwabi(ServicoSwabi servicoSwabi) {
		this.servicoSwabi = servicoSwabi;
	}

	public ConversorNaveSwapi getConversorNave() {
		return conversorNave;
	}

	public void setConversorNave(ConversorNaveSwapi conversorNave) {
		this.conversorNave = conversorNave;
	}

	public MessageSource getMensagens() {
		return mensagens;
	}

	public void setMensagens(MessageSource mensagens) {
		this.mensagens = mensagens;
	}

	public BeanBreadcrumb getBeanBreadcrumb() {
		return beanBreadcrumb;
	}

	public void setBeanBreadcrumb(BeanBreadcrumb beanBreadcrumb) {
		this.beanBreadcrumb = beanBreadcrumb;
	}

	public String getChave() {
		return chave;
	}

	public BeanSelecionados getBeanSelecionados() {
		return beanSelecionados;
	}

	public void setBeanSelecionados(BeanSelecionados beanSelecionados) {
		this.beanSelecionados = beanSelecionados;
	}

	public EntidadeFiltroDjango getFiltro() {
		return filtro;
	}

	public void setFiltro(EntidadeFiltroDjango filtro) {
		this.filtro = filtro;
	}

}