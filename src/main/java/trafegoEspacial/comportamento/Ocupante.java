package trafegoEspacial.comportamento;

public class Ocupante implements InterfaceOcupante {

	private RegiaoOcupavel<InterfaceOcupante> regiaoOcupada;

	public RegiaoOcupavel<InterfaceOcupante> getRegiaoOcupada() {
		return regiaoOcupada;
	}

	public void setRegiaoOcupada(RegiaoOcupavel<InterfaceOcupante> regiaoOcupada) {
		this.regiaoOcupada = regiaoOcupada;
	}

}