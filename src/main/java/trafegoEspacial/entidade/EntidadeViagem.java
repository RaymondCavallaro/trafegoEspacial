package trafegoEspacial.entidade;

public class EntidadeViagem {

	public static final String VIAGEM_STATUS_FINALIZADA = "entidade.viagem.propriedade.status.valor.finalizado";
	public static final String VIAGEM_STATUS_EM_CURSO = "entidade.viagem.propriedade.status.valor.curso";
	public static final String VIAGEM_STATUS_FUTURO = "entidade.viagem.propriedade.status.valor.futuro";

	// @SuppressWarnings("unchecked")
	// private Ocupacao ocupacao = new Ocupacao(EntidadeNave.class);

	private String chave;

	private EntidadePlaneta destino;

	private EntidadeNave nave;

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EntidadePlaneta getDestino() {
		return destino;
	}

	public void setDestino(EntidadePlaneta destino) {
		this.destino = destino;
	}

	public EntidadeNave getNave() {
		return nave;
	}

	public void setNave(EntidadeNave nave) {
		this.nave = nave;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

}