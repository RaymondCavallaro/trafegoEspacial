package trafegoEspacial.entidade;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.Ocupacao;

public class EntidadePlaneta implements InterfaceEntidade {

	@JsonProperty("url")
	private String url;
	@JsonProperty("name")
	private String nome;
	@JsonProperty("diameter")
	private String diametro;
	@JsonProperty("climate")
	private String clima;
	@JsonProperty("terrain")
	private String terreno;
	@JsonProperty("population")
	private String populacao;

	@JsonIgnore
	@SuppressWarnings("unchecked")
	private Ocupacao ocupacao = new Ocupacao(EntidadeTripulante.class, EntidadeNave.class);

	@JsonIgnore
	private Map<String, Object> ignore = new HashMap<String, Object>();

	@Override
	@JsonIgnore
	public String getChaveEntidade() {
		return getUrl();
	}

	@JsonAnySetter
	public void setIgnore(String name, Object value) {
		this.ignore.put(name, value);
	}

	@JsonIgnore
	public void setIgnore(Map<String, Object> ignore) {
		this.ignore = ignore;
	}

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty("name")
	public String getNome() {
		return nome;
	}

	@JsonProperty("name")
	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonProperty("diameter")
	public String getDiametro() {
		return diametro;
	}

	@JsonProperty("diameter")
	public void setDiametro(String diametro) {
		this.diametro = diametro;
	}

	@JsonProperty("climate")
	public String getClima() {
		return clima;
	}

	@JsonProperty("climate")
	public void setClima(String clima) {
		this.clima = clima;
	}

	@JsonProperty("terrain")
	public String getTerreno() {
		return terreno;
	}

	@JsonProperty("terrain")
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}

	@JsonProperty("population")
	public String getPopulacao() {
		return populacao;
	}

	@JsonProperty("population")
	public void setPopulacao(String populacao) {
		this.populacao = populacao;
	}

}