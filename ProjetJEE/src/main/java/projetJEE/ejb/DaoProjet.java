package projetJEE.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import projetJEE.modele.Anomalie;
import projetJEE.modele.Projet;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DaoProjet {

	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
    public void initialisation() {
		Projet projet1 = new Projet();
		projet1.setNom("projet1");
		Anomalie anomalie1 = new Anomalie();
		anomalie1.setSujet("anomalie1");
		anomalie1.setDescription("Description de l'anomalie1");
		anomalie1.setNomProjet("projet1");
		projet1.add(anomalie1);
		Anomalie anomalie2 = new Anomalie();
		anomalie2.setSujet("anomalie2");
		anomalie2.setDescription("Description de l'anomalie2");
		anomalie2.setNomProjet("projet1");
		projet1.add(anomalie2);
		persister(projet1);
		Projet projet2 = new Projet();
		projet2.setNom("projet2");
		Anomalie anomalie3 = new Anomalie();
		anomalie3.setSujet("anomalie3");
		anomalie3.setDescription("Description de l'anomalie3");
		anomalie2.setNomProjet("projet2");
		projet2.add(anomalie3);
		persister(projet2);
		Projet projet3 = new Projet();
		projet3.setNom("projet3");
		persister(projet3);
	}
	
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Projet persister(final Projet projet) {
        try{
        	em.persist(projet);
        }catch(Exception e){
        	em.getTransaction().rollback();
        }
        return projet;
    }

	@Lock(LockType.READ)
	public Projet getProjet(String nom) {
		return em.find(Projet.class, nom);
	}

	@SuppressWarnings("unchecked")
	public List<Projet> getProjets() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return em.createNativeQuery("select * from projet",Projet.class).getResultList();
	}
	
	@Lock(LockType.WRITE)
	public void removeProjet(Projet oldProjet) {
		em.remove(oldProjet);
	}

	@Lock(LockType.READ)
	public boolean existeDejaEnBase(Projet projet) {
		return(em.find(Projet.class, projet.getNom()) != null);
	}

	@Lock(LockType.WRITE)
	public void persisterAnomalie(String nomProjet, Anomalie anomalie) {
		Projet projetCourant;
		projetCourant = getProjet(nomProjet);
		projetCourant.add(anomalie);
		persister(projetCourant);
	}

	@Lock(LockType.READ)
	public Anomalie getAnomalie(long id) {
		return em.find(Anomalie.class, id);
	}

	public List<Anomalie> getAnomaliesOfProject(String nomProjet) {
		Projet projet;
		List<Anomalie> anomalies = new ArrayList<Anomalie>();
		projet = getProjet(nomProjet);
		anomalies = projet.getAnomalies();
		return anomalies;
	}

	public Anomalie getAnomalieOfProject(String nomProjet,String sujetAnomalie) {
		Projet projet;
		List<Anomalie> anomalies = new ArrayList<Anomalie>();
		Anomalie anomalie = null;
		try{
			projet = getProjet(nomProjet);
			anomalies = projet.getAnomalies();
			for (Anomalie anomalieCourante : anomalies) {
				if(anomalieCourante.getSujet()==sujetAnomalie){
					anomalie=anomalieCourante;
					break;
				}
			}
		}catch(Exception e){
			anomalie = null;
		}
		return anomalie;
	}

	public <T> Object find(Class<T> class1, Object key) {
		return em.find(class1, key);
	}

}
