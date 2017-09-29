package trafegoEspacial.ui.bean.view.beanViagem;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.EntidadeTripulante;
import trafegoEspacial.entidade.EntidadeViagem;
import trafegoEspacial.entidade.view.EntidadeFiltroSelecionados;
import trafegoEspacial.servico.bean.Armazenamento;
import trafegoEspacial.ui.bean.BeanSelecionados;
import trafegoEspacial.ui.bean.view.BeanViagem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "file:src/main/resources/config/spring/applicationContext-geral.xml" })
public final class BeanViagemTest {

	private Armazenamento armazenamento;

	private BeanSelecionados beanSelecionados;

	private BeanViagem beanViagem;

	@Before
	public void setUp() throws Exception {
		beanViagem = new BeanViagem();
	}

	@Test
	public void testAdicionarTripulante() {
		// configuracao dos dados de teste

		/*
		 * configuracao de novos itens mock e seus comportamentos para a realizacao do
		 * teste
		 */
		EntidadeViagem viagem = new EntidadeViagem();
		EntidadeNave nave = new EntidadeNave();
		viagem.setNave(nave);

		nave.setPassageiros(2);

		EntidadeTripulante tripulanteASerAdicionado = new EntidadeTripulante();

		armazenamento = new Armazenamento();
		armazenamento.setMapaTripulante(new HashMap<String, EntidadeTripulante>());
		armazenamento.setMapaViagem(new HashMap<String, EntidadeViagem>());
		beanViagem.setArmazenamento(armazenamento);

		beanSelecionados = new BeanSelecionados();
		beanSelecionados.setFiltroSelecionados(new EntidadeFiltroSelecionados());
		beanSelecionados.getFiltroSelecionados().setTripulante(tripulanteASerAdicionado);
		beanViagem.setBeanSelecionados(beanSelecionados);

		/*
		 * realizacao das atividades que serao testadas dos itens da aplicacao
		 */
		beanViagem.adicionarTripulante(viagem);
		/*
		 * verificacao dos comportamentos esperados e dos nao permitidos dos itens
		 * testados
		 */
		Assert.assertTrue(viagem.getTripulantes().contains(tripulanteASerAdicionado));
		Assert.assertTrue(tripulanteASerAdicionado.getViagens().contains(viagem));
	}

}