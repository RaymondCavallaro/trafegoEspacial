package trafegoEspacial.entidade.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntidadeFiltroViagem implements Serializable {

	private static final long serialVersionUID = 4698664958901990673L;

	private Boolean filtraNave = Boolean.FALSE;
	private Boolean filtraPlaneta = Boolean.FALSE;
	private Boolean filtraTripulante = Boolean.FALSE;
	private List<String> status = new ArrayList<>();

	public Boolean getFiltraNave() {
		return filtraNave;
	}

	public void setFiltraNave(Boolean filtraNave) {
		this.filtraNave = filtraNave;
	}

	public Boolean getFiltraPlaneta() {
		return filtraPlaneta;
	}

	public void setFiltraPlaneta(Boolean filtraPlaneta) {
		this.filtraPlaneta = filtraPlaneta;
	}

	public Boolean getFiltraTripulante() {
		return filtraTripulante;
	}

	public void setFiltraTripulante(Boolean filtraTripulante) {
		this.filtraTripulante = filtraTripulante;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

}
