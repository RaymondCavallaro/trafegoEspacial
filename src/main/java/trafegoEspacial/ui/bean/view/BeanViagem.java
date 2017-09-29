package trafegoEspacial.ui.bean.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import trafegoEspacial.comportamento.InterfaceViewBean;
import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.EntidadePlaneta;
import trafegoEspacial.entidade.EntidadeTripulante;
import trafegoEspacial.entidade.EntidadeViagem;
import trafegoEspacial.entidade.view.EntidadeFiltroViagem;
import trafegoEspacial.servico.bean.Armazenamento;
import trafegoEspacial.ui.bean.BeanSelecionados;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public final class BeanViagem implements InterfaceViewBean {

	private static final String CHAVE_VALIDACAO_NAOPOSSUINAVE = "entidade.viagem.novo.validacao.naoPossuiNave";
	private static final String CHAVE_VALIDACAO_NAOPOSSUIDESTINO = "entidade.viagem.novo.validacao.naoPossuiDestino";
	private static final String CHAVE_VALIDACAO_PARTIDAIGUALDESTINO = "entidade.viagem.novo.validacao.partidaIgualDestino";
	private static final String CHAVE_VALIDACAO_TRIPULANTEEXISTENTE = "entidade.viagem.propriedade.tripulantes.adicionar.validacao.tripulanteExistente";
	public static final String CHAVE_VALIDACAO_LOTADO = "entidade.viagem.propriedade.tripulantes.adicionar.validacao.lotado";

	private static final List<String> VIAGEM_STATUS = new ArrayList<>(
			Arrays.asList(EntidadeViagem.VIAGEM_STATUS_FINALIZADA, EntidadeViagem.VIAGEM_STATUS_EM_CURSO,
					EntidadeViagem.VIAGEM_STATUS_FUTURO, EntidadeViagem.VIAGEM_STATUS_AGUARDANDOTRIPULACAO));

	private static final String CHAVE_VIEW = "viagens";

	@Autowired
	private Armazenamento armazenamento;

	@Autowired
	private BeanSelecionados beanSelecionados;

	@Autowired
	private MessageSource mensagens;

	private EntidadeFiltroViagem filtroViagem;

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@PostConstruct
	public void postConstruct() {
		init();
	}

	protected void init() {
		filtroViagem = new EntidadeFiltroViagem();
		filtroViagem.setStatus(getViagemStatus());
		pesquisar();
	}

	public List<EntidadeViagem> pesquisar() {
		try {
			Collection<EntidadeViagem> colecaoFiltrada = null;
			if (filtroViagem.getFiltraNave() && beanSelecionados.getFiltroSelecionados().getNave() != null) {
				EntidadeNave nave = armazenamento.atualizaNave(beanSelecionados.getFiltroSelecionados().getNave());
				colecaoFiltrada = nave.getViagens();
			}
			if (colecaoFiltrada == null) {
				if (filtroViagem.getFiltraTripulante()
						&& beanSelecionados.getFiltroSelecionados().getTripulante() != null) {
					EntidadeTripulante tripulante = armazenamento
							.atualizaTripulacao(beanSelecionados.getFiltroSelecionados().getTripulante());
					colecaoFiltrada = tripulante.getViagens();
				} else {
					colecaoFiltrada = armazenamento.getMapaViagem().values();
				}
			} else if (filtroViagem.getFiltraTripulante()
					&& beanSelecionados.getFiltroSelecionados().getTripulante() != null) {
				colecaoFiltrada = armazenamento.filtraViagens(colecaoFiltrada,
						Armazenamento.CHAVE_ARMAZENAMENTOMAPA_FILTROCONJUNTO, "tripulantes", "chave",
						beanSelecionados.getFiltroSelecionados().getTripulante().getUrl());
			}
			if (filtroViagem.getFiltraPlaneta() && beanSelecionados.getFiltroSelecionados().getPlaneta() != null) {
				colecaoFiltrada = armazenamento.filtraViagens(colecaoFiltrada,
						Armazenamento.CHAVE_ARMAZENAMENTOMAPA_FILTROVALOR, "destino.url", "chave",
						beanSelecionados.getFiltroSelecionados().getPlaneta().getUrl());
			}
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(filtroViagem.getStatus());
			return armazenamento.filtraViagens(colecaoFiltrada, Armazenamento.CHAVE_ARMAZENAMENTOMAPA_FILTROIN,
					"status", "chave", json);
		} catch (

		JsonProcessingException e) {
			getLogger().info(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	public void adiciona(EntidadePlaneta planeta) {
		beanSelecionados.getFiltroSelecionados().setPlaneta(planeta);
		adiciona((String) null);
	}

	private void adiciona(String clientId) {
		if (beanSelecionados.getFiltroSelecionados().getNave() == null) {
			String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_NAOPOSSUINAVE, new Object[0], null);
			FacesContext.getCurrentInstance().addMessage(clientId,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} else {
			if (beanSelecionados.getFiltroSelecionados().getPlaneta() == null) {
				String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_NAOPOSSUIDESTINO, new Object[0], null);
				FacesContext.getCurrentInstance().addMessage(clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
			} else {
				List<EntidadeViagem> viagensNave = beanSelecionados.getFiltroSelecionados().getNave().getViagens();
				boolean adicionar = false;
				if (viagensNave.isEmpty()) {
					adicionar = true;
				} else {
					EntidadePlaneta planetaPartida = viagensNave.get(viagensNave.size() - 1).getDestino();
					if (beanSelecionados.getFiltroSelecionados().getPlaneta().equals(planetaPartida)) {
						String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_PARTIDAIGUALDESTINO, new Object[0],
								null);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
					} else {
						adicionar = true;
					}
				}
				if (adicionar) {
					armazenamento.adicionaViagem(beanSelecionados.getFiltroSelecionados().getNave(),
							beanSelecionados.getFiltroSelecionados().getPlaneta());
				}
			}
		}

	}

	public void novo() {
		adiciona("btnNovo");
	}

	public void adicionarTripulante(EntidadeViagem viagem) {
		viagem = armazenamento.atualizaViagem(viagem);
		EntidadeTripulante tripulante = armazenamento
				.atualizaTripulacao(beanSelecionados.getFiltroSelecionados().getTripulante());
		if (viagem.getTripulantes().contains(tripulante)) {
			String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_TRIPULANTEEXISTENTE, new Object[0], null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} else if (viagem.getNave().getPassageiros() > viagem.getTripulantes().size()) {
			viagem.getTripulantes().add(tripulante);
			tripulante.getViagens().add(viagem);
		} else {
			String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_LOTADO, new Object[0], null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		}
	}

	public void finalizar(EntidadeViagem viagem) {
		int index = viagem.getNave().getViagens().indexOf(viagem);
		if (EntidadeViagem.VIAGEM_STATUS_EM_CURSO.equals(viagem.getStatus())) {
			viagem.setStatus(EntidadeViagem.VIAGEM_STATUS_FINALIZADA);
		} else {
			try {
				List<EntidadeViagem> resultado = armazenamento.filtraViagens(viagem.getNave().getViagens(),
						Armazenamento.CHAVE_ARMAZENAMENTOMAPA_FILTROVALOR, "status", "chave",
						EntidadeViagem.VIAGEM_STATUS_EM_CURSO);
				assert (resultado != null);
				assert (resultado.size() == 1);
				int indexCurso = viagem.getNave().getViagens().indexOf(resultado.get(0));
				for (int i = indexCurso; i <= index; i++) {
					viagem.getNave().getViagens().get(i).setStatus(EntidadeViagem.VIAGEM_STATUS_FINALIZADA);
				}
			} catch (JsonProcessingException e) {
				getLogger().warn(e.getMessage(), e);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			}
		}
		if (index > 0) {

		}
	}

	public void selecionaNave(EntidadeNave nave) {
		beanSelecionados.getFiltroSelecionados().setNave(nave);
		init();
		filtroViagem.setFiltraNave(true);
	}

	public void selecionaViagem(EntidadeViagem viagem) {
		beanSelecionados.getFiltroSelecionados().setViagem(viagem);
	}

	public List<String> getViagemStatus() {
		return VIAGEM_STATUS;
	}

	public EntidadeFiltroViagem getFiltroViagem() {
		return filtroViagem;
	}

	public void setFiltroViagem(EntidadeFiltroViagem filtroViagem) {
		this.filtroViagem = filtroViagem;
	}

	public Armazenamento getArmazenamento() {
		return armazenamento;
	}

	public void setArmazenamento(Armazenamento armazenamento) {
		this.armazenamento = armazenamento;
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

}