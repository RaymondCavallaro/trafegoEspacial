package trafegoEspacial.servico.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.EntidadePlaneta;
import trafegoEspacial.entidade.EntidadeTripulante;
import trafegoEspacial.entidade.EntidadeViagem;
import trafegoEspacial.servico.ProcessadorMensagem;

@Component
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class Armazenamento {

	public static final String CHAVE_ARMAZENAMENTOMAPA_FILTROVALOR = "armazenamento.mapa.filtro.valor";
	public static final String CHAVE_ARMAZENAMENTOMAPA_FILTROIN = "armazenamento.mapa.filtro.in";
	public static final String CHAVE_ARMAZENAMENTOMAPA_FILTROCONJUNTO = "armazenamento.mapa.filtro.conjunto";

	@Autowired
	private ProcessadorMensagem processadorArmazenamento;

	@Resource(name = "mapaNave")
	private Map<String, EntidadeNave> mapaNave;

	@Resource(name = "mapaTripulante")
	private Map<String, EntidadeTripulante> mapaTripulante;

	@Resource(name = "mapaViagem")
	private Map<String, EntidadeViagem> mapaViagem;

	public List<EntidadeViagem> filtraViagens(String chaveFiltro, String campo, String valorJson)
			throws JsonProcessingException {
		return filtraViagens(mapaViagem.values(), chaveFiltro, campo, valorJson);
	}

	public List<EntidadeViagem> filtraViagens(Collection<EntidadeViagem> viagens, String chaveFiltro, String campo,
			String valorJson) throws JsonProcessingException {
		List<EntidadeViagem> filtrado = new ArrayList<>();
		if (!mapaViagem.isEmpty()) {
			Map<String, Object> dados = new HashMap<>();
			dados.put("campo", campo);
			dados.put("valor", valorJson);
			String expressao = processadorArmazenamento.resolveExpressaoChave(chaveFiltro, dados);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(viagens);
			List<String> idViagens = JsonPath.read(json, expressao);
			for (String idViagem : idViagens) {
				filtrado.add(mapaViagem.get(idViagem));
			}
		}
		return filtrado;
	}

	public EntidadeTripulante atualizaTripulacao(EntidadeTripulante tripulante) {
		synchronized (mapaTripulante) {
			if (!mapaTripulante.containsKey(tripulante.getChaveEntidade())) {
				mapaTripulante.put(tripulante.getChaveEntidade(), tripulante);
			}
		}
		return mapaTripulante.get(tripulante.getChaveEntidade());
	}

	public EntidadeViagem atualizaViagem(EntidadeViagem viagem) {
		synchronized (mapaViagem) {
			if (!mapaViagem.containsKey(viagem.getChave())) {
				mapaViagem.put(viagem.getChave(), viagem);
			}
		}
		return mapaViagem.get(viagem.getChave());
	}

	public EntidadeNave atualizaNave(EntidadeNave nave) {
		synchronized (mapaNave) {
			if (!mapaNave.containsKey(nave.getChaveEntidade())) {
				mapaNave.put(nave.getChaveEntidade(), nave);
			}
		}
		return mapaNave.get(nave.getChaveEntidade());
	}

	public EntidadeViagem adicionaViagem(EntidadeNave nave, EntidadePlaneta planeta) {
		EntidadeViagem viagem = new EntidadeViagem();
		nave = atualizaNave(nave);
		synchronized (mapaViagem) {
			String chave = null;
			while (chave == null) {
				chave = UUID.randomUUID().toString();
				if (mapaViagem.containsKey(chave)) {
					chave = null;
				}
			}
			viagem.setChave(chave);
			viagem.setDestino(planeta);
			// viagem.getOcupacao().getRegioes().get(EntidadeNave.class).getOcupantes().add(nave);
			viagem.setNave(nave);
			mapaViagem.put(chave, viagem);
			if (nave.getViagens().isEmpty()) {
				viagem.setStatus(EntidadeViagem.VIAGEM_STATUS_EM_CURSO);
			} else {
				viagem.setStatus(EntidadeViagem.VIAGEM_STATUS_FUTURO);
			}
			nave.getViagens().add(viagem);
		}
		return viagem;
	}

	public ProcessadorMensagem getProcessadorArmazenamento() {
		return processadorArmazenamento;
	}

	public void setProcessadorArmazenamento(ProcessadorMensagem processadorArmazenamento) {
		this.processadorArmazenamento = processadorArmazenamento;
	}

	public Map<String, EntidadeNave> getMapaNave() {
		return mapaNave;
	}

	public void setMapaNave(Map<String, EntidadeNave> mapaNave) {
		this.mapaNave = mapaNave;
	}

	public Map<String, EntidadeTripulante> getMapaTripulante() {
		return mapaTripulante;
	}

	public void setMapaTripulante(Map<String, EntidadeTripulante> mapaTripulante) {
		this.mapaTripulante = mapaTripulante;
	}

	public Map<String, EntidadeViagem> getMapaViagem() {
		return mapaViagem;
	}

	public void setMapaViagem(Map<String, EntidadeViagem> mapaViagem) {
		this.mapaViagem = mapaViagem;
	}

}
