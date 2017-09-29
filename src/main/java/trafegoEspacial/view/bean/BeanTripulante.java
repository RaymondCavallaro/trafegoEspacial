package trafegoEspacial.view.bean;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

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
import trafegoEspacial.comportamento.TabelaSwapi;
import trafegoEspacial.entidade.EntidadeTripulante;
import trafegoEspacial.entidade.EntidadeViagem;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;
import trafegoEspacial.entidade.view.EntidadeFiltroTripulante;
import trafegoEspacial.servico.bean.Armazenamento;
import trafegoEspacial.servico.bean.BeanSelecionados;
import trafegoEspacial.servico.bean.ConversorSwapi;
import trafegoEspacial.servico.bean.ServicoBuscaDadosSwapi;
import trafegoEspacial.servico.bean.ServicoSwabi;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public final class BeanTripulante implements InterfaceViewBean {

	private static final String CHAVE_VIEW = "tripulantes";

	private static final String CHAVE_VALIDACAO_NAOPOSSUIVIAGEM = "entidade.tripulante.adicionar.validacao.naoPossuiViagem";
	private static final String CHAVE_VALIDACAO_NAOPOSSUITRIPULANTES = "entidade.tripulante.adicionar.validacao.naoPossuiTripulantes";

	@Autowired
	private Armazenamento armazenamento;

	@Autowired
	private ServicoBuscaDadosSwapi servicoBuscaDadosSwapi;

	@Autowired
	private ServicoSwabi servicoSwabi;

	@Autowired
	private BeanSelecionados beanSelecionados;

	@Autowired
	private MessageSource mensagens;

	@Autowired
	private ConversorSwapi conversor;

	private EntidadeFiltroTripulante filtroTripulante;

	private TabelaSwapi tabela;

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@PostConstruct
	public void postConstruct() {
		init();
	}

	private void init() {
		filtroTripulante = new EntidadeFiltroTripulante();
		InterfaceBuscadorDados buscador = new InterfaceBuscadorDados() {

			@Override
			public InterfaceEntidade[] buscaDados(EntidadeDadosServicoSwapi dadosBusca) throws Exception {
				if (filtroTripulante.getFiltraViagem()
						&& beanSelecionados.getFiltroSelecionados().getViagem() != null) {
					EntidadeViagem viagem = armazenamento
							.atualizaViagem(beanSelecionados.getFiltroSelecionados().getViagem());
					return viagem.getTripulantes().toArray(new InterfaceEntidade[0]);
				} else {
					return servicoSwabi.buscaTripulantesPagina(dadosBusca);
				}
			}
		};
		tabela = new TabelaSwapi(servicoBuscaDadosSwapi, conversor, buscador);
		tabela.init();
	}

	public void adicionar() {
		if (beanSelecionados.getFiltroSelecionados().getViagem() == null) {
			String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_NAOPOSSUIVIAGEM, new Object[0], null);
			FacesContext.getCurrentInstance().addMessage("btnAdicionar",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} else if (tabela.getSelecao().isEmpty()) {
			String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_NAOPOSSUITRIPULANTES, new Object[0], null);
			FacesContext.getCurrentInstance().addMessage("btnAdicionar",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} else {
			EntidadeViagem viagem = armazenamento.atualizaViagem(beanSelecionados.getFiltroSelecionados().getViagem());
			if (viagem.getNave().getPassageiros() > (viagem.getTripulantes().size() + tabela.getSelecao().size())) {
				for (InterfaceEntidade entidade : tabela.getSelecao()) {
					EntidadeTripulante tripulante = armazenamento.atualizaTripulacao((EntidadeTripulante) entidade);
					if (!viagem.getTripulantes().contains(tripulante)) {
						viagem.getTripulantes().add(tripulante);
						tripulante.getViagens().add(viagem);
					}
				}
			} else {
				String mensagem = mensagens.getMessage(BeanViagem.CHAVE_VALIDACAO_LOTADO, new Object[0], null);
				FacesContext.getCurrentInstance().addMessage("btnAdicionar",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
			}
		}
	}

	public void selecionaViagem(EntidadeViagem viagem) {
		beanSelecionados.getFiltroSelecionados().setViagem(viagem);
		init();
		filtroTripulante.setFiltraViagem(true);
	}

	public void selecionaTripulante(EntidadeTripulante tripulante) {
		beanSelecionados.getFiltroSelecionados().setTripulante(tripulante);
	}

	public String getChave() {
		return CHAVE_VIEW;
	}

	public BeanSelecionados getBeanSelecionados() {
		return beanSelecionados;
	}

	public void setBeanSelecionados(BeanSelecionados beanSelecionados) {
		this.beanSelecionados = beanSelecionados;
	}

	public ServicoBuscaDadosSwapi getServicoBuscaDadosSwapi() {
		return servicoBuscaDadosSwapi;
	}

	public void setServicoBuscaDadosSwapi(ServicoBuscaDadosSwapi servicoBuscaDadosSwapi) {
		this.servicoBuscaDadosSwapi = servicoBuscaDadosSwapi;
	}

	public ServicoSwabi getServicoSwabi() {
		return servicoSwabi;
	}

	public void setServicoSwabi(ServicoSwabi servicoSwabi) {
		this.servicoSwabi = servicoSwabi;
	}

	public EntidadeFiltroTripulante getFiltroTripulante() {
		return filtroTripulante;
	}

	public void setFiltroTripulante(EntidadeFiltroTripulante filtroTripulante) {
		this.filtroTripulante = filtroTripulante;
	}

	public TabelaSwapi getTabela() {
		return tabela;
	}

	public void setTabela(TabelaSwapi tabela) {
		this.tabela = tabela;
	}

	public ConversorSwapi getConversor() {
		return conversor;
	}

	public void setConversor(ConversorSwapi conversor) {
		this.conversor = conversor;
	}

	public Armazenamento getArmazenamento() {
		return armazenamento;
	}

	public void setArmazenamento(Armazenamento armazenamento) {
		this.armazenamento = armazenamento;
	}

}