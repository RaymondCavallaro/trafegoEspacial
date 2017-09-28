package trafegoEspacial.comportamento;

import java.util.Map;

import org.primefaces.model.SortOrder;

import trafegoEspacial.view.componente.DatamodelEntidade;

public interface InterfaceBuscadorDados {

	Map<String, InterfaceEntidade> buscaDados(DatamodelEntidade model, int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) throws Exception;

}
