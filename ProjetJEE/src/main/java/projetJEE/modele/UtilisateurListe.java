package javaEE.projetJEE.modele;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ListeUtilisteurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class UtilisateurListe {

    @XmlElement(name = "Utilisateur")
	private List<Utilisateur> utilisateurs;
    @XmlAttribute(name = "page")
	private int page;
    @XmlAttribute(name = "pagePrecedente")
	private String pagePrecedente;
    @XmlAttribute(name = "pageSuivante")
	private String pageSuivante;
    
    
    
	public UtilisateurListe() {
		super();
		this.utilisateurs = new ArrayList<Utilisateur>();
		this.page = 0;
		this.pagePrecedente = "";
		this.pageSuivante = "";
	}
	public List<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}
	public void setUtilisateurs(List<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}
	public void add(Utilisateur utilisateur) {
		this.utilisateurs.add(utilisateur);
	}
	public void remove(int index) {
		this.utilisateurs.remove(index);
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getPagePrecedente() {
		return pagePrecedente;
	}
	public void setPagePrecedente(String pagePrecedente) {
		this.pagePrecedente = pagePrecedente;
	}
	public String getPageSuivante() {
		return pageSuivante;
	}
	public void setPageSuivante(String pageSuivante) {
		this.pageSuivante = pageSuivante;
	}
    
    

}
