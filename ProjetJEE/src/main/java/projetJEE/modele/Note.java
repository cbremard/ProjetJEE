package projetJEE.modele;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name = "Note")
@XmlAccessorType(XmlAccessType.FIELD)
public class Note {

	@Id @GeneratedValue
	@XmlTransient
	long id;
    @XmlElement(name = "Date")
    @Temporal(TemporalType.DATE)
	private Date date;
    @XmlElement(name = "Description", required = true)
	private String description;
    
	public Note() {
		super();
		date = new Date();
		this.description="";
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    
}
