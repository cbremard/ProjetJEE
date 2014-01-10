package projetJEE.modele;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
    @XmlElement(name = "Login", required = true)
	private String login;
    @XmlElement(name = "Nom", required = true)
	private String nom;
    @XmlElement(name = "Prenom", required = true)
	private String prenom;
    @XmlElement(name = "Email", required = true)
	private String email;
    @XmlElement(name = "MotDePasse", required = true)
	private String motDePasse;
    
    @XmlElementWrapper(name = "Anomalies")
    @XmlElement(name = "Anomalie")
	@OneToMany(cascade=CascadeType.ALL) 
	@JoinColumn(name = "LOGIN_UTILISATEUR")
    private List<Anomalie> listeAnomalies;

    
	public Utilisateur() {
		super();
		this.login="";
		this.nom="";
		this.prenom="";
		this.email="";
		this.motDePasse="";
		this.listeAnomalies = new ArrayList<Anomalie>();
	}
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
	public List<Anomalie> getListeAnomalies() {
		return listeAnomalies;
	}
	public void setListeAnomalies(List<Anomalie> listeAnomalies) {
		this.listeAnomalies = listeAnomalies;
	}
	public void add(Anomalie anomalie) {
		this.listeAnomalies.add(anomalie);
	}
	public void remove(int index) {
		this.listeAnomalies.remove(index);
	}
    
    
    
}
