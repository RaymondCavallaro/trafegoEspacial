package trafegoEspacial.entidade.view;

import java.io.Serializable;

public class EntidadeFiltroDjango implements Serializable {

	private static final long serialVersionUID = -6303791777717030620L;

	private String filtro;

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

}
