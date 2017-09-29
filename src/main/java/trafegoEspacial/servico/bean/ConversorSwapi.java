package trafegoEspacial.servico.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import trafegoEspacial.entidade.view.EntidadeFiltroDjango;

@Component
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class ConversorSwapi implements Serializable {

	private static final long serialVersionUID = -7682320834531990104L;

	public Map<String, String> montaMapa(EntidadeFiltroDjango filtro) throws IOException {
		Map<String, String> jsonMap = new HashMap<String, String>();

		if (!StringUtils.isEmpty(filtro.getFiltro())) {
			jsonMap.put("search", filtro.getFiltro());
		}

		return jsonMap;
	}

}
