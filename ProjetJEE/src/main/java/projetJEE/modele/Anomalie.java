package projetJEE.modele;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@XmlRootElement(name="Anomalie")
@XmlAccessorType(XmlAccessType.FIELD)
public class Anomalie {
	
	@Id @GeneratedValue
	@XmlTransient
	long id;
	@XmlTransient
	private String nomProjet;
	@XmlTransient
	private String loginUtilisateur;
	
	@Column(columnDefinition="VARCHAR(255)")
	@XmlElement(name="Sujet", required = true)
	private String sujet;
	@XmlElement(name="Description", required = true)
	@Column(columnDefinition="VARCHAR(5000)")
	private String description;
	@XmlElement(name="Etat")
	@Column(columnDefinition="VARCHAR(8)")
	private String etat;
	@XmlElement(name="Affectation")
	@Transient
	private AnomalieAffectation affectation;
	@XmlElementWrapper(name = "Notes")
    @XmlElement(name = "Note")
	@Transient
	private List<Note> notes;
	
	public Anomalie() {
		super();
		setEtatToNouveau();
		notes = new ArrayList<Note>();
		affectation = new AnomalieAffectation();
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSujet() {
		return sujet;
	}
	public void setSujet(String sujet) {
		this.sujet = sujet;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	public String getNomProjet() {
		return nomProjet;
	}
	public void setNomProjet(String nomProjet) {
		this.nomProjet = nomProjet;
	}
	public String getLoginUtilisateur() {
		return loginUtilisateur;
	}
	public void setLoginUtilisateur(String loginUtilisateur) {
		this.loginUtilisateur = loginUtilisateur;
	}
	public AnomalieAffectation getAffectation() {
		return affectation;
	}
	public void setAffectation(AnomalieAffectation affectation) {
		this.affectation = affectation;
	}
	public List<Note> getNotes() {
		return notes;
	}
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public void setEtatToNouveau() {
		this.etat = "NOUVEAU";
	}
	public void setEtatToAffectee() {
		this.etat = "AFFECTEE";
	}
	public void setEtatToResolue() {
		this.etat = "RESOLUE";
	}
	public void setEtatToFermee() {
		this.etat = "FERMEE";
	}
	public void addNote(Note note) {
		this.notes.add(note);
	}
	public void removeNote(int index) {
		this.notes.remove(index);
	}

	
	
	


}
