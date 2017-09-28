package trafegoEspacial.view.bean;

import java.util.ArrayList;
import java.util.Arrays;
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
import trafegoEspacial.entidade.EntidadePlaneta;
import trafegoEspacial.entidade.EntidadeViagem;
import trafegoEspacial.entidade.view.EntidadeFiltroViagem;
import trafegoEspacial.servico.Armazenamento;
import trafegoEspacial.servico.ServicoSwabi;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public final class BeanViagem implements InterfaceViewBean {

	private static final String CHAVE_VALIDACAO_NAOPOSSUINAVE = "entidade.viagem.novo.validacao.naoPossuiNave";
	private static final String CHAVE_VALIDACAO_NAOPOSSUIDESTINO = "entidade.viagem.novo.validacao.naoPossuiDestino";
	private static final String CHAVE_VALIDACAO_PARTIDAIGUALDESTINO = "entidade.viagem.novo.validacao.partidaIgualDestino";

	private static final List<String> VIAGEM_STATUS = new ArrayList<>(
			Arrays.asList(EntidadeViagem.VIAGEM_STATUS_FINALIZADA, EntidadeViagem.VIAGEM_STATUS_EM_CURSO,
					EntidadeViagem.VIAGEM_STATUS_FUTURO));

	private final String chave = "viagens";

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
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(filtroViagem.getStatus());
			return armazenamento.filtraViagens("status", json);
		} catch (JsonProcessingException e) {
			getLogger().info(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	public void adiciona(EntidadePlaneta planeta) {
		beanSelecionados.getFiltroSelecionados().setPlaneta(planeta);
	}

	private void adiciona() {
		List<EntidadeViagem> viagensNave = beanSelecionados.getFiltroSelecionados().getNave().getViagens();
		boolean adicionar = false;
		if (viagensNave.isEmpty()) {
			adicionar = true;
		} else {
			EntidadePlaneta planetaPartida = viagensNave.get(viagensNave.size() - 1).getDestino();
			if (!beanSelecionados.getFiltroSelecionados().getPlaneta().equals(planetaPartida)) {
				String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_PARTIDAIGUALDESTINO, new Object[0], null);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
				adicionar = true;
			}
		}
		if (adicionar) {
			armazenamento.adicionaViagem(beanSelecionados.getFiltroSelecionados().getNave(),
					beanSelecionados.getFiltroSelecionados().getPlaneta());
		}
	}

	public void novo() {
		if (beanSelecionados.getFiltroSelecionados().getNave() == null) {
			String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_NAOPOSSUINAVE, new Object[0], null);
			FacesContext.getCurrentInstance().addMessage("btnNovo",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
		} else {
			if (beanSelecionados.getFiltroSelecionados().getPlaneta() == null) {
				String mensagem = mensagens.getMessage(CHAVE_VALIDACAO_NAOPOSSUIDESTINO, new Object[0], null);
				FacesContext.getCurrentInstance().addMessage("btnNovo",
						new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, null));
			} else {
				adiciona();
			}
		}
	}

	public void finalizar(EntidadeViagem viagem) {
		int index = viagem.getNave().getViagens().indexOf(viagem);
		if (EntidadeViagem.VIAGEM_STATUS_EM_CURSO.equals(viagem.getStatus())) {
			viagem.setStatus(EntidadeViagem.VIAGEM_STATUS_FINALIZADA);
		} else {
			// viagem futura
		}
		if (index > 0) {

		}
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
		return chave;
	}

	public BeanSelecionados getBeanSelecionados() {
		return beanSelecionados;
	}

	public void setBeanSelecionados(BeanSelecionados beanSelecionados) {
		this.beanSelecionados = beanSelecionados;
	}

}