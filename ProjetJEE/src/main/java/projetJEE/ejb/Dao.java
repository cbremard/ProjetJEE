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
import projetJEE.modele.Utilisateur;

@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Singleton
public class Dao {

	@PersistenceContext
	private EntityManager bdd;
	
	/*
	 *  Initialisation de la BDD pour repartir d'une base propre et connue
	 */
	
	@PostConstruct
    public void initialisation() {
		Utilisateur utilisateur1 = new Utilisateur(); utilisateur1.setLogin("coco"); utilisateur1.setPrenom("Corentin"); utilisateur1.setNom("Brémard"); utilisateur1.setMotDePasse("coco"); utilisateur1.setEmail("corentin.bremard@eleve.ensai.fr"); utilisateur1.setAdmin(true);
		persisterUtilisateur(utilisateur1);
		Utilisateur utilisateur2 = new Utilisateur(); utilisateur2.setLogin("nyfa"); utilisateur2.setPrenom("Fanny"); utilisateur2.setNom("Thomas"); utilisateur2.setMotDePasse("nyfa"); utilisateur2.setEmail("fanny.thomas@eleve.ensai.fr"); utilisateur2.setAdmin(true);
		persisterUtilisateur(utilisateur2);
		Utilisateur utilisateur3 = new Utilisateur(); utilisateur3.setLogin("michu"); utilisateur3.setPrenom("Madame"); utilisateur3.setNom("Michu"); utilisateur3.setMotDePasse("michu"); utilisateur3.setEmail("madame.michu@email.com"); utilisateur3.setAdmin(false);
		persisterUtilisateur(utilisateur3);
		Utilisateur utilisateur4 = new Utilisateur(); utilisateur4.setLogin("login1"); utilisateur4.setPrenom("Prenom1"); utilisateur4.setNom("Nom1"); utilisateur4.setMotDePasse("login1"); utilisateur4.setEmail("prenom1.nom1@email.truc"); utilisateur4.setAdmin(false);
		persisterUtilisateur(utilisateur4);
		Utilisateur utilisateur5 = new Utilisateur(); utilisateur5.setLogin("login2"); utilisateur5.setPrenom("Prenom2"); utilisateur5.setNom("Nom2"); utilisateur5.setMotDePasse("login1"); utilisateur5.setEmail("prenom2.nom2@email.chose"); utilisateur5.setAdmin(false);
		persisterUtilisateur(utilisateur5);
		Utilisateur utilisateur6 = new Utilisateur(); utilisateur6.setLogin("login3"); utilisateur6.setPrenom("Prenom3"); utilisateur6.setNom("Nom3"); utilisateur6.setMotDePasse("login3"); utilisateur6.setEmail("prenom3.nom3@email.machin"); utilisateur6.setAdmin(false);
		persisterUtilisateur(utilisateur6);
		Utilisateur utilisateur7 = new Utilisateur(); utilisateur7.setLogin("login4"); utilisateur7.setPrenom("Prenom4"); utilisateur7.setNom("Nom4"); utilisateur7.setMotDePasse("login4"); utilisateur7.setEmail("prenom4.nom4@email.bidule"); utilisateur7.setAdmin(false);
		persisterUtilisateur(utilisateur7);
		Utilisateur utilisateur8 = new Utilisateur(); utilisateur8.setLogin("login5"); utilisateur8.setPrenom("Prenom5"); utilisateur8.setNom("Nom5"); utilisateur8.setMotDePasse("login5"); utilisateur8.setEmail("prenom5.nom5@email.uk"); utilisateur8.setAdmin(false);
		persisterUtilisateur(utilisateur8);

		Projet projet1 = new Projet(); projet1.setNom("projet1");
		Anomalie anomalie1 = new Anomalie(); anomalie1.setSujet("anomalie1"); anomalie1.setDescription("Description de l'anomalie1"); anomalie1.setNomProjet("projet1");projet1.add(anomalie1);
		Anomalie anomalie2 = new Anomalie(); anomalie2.setSujet("erreur de compilation"); anomalie2.setDescription("La complication du projet écoue à cause d'un bug"); anomalie2.setNomProjet("projet1"); projet1.add(anomalie2);
		persisterProjet(projet1);
		Projet projet2 = new Projet(); projet2.setNom("projet2");
		Anomalie anomalie3 = new Anomalie(); anomalie3.setSujet("anomalie3"); anomalie3.setDescription("Description de l'anomalie3"); anomalie2.setNomProjet("projet2"); projet2.add(anomalie3);
		persisterProjet(projet2);
		Projet projet3 = new Projet(); projet3.setNom("projet3");
		persisterProjet(projet3);
	}
	
	/*
	 * Les Getters permettant de lire la BDD
	 */

	@Lock(LockType.READ)
	public Utilisateur getUtilisateur(String login) {
		return bdd.find(Utilisateur.class, login);
	}
	
	@SuppressWarnings("unchecked")
	public List<Utilisateur> getUtilisateurs() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return bdd.createNativeQuery("SELECT * FROM UTILISATEUR",Utilisateur.class).getResultList();
	}

	@Lock(LockType.READ)
	public Projet getProjet(String nom) {
		return bdd.find(Projet.class, nom);
	}

	@Lock(LockType.READ)
	@SuppressWarnings("unchecked")
	public List<Projet> getProjets() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return bdd.createNativeQuery("select * from projet",Projet.class).getResultList();
	}

	@Lock(LockType.READ)
	public Anomalie getAnomalie(long id) {
		return bdd.find(Anomalie.class, id);
	}
	
	@Lock(LockType.READ)
	public List<Anomalie> getAnomaliesOfProject(String nomProjet) {
		Projet projet;
		List<Anomalie> anomalies = new ArrayList<Anomalie>();
		projet = getProjet(nomProjet);
		anomalies = projet.getAnomalies();
		return anomalies;
	}
	
	@Lock(LockType.READ)
	public Anomalie getAnomalieOfProject(String nomProjet,String sujetAnomalie) {
		Projet projet;
		List<Anomalie> anomalies = new ArrayList<Anomalie>();
		Anomalie anomalie = null;
		try{
			projet = getProjet(nomProjet);
			anomalies = projet.getAnomalies();
			for (Anomalie anomalieCourante : anomalies) {
				if(anomalieCourante.getSujet().equals(sujetAnomalie)){
					anomalie=anomalieCourante;
					break;
				}
			}
		}catch(Exception e){
			anomalie = null;
		}
		return anomalie;
	}

	@Lock(LockType.READ)
	@SuppressWarnings("unchecked")
	public List<Anomalie> getAnomalies() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return bdd.createNativeQuery("select * from anomalie",Anomalie.class).getResultList();
	}
	
	/*
	 * Les Méthodes permettant de supprimer des éléments de la BDD
	 */
	
	@Lock(LockType.WRITE)
	public void removeProjet(Projet projetAsupprimer) {
		bdd.remove(projetAsupprimer);
	}

	@Lock(LockType.WRITE)
	public void removeAnomalie(Anomalie anomalieAsupprimer) {
		bdd.remove(anomalieAsupprimer);
	}
	
	/*
	 * Les méthodes permettant d'écrire dans la BDD
	 */
	
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Utilisateur persisterUtilisateur(final Utilisateur utilisateur) {
        try{
        	bdd.persist(utilisateur);
        }catch(Exception e){
        	bdd.getTransaction().rollback();
        }
        return utilisateur;
    }

	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Projet persisterProjet(final Projet projet) {
        try{
        	bdd.persist(projet);
        }catch(Exception e){
        	bdd.getTransaction().rollback();
        }
        return projet;
    }
    
	public void persisterAnomalie(String nomProjet, Anomalie anomalie) {
		Projet projetCourant;
		projetCourant = getProjet(nomProjet);
		projetCourant.add(anomalie);
		persisterProjet(projetCourant);
	}
	
	/*
	 * Les tests nécessaires à la mise en persistance
	 */

	@Lock(LockType.READ)
	public boolean ProjetExisteDejaEnBase(Projet projet) {
		return(bdd.find(Projet.class, projet.getNom()) != null);
	}
	
	@Lock(LockType.READ)
	public boolean UtilisateurExisteDejaEnBase(String utilisateurLogin) {
		Utilisateur utilisateurEnBase;
		utilisateurEnBase = bdd.find(Utilisateur.class, utilisateurLogin);
		return(utilisateurEnBase != null);
	}

}
