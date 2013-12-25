package javaEE.projetJEE.modele;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "Utilisateur")
@XmlAccessorType(XmlAccessType.FIELD)
public class Utilisateur {

	@Id
    @XmlElement(name = "Login")
	private String login;
    @XmlElement(name = "Nom")
	private String nom;
    @XmlElement(name = "Prenom")
	private String prenom;
    @XmlElement(name = "Email")
	private String email;
    @XmlElement(name = "MotDePasse")
	private String motDePasse;
    @XmlElement(name = "IsAdmin")
	private boolean isAdmin;
    
    @XmlElementWrapper(name = "Anomalies")
    @XmlElement(name = "Anomalie")
    @Transient
    private List<Anomalie> listeAnomalies;


	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public List<Anomalie> getListeAnomalies() {
		return listeAnomalies;
	}
	public void setListeAnomalies(List<Anomalie> listeAnomalies) {
		this.listeAnomalies = listeAnomalies;
	}
    
    
    
}
