package trafegoEspacial.view.componente;

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

import trafegoEspacial.comportamento.InterfaceBuscadorDados;
import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;
import trafegoEspacial.entidade.view.EntidadeFiltroDjango;

public class DatamodelEntidade extends LazyDataModel<InterfaceEntidade>
		implements SelectableDataModel<InterfaceEntidade> {

	private static final long serialVersionUID = 872762449122181427L;

	private EntidadeFiltroDjango filtro;

	private Map<String, InterfaceEntidade> dados;

	private EntidadeDadosServicoSwapi dadosBuscaNave;

	private final InterfaceBuscadorDados buscadorDados;

	public DatamodelEntidade(InterfaceBuscadorDados interfaceBuscadorDados) {
		this.buscadorDados = interfaceBuscadorDados;
	}

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	public void init(EntidadeFiltroDjango filtro, EntidadeDadosServicoSwapi dadosBuscaNave) {
		this.filtro = filtro;
		this.dadosBuscaNave = dadosBuscaNave;
		dadosBuscaNave.setUltimaPagina(1);
		dados = new HashMap<>();
	}

	private void ajustaPaginaPelaQuantidade(Integer quantidadePorPagina) {
		if (!StringUtils.isNumeric(dadosBuscaNave.getResultadosPorPagina())
				|| Integer.parseInt(dadosBuscaNave.getResultadosPorPagina()) < 0) {
			dadosBuscaNave.setResultadosPorPagina("1");
		}
		if (dadosBuscaNave.getPaginaAtual() > 0) {
			Integer listados = dadosBuscaNave.getPaginaAtual()
					* Integer.parseInt(dadosBuscaNave.getResultadosPorPagina());
			if (listados % quantidadePorPagina == 0) {
				listados--;
			}
			Integer pagina = listados / quantidadePorPagina;
			dadosBuscaNave.setPaginaAtual(pagina);
		}
	}

	private int primeiro() {
		if (dadosBuscaNave.getPaginaAtual() > 0) {
			return Integer.valueOf(dadosBuscaNave.getResultadosPorPagina()) * (dadosBuscaNave.getPaginaAtual() - 1);
		}
		return 0;
	}

	public List<InterfaceEntidade> load(int first, int pageSize, String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		if (getPageSize() != pageSize) {
			init(filtro, dadosBuscaNave);
			dadosBuscaNave.setResultadosPorPagina(new Integer(pageSize).toString());
		}
		if (!new Integer(pageSize).toString().equals(dadosBuscaNave.getResultadosPorPagina())) {
			ajustaPaginaPelaQuantidade(pageSize);
			dadosBuscaNave.setResultadosPorPagina(new Integer(pageSize).toString());
		}
		if (primeiro() != first) {
			dadosBuscaNave.setPaginaAtual(first / pageSize);
		}
		try {
			this.dados.clear();
			Map<String, InterfaceEntidade> retorno = buscadorDados.buscaDados(this, first, pageSize, sortField,
					sortOrder, filters);
			this.dados.putAll(retorno);
			setPageSize(new Integer(dadosBuscaNave.getResultadosPorPagina()));
		} catch (Exception ex) {
			dadosBuscaNave.setUltimaPagina(1);
			this.dados = new HashMap<>();
			getLogger().warn(ex.getMessage(), ex);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
		}
		if (dadosBuscaNave.getTotalRegistros() != null) {
			this.setRowCount(dadosBuscaNave.getTotalRegistros());
		} else if (dadosBuscaNave.getUltimaPagina() == null) {
			this.setRowCount(this.getPageSize());
		} else {
			this.setRowCount(dadosBuscaNave.getUltimaPagina() * this.getPageSize());
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

	public EntidadeDadosServicoSwapi getDadosBuscaNave() {
		return dadosBuscaNave;
	}

	public void setDadosBuscaNave(EntidadeDadosServicoSwapi dadosBuscaNave) {
		this.dadosBuscaNave = dadosBuscaNave;
	}

	public InterfaceBuscadorDados getBuscadorDados() {
		return buscadorDados;
	}

	public Map<String, InterfaceEntidade> getDados() {
		return dados;
	}

	public void setDados(Map<String, InterfaceEntidade> dados) {
		this.dados = dados;
	}

}
