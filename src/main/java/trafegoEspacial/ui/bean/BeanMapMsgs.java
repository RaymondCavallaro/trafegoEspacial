package trafegoEspacial.ui.bean;

import java.util.AbstractMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component("msgs")
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class BeanMapMsgs {

	@Autowired
	private MessageSource mensagens;

	private Map<String, String> map = new AbstractMap<String, String>() {

		@Override
		public String get(Object key) {
			try {
				return mensagens.getMessage((String) key, null, getLocale());
			} catch (Exception ex) {
				return (String) key;
			}
		}

		@Override
		public Set<Map.Entry<String, String>> entrySet() {
			return null;
		}

	};

	public Locale getLocale() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Locale loc = fc.getELContext().getLocale();
		if (fc.getExternalContext() != null) {
			loc = fc.getExternalContext().getRequestLocale();
		}
		UIViewRoot viewRoot = fc.getViewRoot();
		if (viewRoot != null) {
			loc = viewRoot.getLocale();
		}
		if (loc == null) {
			loc = fc.getApplication().getDefaultLocale();
		}

		return loc;
	}

	public MessageSource getMensagens() {
		return mensagens;
	}

	public void setMensagens(MessageSource mensagens) {
		this.mensagens = mensagens;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
