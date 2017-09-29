package trafegoEspacial.comportamento;

import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;

public interface InterfaceBuscadorDados {

	InterfaceEntidade[] buscaDados(EntidadeDadosServicoSwapi dadosBusca) throws Exception;

}
