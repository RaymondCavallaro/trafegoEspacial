package trafegoEspacial.entidade.view;

public class EntidadeViewBreadcrumbLink {

	private String link;

	private String viewName;

	public EntidadeViewBreadcrumbLink() {
	}

	public EntidadeViewBreadcrumbLink(String link, String viewName) {
		this.link = link;
		this.viewName = viewName;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}