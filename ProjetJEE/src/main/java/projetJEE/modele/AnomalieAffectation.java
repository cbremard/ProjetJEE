package javaEE.projetJEE.modele;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="Affectation")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnomalieAffectation {
	
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
}
