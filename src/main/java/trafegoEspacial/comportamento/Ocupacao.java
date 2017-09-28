package trafegoEspacial.comportamento;

import java.util.HashMap;
import java.util.Map;

public class Ocupacao {

	private Map<Class<InterfaceOcupante>, RegiaoOcupavel<InterfaceOcupante>> regioes = new HashMap<>();

	public Ocupacao() {
	}

	public Ocupacao(Class<? extends InterfaceOcupante>... classes) {
		for (Class<? extends InterfaceOcupante> clazz : classes) {
			putNew(clazz);
		}
	}

	public <IO extends InterfaceOcupante> void putNew(Class<IO> clazz) {
		regioes.put((Class<InterfaceOcupante>) clazz, (RegiaoOcupavel<InterfaceOcupante>) new RegiaoOcupavel<IO>());
	}

	public Map<Class<InterfaceOcupante>, RegiaoOcupavel<InterfaceOcupante>> getRegioes() {
		return regioes;
	}

	public void setRegioes(Map<Class<InterfaceOcupante>, RegiaoOcupavel<InterfaceOcupante>> regioes) {
		this.regioes = regioes;
	}

}