package trafegoEspacial.ui.componente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SelectableDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.TabelaSwapi;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;
import trafegoEspacial.entidade.view.EntidadeFiltroDjango;

public class DatamodelEntidade extends LazyDataModel<InterfaceEntidade>
		implements SelectableDataModel<InterfaceEntidade> {

	private static final long serialVersionUID = 872762449122181427L;

	private EntidadeFiltroDjango filtro;

	private Map<String, InterfaceEntidade> dados;

	private EntidadeDadosServicoSwapi dadosBusca;

	private final TabelaSwapi tabelaSwapi;

	public DatamodelEntidade(TabelaSwapi tabelaSwapi) {
		this.tabelaSwapi = tabelaSwapi;
	}

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	public void init(EntidadeFiltroDjango filtro, EntidadeDadosServicoSwapi dadosBusca) {
		this.filtro = filtro;
		this.dadosBusca = dadosBusca;
		dadosBusca.setUltimaPagina(1);
		dados = new HashMap<>();
	}

	private void ajustaPaginaPelaQuantidade(Integer quantidadePorPagina) {
		if (!StringUtils.isNumeric(dadosBusca.getResultadosPorPagina())
				|| Integer.parseInt(dadosBusca.getResultadosPorPagina()) < 0) {
			dadosBusca.setResultadosPorPagina("1");
		}
		if (dadosBusca.getPaginaAtual() > 0) {
			Integer listados = dadosBusca.getPaginaAtual() * Integer.parseInt(dadosBusca.getResultadosPorPagina());
			if (listados % quantidadePorPagina == 0) {
				listados--;
			}
			Integer pagina = listados / quantidadePorPagina;
			dadosBusca.setPaginaAtual(pagina);
		}
	}

	private int primeiro() {
		if (dadosBusca.getPaginaAtual() > 0) {
			return Integer.valueOf(dadosBusca.getResultadosPorPagina()) * (dadosBusca.getPaginaAtual() - 1);
		}
		return 0;
	}

	public List<InterfaceEntidade> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if (getPageSize() != pageSize) {
			init(filtro, dadosBusca);
			dadosBusca.setResultadosPorPagina(new Integer(pageSize).toString());
		}
		if (!new Integer(pageSize).toString().equals(dadosBusca.getResultadosPorPagina())) {
			ajustaPaginaPelaQuantidade(pageSize);
			dadosBusca.setResultadosPorPagina(new Integer(pageSize).toString());
		}
		if (primeiro() != first) {
			dadosBusca.setPaginaAtual(first / pageSize);
		}
		try {
			this.dados.clear();
			Map<String, InterfaceEntidade> retorno = tabelaSwapi.buscaDados(this, first, pageSize, sortField, sortOrder,
					filters);
			this.dados.putAll(retorno);
			setPageSize(new Integer(dadosBusca.getResultadosPorPagina()));
		} catch (Exception ex) {
			dadosBusca.setUltimaPagina(1);
			this.dados = new HashMap<>();
			getLogger().warn(ex.getMessage(), ex);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
		}
		if (dadosBusca.getTotalRegistros() != null) {
			this.setRowCount(dadosBusca.getTotalRegistros());
		} else if (dadosBusca.getUltimaPagina() == null) {
			this.setRowCount(this.getPageSize());
		} else {
			this.setRowCount(dadosBusca.getUltimaPagina() * this.getPageSize());
		}
		return new ArrayList<>(dados.values());
	}

	public InterfaceEntidade getRowData(String rowKey) {
		return dados.get(rowKey);
	}

	public Object getRowKey(InterfaceEntidade entidade) {
		if (entidade != null) {
			return entidade.getChaveEntidade();
		}
		return null;
	}

	public EntidadeFiltroDjango getFiltro() {
		return filtro;
	}

	public void setFiltro(EntidadeFiltroDjango filtro) {
		this.filtro = filtro;
	}

	public EntidadeDadosServicoSwapi getDadosBusca() {
		return dadosBusca;
	}

	public void setDadosBusca(EntidadeDadosServicoSwapi dadosBusca) {
		this.dadosBusca = dadosBusca;
	}

	public Map<String, InterfaceEntidade> getDados() {
		return dados;
	}

	public void setDados(Map<String, InterfaceEntidade> dados) {
		this.dados = dados;
	}

}
