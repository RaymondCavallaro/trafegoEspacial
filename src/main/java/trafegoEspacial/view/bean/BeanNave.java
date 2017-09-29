package trafegoEspacial.view.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import trafegoEspacial.comportamento.InterfaceBuscadorDados;
import trafegoEspacial.comportamento.InterfaceEntidade;
import trafegoEspacial.comportamento.InterfaceViewBean;
import trafegoEspacial.comportamento.TabelaSwapi;
import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.servico.EntidadeDadosServicoSwapi;
import trafegoEspacial.servico.bean.Armazenamento;
import trafegoEspacial.servico.bean.BeanSelecionados;
import trafegoEspacial.servico.bean.ConversorSwapi;
import trafegoEspacial.servico.bean.ServicoBuscaDadosSwapi;
import trafegoEspacial.servico.bean.ServicoSwabi;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BeanNave implements InterfaceViewBean {

	private static final String CHAVE_VIEW = "naves";

	@Autowired
	private Armazenamento armazenamento;

	@Autowired
	private ServicoBuscaDadosSwapi servicoBuscaDadosSwapi;

	@Autowired
	private ServicoSwabi servicoSwabi;

	@Autowired
	private BeanSelecionados beanSelecionados;

	@Autowired
	private ConversorSwapi conversor;

	private TabelaSwapi tabela;

	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}

	@PostConstruct
	public void postConstruct() {
		init();
	}

	private void init() {
		InterfaceBuscadorDados buscador = new InterfaceBuscadorDados() {

			@Override
			public InterfaceEntidade[] buscaDados(EntidadeDadosServicoSwapi dadosBusca) throws Exception {
				InterfaceEntidade[] naves = servicoSwabi.buscaNavesPagina(dadosBusca);
				List<EntidadeNave> listNaves = new ArrayList<>(Arrays.asList((EntidadeNave[]) naves));
				armazenamento.adicionaNaves(listNaves);
				List<String> chaves = armazenamento.filtra(listNaves, Armazenamento.CHAVE_ARMAZENAMENTOMAPA_FILTROCHAVE,
						"", "url", "");
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(chaves);
				List<String> idsNavesFiltrada = armazenamento.filtra(armazenamento.getMapaViagem().values(),
						Armazenamento.CHAVE_ARMAZENAMENTOMAPA_FILTROCRUZADO, "tripulantes", "nave.url", json);
				chaves.removeAll(idsNavesFiltrada);
				if (!chaves.isEmpty()) {
					for (Iterator<EntidadeNave> iterator = listNaves.iterator(); iterator.hasNext();) {
						EntidadeNave nave = iterator.next();
						if (!chaves.contains(nave.getUrl())) {
							iterator.remove();
						}
					}
				}
				List<EntidadeNave> navesFiltradas = armazenamento.filtra(armazenamento.getMapaNave(), idsNavesFiltrada);
				listNaves.addAll(navesFiltradas);
				return listNaves.toArray(new InterfaceEntidade[0]);
			}
		};
		tabela = new TabelaSwapi(servicoBuscaDadosSwapi, conversor, buscador);
		tabela.init();
	}

	public void selecionaNave(EntidadeNave nave) {
		beanSelecionados.getFiltroSelecionados().setNave(nave);
	}

	public String getChave() {
		return CHAVE_VIEW;
	}

	public ServicoSwabi getServicoSwabi() {
		return servicoSwabi;
	}

	public void setServicoSwabi(ServicoSwabi servicoSwabi) {
		this.servicoSwabi = servicoSwabi;
	}

	public BeanSelecionados getBeanSelecionados() {
		return beanSelecionados;
	}

	public void setBeanSelecionados(BeanSelecionados beanSelecionados) {
		this.beanSelecionados = beanSelecionados;
	}

	public ServicoBuscaDadosSwapi getServicoBuscaDadosSwapi() {
		return servicoBuscaDadosSwapi;
	}

	public void setServicoBuscaDadosSwapi(ServicoBuscaDadosSwapi servicoBuscaDadosSwapi) {
		this.servicoBuscaDadosSwapi = servicoBuscaDadosSwapi;
	}

	public ConversorSwapi getConversor() {
		return conversor;
	}

	public void setConversor(ConversorSwapi conversor) {
		this.conversor = conversor;
	}

	public TabelaSwapi getTabela() {
		return tabela;
	}

	public void setTabela(TabelaSwapi tabela) {
		this.tabela = tabela;
	}

}