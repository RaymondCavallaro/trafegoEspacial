package trafegoEspacial.entidade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.Ocupante;

@JsonIgnoreProperties({ "regiaoOcupada" })
public class EntidadeTripulante extends Ocupante implements InterfaceEntidade {

	@JsonProperty("url")
	private String url;
	@JsonProperty("name")
	private String nome;
	@JsonProperty("gender")
	private String genero;

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

	@JsonProperty("gender")
	public String getGenero() {
		return genero;
	}

	@JsonProperty("gender")
	public void setGenero(String genero) {
		this.genero = genero;
	}

	@JsonIgnore
	public List<EntidadeViagem> getViagens() {
		return viagens;
	}

	@JsonIgnore
	public void setViagens(List<EntidadeViagem> viagens) {
		this.viagens = viagens;
	}

}