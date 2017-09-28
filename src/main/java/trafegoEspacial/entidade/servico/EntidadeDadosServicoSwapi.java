package trafegoEspacial.entidade.servico;

import java.util.Map;

import javax.ws.rs.client.Entity;

public class EntidadeDadosServicoSwapi {

	private String path;
	private Entity payload;
	private String method;
	private Map<String, String> headers;
	private String urlString;

	private String expressao;
	private Class returnClass;

	private Map<String, String> queryParameters;

	private int status;
	private String pathUri;

	private Integer ultimaPagina;
	private Integer paginaAtual;
	private String resultadosPorPagina;
	private Integer totalRegistros;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Class getReturnClass() {
		return returnClass;
	}

	public void setReturnClass(Class returnClass) {
		this.returnClass = returnClass;
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public Map<String, String> getQueryParameters() {
		return queryParameters;
	}

	public void setQueryParameters(Map<String, String> queryParameters) {
		this.queryParameters = queryParameters;
	}

	public Entity getPayload() {
		return payload;
	}

	public void setPayload(Entity payload) {
		this.payload = payload;
	}

	public Integer getUltimaPagina() {
		return ultimaPagina;
	}

	public void setUltimaPagina(Integer ultimaPagina) {
		this.ultimaPagina = ultimaPagina;
	}

	public Integer getPaginaAtual() {
		return paginaAtual;
	}

	public void setPaginaAtual(Integer paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	public String getResultadosPorPagina() {
		return resultadosPorPagina;
	}

	public void setResultadosPorPagina(String resultadosPorPagina) {
		this.resultadosPorPagina = resultadosPorPagina;
	}

	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPathUri() {
		return pathUri;
	}

	public void setPathUri(String pathUri) {
		this.pathUri = pathUri;
	}

	public String getExpressao() {
		return expressao;
	}

	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}

}
