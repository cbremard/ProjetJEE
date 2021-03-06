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
@XmlRootElement(name = "Projet")
@XmlAccessorType(XmlAccessType.FIELD)
public class Projet {
	
	@Id
	@XmlElement(name = "Nom", required = true)
	private String nom;
	@XmlElementWrapper(name = "Anomalies")
    @XmlElement(name = "Anomalie")
	@OneToMany(cascade=CascadeType.ALL) 
	@JoinColumn(name = "NOM_PROJET")
	private List<Anomalie> anomalies;
	
	
	
	
	public Projet() {
		super();
		this.nom="";
		this.anomalies = new ArrayList<Anomalie>();
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<Anomalie> getAnomalies() {
		return anomalies;
	}
	public void setAnomalies(List<Anomalie> anomalies) {
		this.anomalies = anomalies;
	}
	public void add(Anomalie anomalie) {
		this.anomalies.add(anomalie);
	}
	public void remove(int index) {
		this.anomalies.remove(index);
	}

}
