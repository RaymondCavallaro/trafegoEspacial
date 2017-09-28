package trafegoEspacial.entidade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.Ocupante;

@JsonIgnoreProperties({ "regiaoOcupada" })
public class EntidadeNave extends Ocupante implements InterfaceEntidade {

	@JsonProperty("url")
	private String url;
	@JsonProperty("name")
	private String nome;
	@JsonProperty("model")
	private String modelo;
	@JsonProperty("passengers")
	private Integer passageiros;

	// @JsonIgnore
	// @SuppressWarnings("unchecked")
	// private Ocupacao ocupacaoTripulantes = new
	// Ocupacao(EntidadeTripulante.class);

	@JsonIgnore
	private List<EntidadeTripulante> tripulantes = new ArrayList<EntidadeTripulante>();

	@JsonIgnore
	private List<EntidadeViagem> viagens = new ArrayList<EntidadeViagem>();

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

	@JsonIgnore
	public List<EntidadeViagem> getViagens() {
		return viagens;
	}

	@JsonIgnore
	public void setViagens(List<EntidadeViagem> viagens) {
		this.viagens = viagens;
	}

	@JsonAnyGetter
	public Map<String, Object> getIgnore() {
		return this.ignore;
	}

	@JsonProperty("name")
	public String getNome() {
		return nome;
	}

	@JsonProperty("name")
	public void setNome(String nome) {
		this.nome = nome;
	}

	@JsonProperty("passengers")
	public Integer getPassageiros() {
		return passageiros;
	}

	@JsonProperty("passengers")
	public void setPassageiros(Integer passageiros) {
		this.passageiros = passageiros;
	}

	@JsonProperty("url")
	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	@JsonProperty("model")
	public String getModelo() {
		return modelo;
	}

	@JsonProperty("model")
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	@JsonIgnore
	public List<EntidadeTripulante> getTripulantes() {
		return tripulantes;
	}

	@JsonIgnore
	public void setTripulantes(List<EntidadeTripulante> tripulantes) {
		this.tripulantes = tripulantes;
	}

}