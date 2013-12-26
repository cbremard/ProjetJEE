package projetJEE.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name="Affectation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnomalieAffectation {
	
	@Id @GeneratedValue
	@XmlTransient
	long id;
	@XmlAttribute(name = "ref")
	private String refToUtilisateur;
	
	
	public AnomalieAffectation() {
		super();
		this.refToUtilisateur = "";
	}
	
	public String getRefToUtilisateur() {
		return refToUtilisateur;
	}
	public void setRefToUtilisateur(String refToUtilisateur) {
		this.refToUtilisateur = refToUtilisateur;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
}
