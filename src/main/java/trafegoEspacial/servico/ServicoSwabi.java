package trafegoEspacial.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.EntidadePlaneta;
import trafegoEspacial.entidade.EntidadeTripulante;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;

//import org.apache.http.HttpStatus;

@Component
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class ServicoSwabi {

	private static final String CHAVE_RESTTARGET = "rest.swapi.host";
	private static final String CHAVE_SERVICONAVES = "rest.swapi.naves";
	private static final String CHAVE_SERVICOPLANETAS = "rest.swapi.planetas";
	private static final String CHAVE_SERVICOTRIPULANTES = "rest.swapi.tripulantes";

	private static final String CHAVE_SERVICORETORNO = "django.retorno";

	public static final String CHAVE_SERVICENAVES_PROBLEMADESCONHECIDO = "rest.swapi.naves.chamada.problema.desconhecido";

	@Autowired
	private ProcessadorMensagem processadorMensagemSwapi;

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	private String getWebTargetUrlString() {
		return processadorMensagemSwapi.resolveChave(CHAVE_RESTTARGET);
	}

	private Client getClient() {
		Client client = ClientBuilder.newClient();
		return client;
	}

	private String resolveChave(EntidadeDadosServicoSwapi dados, String chave) {
		Map<String, Object> map = new HashMap<>();
		return processadorMensagemSwapi.resolveExpressaoChave(chave, map);
	}

	private void processaPaginacaoDjango(EntidadeDadosServicoSwapi dados, Map<Object, Object> mapResponse) {
		if (mapResponse.get("count") == null) {
			dados.setTotalRegistros(null);
		} else {
			dados.setTotalRegistros(Integer.parseInt(mapResponse.get("count").toString()));
		}
		dados.setUltimaPagina(null);
	}

	private Response chamaServico(Client client, EntidadeDadosServicoSwapi dados) {
		if (dados.getPath() == null) {
			return null;
		}
		WebTarget target = client.target(dados.getUrlString()).path(dados.getPath());
		configParameterPagina(dados);
		if (dados.getQueryParameters() != null) {
			for (Entry<String, String> entry : dados.getQueryParameters().entrySet()) {
				target = target.queryParam(entry.getKey(), entry.getValue());
			}
		}
		dados.setPathUri(target.getUri().toString());
		Builder builder = target.request(MediaType.APPLICATION_JSON);
		if (dados.getHeaders() != null) {
			for (Entry<String, String> entry : dados.getHeaders().entrySet()) {
				builder.header(entry.getKey(), entry.getValue());
			}
		}
		Invocation invocation;
		if (dados.getPayload() != null) {
			invocation = builder.build(dados.getMethod(), dados.getPayload());
		} else {
			invocation = builder.build(dados.getMethod());
		}
		Response response = invocation.invoke();

		dados.setStatus(response.getStatus());
		// if (new Integer(HttpStatus.SC_NOT_FOUND).equals(dados.getStatus())) {
		// return null;
		// }
		return response;
	}

	private <T> T chamaServicoEntity(Client client, EntidadeDadosServicoSwapi dados) {
		Response response = chamaServico(client, dados);
		if (isOk(dados)) {
			return transformaEntity(response, (Class<T>) dados.getReturnClass());
		}
		return null;
	}

	private <T> T transformaEntity(Response response, Class<T> clazz) {
		return response.readEntity(clazz);
	}

	private <T> T chamaServicoDjangoPaginado(EntidadeDadosServicoSwapi dados) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		Client client = getClient();
		try {
			Response response = chamaServico(client, dados);
			if (isOk(dados)) {
				Map<Object, Object> mapRetorno = transformaEntity(response, Map.class);
				processaPaginacaoDjango(dados, mapRetorno);
				String retorno = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapRetorno);
				Object object = JsonPath.read(retorno, dados.getExpressao());
				String retornoDesejado = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
				return (T) mapper.readValue(retornoDesejado, (Class<T>) dados.getReturnClass());
			}
			throw new Exception("formato inválido");
		} finally {
			client.close();
		}
	}

	private void configParameterPagina(EntidadeDadosServicoSwapi dados) {
		Map<String, String> queryParameters;
		if (dados.getQueryParameters() == null) {
			queryParameters = new HashMap<>();
		} else {
			queryParameters = dados.getQueryParameters();
		}
		if (dados.getPaginaAtual() != null) {
			if (dados.getPaginaAtual() == 0) {
				dados.setPaginaAtual(1);
			}
			queryParameters.put("page", dados.getPaginaAtual().toString());
		}
		if (dados.getResultadosPorPagina() != null) {
			// digesto
			queryParameters.put("per_page", dados.getResultadosPorPagina());
			// restheart
			queryParameters.put("pagesize", dados.getResultadosPorPagina());
		}
		if (!queryParameters.isEmpty()) {
			dados.setQueryParameters(queryParameters);
		}
	}

	public <T> T chamaServico(EntidadeDadosServicoSwapi dados) {
		Client client = getClient();
		try {
			return chamaServicoEntity(client, dados);
		} finally {
			client.close();
		}
	}

	public EntidadeDadosServicoSwapi createEntidadeDadosServicoSwapi() {
		EntidadeDadosServicoSwapi dados = new EntidadeDadosServicoSwapi();
		initDadosServicoSwapi(dados);
		return dados;
	}

	public void initDadosServicoSwapi(EntidadeDadosServicoSwapi dados) {
		dados.setQueryParameters(new HashMap<String, String>());
		dados.setHeaders(new HashMap<String, String>());
		dados.setUrlString(getWebTargetUrlString());
		dados.setPaginaAtual(0);
		dados.setUltimaPagina(1);
	}

	public synchronized <T> List<T> chamaServicoPagina(EntidadeDadosServicoSwapi dados) {
		dados.setPaginaAtual(1);
		List<T> resposta = new ArrayList<>();

		Client client = getClient();
		try {
			resposta.add((T) chamaServicoEntity(client, dados));
		} finally {
			client.close();
		}

		Integer ultimaPagina = dados.getUltimaPagina();
		if (ultimaPagina != 1) {
			for (int i = 2; i <= ultimaPagina; i++) {
				dados.setPaginaAtual(new Integer(i));
				client = getClient();
				try {
					resposta.add((T) chamaServicoEntity(client, dados));
				} finally {
					client.close();
				}
			}
		}
		return resposta;
	}

	public void configDefaultGet(EntidadeDadosServicoSwapi dados) {
		dados.setHeaders(new HashMap<String, String>());
		dados.setMethod(HttpMethod.GET);
		dados.setPayload(null);
		dados.setReturnClass(String.class);
	}

	public void configDados(EntidadeDadosServicoSwapi dados) {
	}

	public void configBuscaNavesPagina(EntidadeDadosServicoSwapi dados) {
		configDefaultGet(dados);
		dados.setPath(resolveChave(dados, CHAVE_SERVICONAVES));
		dados.setExpressao(resolveChave(dados, CHAVE_SERVICORETORNO));
		dados.setReturnClass(EntidadeNave[].class);
	}

	public EntidadeNave[] buscaNavesPagina(EntidadeDadosServicoSwapi dados) throws Exception {
		configDados(dados);
		configBuscaNavesPagina(dados);
		EntidadeNave[] naves = chamaServicoDjangoPaginado(dados);
		return naves;
	}

	public void configBuscaPlanetasPagina(EntidadeDadosServicoSwapi dados) {
		configDefaultGet(dados);
		dados.setPath(resolveChave(dados, CHAVE_SERVICOPLANETAS));
		dados.setExpressao(resolveChave(dados, CHAVE_SERVICORETORNO));
		dados.setReturnClass(EntidadePlaneta[].class);
	}

	public EntidadePlaneta[] buscaPlanetasPagina(EntidadeDadosServicoSwapi dados) throws Exception {
		configDados(dados);
		configBuscaPlanetasPagina(dados);
		EntidadePlaneta[] retorno = chamaServicoDjangoPaginado(dados);
		return retorno;
	}

	public void configBuscaTripulantesPagina(EntidadeDadosServicoSwapi dados) {
		configDefaultGet(dados);
		dados.setPath(resolveChave(dados, CHAVE_SERVICOTRIPULANTES));
		dados.setExpressao(resolveChave(dados, CHAVE_SERVICORETORNO));
		dados.setReturnClass(EntidadeTripulante[].class);
	}

	public EntidadeTripulante[] buscaTripulantesPagina(EntidadeDadosServicoSwapi dados) throws Exception {
		configDados(dados);
		configBuscaTripulantesPagina(dados);
		EntidadeTripulante[] retorno = chamaServicoDjangoPaginado(dados);
		return retorno;
	}

	private boolean isOk(EntidadeDadosServicoSwapi dados) {
		// boolean ok = new Integer(HttpStatus.SC_OK).equals(dados.getStatus());
		// if (!ok) {
		// LOGGER.error("Erro ao acessar serviço " + dados.getPathUri());
		// }
		// return ok;
		return true;
	}

	public ProcessadorMensagem getProcessadorMensagemSwapi() {
		return processadorMensagemSwapi;
	}

	public void setProcessadorMensagemSwapi(ProcessadorMensagem processadorMensagemSwapi) {
		this.processadorMensagemSwapi = processadorMensagemSwapi;
	}

}
