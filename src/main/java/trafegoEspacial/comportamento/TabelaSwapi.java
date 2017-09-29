package trafegoEspacial.comportamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.SortOrder;

import trafegoEspacial.entidade.view.EntidadeFiltroDjango;
import trafegoEspacial.servico.bean.ConversorSwapi;
import trafegoEspacial.servico.bean.ServicoBuscaDadosSwapi;
import trafegoEspacial.view.componente.DatamodelEntidade;

public class TabelaSwapi {

	private ConversorSwapi conversor;

	private ServicoBuscaDadosSwapi servicoBuscaDadosSwapi;

	private EntidadeFiltroDjango filtro;

	private DatamodelEntidade datamodelEntidade;

	private List<InterfaceEntidade> selecao = new ArrayList<>();

	private final InterfaceBuscadorDados buscadorDados;

	public TabelaSwapi(ServicoBuscaDadosSwapi servicoBuscaDadosSwapi, ConversorSwapi conversor,
			InterfaceBuscadorDados buscadorDados) {
		this.servicoBuscaDadosSwapi = servicoBuscaDadosSwapi;
		this.conversor = conversor;
		this.buscadorDados = buscadorDados;
	}

	public void init() {
		filtro = new EntidadeFiltroDjango();
		pesquisar();
	}

	public void pesquisar() {
		servicoBuscaDadosSwapi.pesquisar(this);
	}

	public void limpaDetalhes() {
	}

	public Map<String, InterfaceEntidade> buscaDados(DatamodelEntidade model, int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) throws Exception {
		return servicoBuscaDadosSwapi.load(this, model.getDadosBusca(), model.getFiltro());
	}

	public List<InterfaceEntidade> verificaDados(List<InterfaceEntidade> dados) {
		return dados;
	}

	public ConversorSwapi getConversorNave() {
		return conversor;
	}

	public void setConversorNave(ConversorSwapi conversorNave) {
		this.conversor = conversorNave;
	}

	public DatamodelEntidade getDatamodelEntidade() {
		return datamodelEntidade;
	}

	public void setDatamodelEntidade(DatamodelEntidade datamodelEntidade) {
		this.datamodelEntidade = datamodelEntidade;
	}

	public EntidadeFiltroDjango getFiltro() {
		return filtro;
	}

	public void setFiltro(EntidadeFiltroDjango filtro) {
		this.filtro = filtro;
	}

	public InterfaceBuscadorDados getBuscadorDados() {
		return buscadorDados;
	}

	public ConversorSwapi getConversor() {
		return conversor;
	}

	public void setConversor(ConversorSwapi conversor) {
		this.conversor = conversor;
	}

	public ServicoBuscaDadosSwapi getServicoBuscaDadosSwapi() {
		return servicoBuscaDadosSwapi;
	}

	public void setServicoBuscaDadosSwapi(ServicoBuscaDadosSwapi servicoBuscaDadosSwapi) {
		this.servicoBuscaDadosSwapi = servicoBuscaDadosSwapi;
	}

	public List<InterfaceEntidade> getSelecao() {
		return selecao;
	}

	public void setSelecao(List<InterfaceEntidade> selecao) {
		this.selecao = selecao;
	}

}
