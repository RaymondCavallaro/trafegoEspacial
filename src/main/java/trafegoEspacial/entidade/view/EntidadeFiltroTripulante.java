package trafegoEspacial.entidade.view;

import java.io.Serializable;

public class EntidadeFiltroTripulante implements Serializable {

	private static final long serialVersionUID = -1985084650177016251L;

	private Boolean filtraViagem = Boolean.FALSE;

	public Boolean getFiltraViagem() {
		return filtraViagem;
	}

	public void setFiltraViagem(Boolean filtraViagem) {
		this.filtraViagem = filtraViagem;
	}

}
