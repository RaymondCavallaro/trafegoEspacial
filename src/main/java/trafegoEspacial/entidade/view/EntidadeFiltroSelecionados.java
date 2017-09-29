package trafegoEspacial.entidade.view;

import java.io.Serializable;

import trafegoEspacial.entidade.EntidadeNave;
import trafegoEspacial.entidade.EntidadePlaneta;
import trafegoEspacial.entidade.EntidadeTripulante;
import trafegoEspacial.entidade.EntidadeViagem;

public class EntidadeFiltroSelecionados implements Serializable {

	private static final long serialVersionUID = 225161295504516389L;

	private EntidadePlaneta planeta;

	private EntidadeNave nave;

	private EntidadeTripulante tripulante;

	private EntidadeViagem viagem;

	public EntidadeNave getNave() {
		return nave;
	}

	public void setNave(EntidadeNave nave) {
		this.nave = nave;
	}

	public EntidadePlaneta getPlaneta() {
		return planeta;
	}

	public void setPlaneta(EntidadePlaneta planeta) {
		this.planeta = planeta;
	}

	public EntidadeTripulante getTripulante() {
		return tripulante;
	}

	public void setTripulante(EntidadeTripulante tripulante) {
		this.tripulante = tripulante;
	}

	public EntidadeViagem getViagem() {
		return viagem;
	}

	public void setViagem(EntidadeViagem viagem) {
		this.viagem = viagem;
	}

}
