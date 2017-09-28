package trafegoEspacial.comportamento;

import java.util.ArrayList;
import java.util.List;

public class RegiaoOcupavel<Tipo extends InterfaceOcupante> {

	private Ocupacao ocupacao;

	private List<Tipo> ocupantes = new ArrayList<>();

	public List<Tipo> getOcupantes() {
		return ocupantes;
	}

	public void setOcupantes(List<Tipo> ocupantes) {
		this.ocupantes = ocupantes;
	}

	public Ocupacao getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(Ocupacao ocupacao) {
		this.ocupacao = ocupacao;
	}

}