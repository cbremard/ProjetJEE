package projetJEE.ejb;

import java.util.ArrayList;
import java.util.Date;
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
import projetJEE.modele.Note;
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
	
	@SuppressWarnings("deprecation")
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
		Anomalie anomalie1 = new Anomalie(); anomalie1.setSujet("anomalie1"); anomalie1.setDescription("Description de l'anomalie1"); anomalie1.setNomProjet("projet1"); projet1.add(anomalie1);
		Note note1 = new Note(); note1.setDescription("Detection d'un bug à la compilateion du projet1, et affectation de l'anomalie à Corentin");
		Anomalie anomalie2 = new Anomalie(); anomalie2.setSujet("erreur de compilation"); anomalie2.setDescription("La complication du projet écoue à cause d'un bug"); anomalie2.setNomProjet("projet1"); anomalie2.addNote(note1); anomalie2.setLoginUtilisateur("coco"); anomalie2.setEtatToAffectee(); projet1.add(anomalie2);
		persisterProjet(projet1);
		Projet projet2 = new Projet(); projet2.setNom("projet2");
		Note note2 = new Note(); note2.setDate(new Date(2013,9,1)); note2.setDescription("Affactation de l'anomalie à Corentin");
		Note note3 = new Note(); note3.setDate(new Date(2013,10,15)); note3.setDescription("La solution de l'anomalie à été trouvée");
		Note note4 = new Note(); note4.setDate(new Date(2013,11,30)); note4.setDescription("La solution de l'anomalie à été mise en place avec succés. Fin de l'anomalie.");
		Anomalie anomalie3 = new Anomalie(); anomalie3.setSujet("anomalie3"); anomalie3.setDescription("Description de l'anomalie3"); anomalie3.setNomProjet("projet2"); anomalie3.setLoginUtilisateur("coco"); anomalie3.addNote(note2); anomalie3.setEtatToAffectee(); anomalie3.addNote(note3); anomalie3.setEtatToResolue(); anomalie3.addNote(note4); anomalie3.setEtatToFermee(); projet2.add(anomalie3);
		persisterProjet(projet2);
		Projet projet3 = new Projet(); projet3.setNom("projet3");
		persisterProjet(projet3);
	}
	
	/*
	 * Les Getters permettant de lire la BDD
	 */
	
	/**
	 * Récupération d'un utilisateur par son login (correspond à la clé primaire en BDD)
	 * @param login : le login de l'utilisateur recherché
	 * @return Un Utilisateur en cas de succés, null en cas d'échec
	 */
	@Lock(LockType.READ)
	public Utilisateur getUtilisateur(String login) {
		return bdd.find(Utilisateur.class, login);
	}
	
	/**
	 * Récupération de l'ensemble des utilisateurs présent en BDD
	 * @return La liste de l'ensemble des utilisateurs présent en BDD 
	 */
	@SuppressWarnings("unchecked")
	public List<Utilisateur> getUtilisateurs() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return bdd.createNativeQuery("SELECT * FROM UTILISATEUR",Utilisateur.class).getResultList();
	}

	/**
	 * Récupérer un projet par son nom (correspond à la cléprimaire en BDD)
	 * @param nom : le nom du projet recherché
	 * @return Un projet en cas de succés, null sinon
	 */
	@Lock(LockType.READ)
	public Projet getProjet(String nom) {
		return bdd.find(Projet.class, nom);
	}

	/**
	 * Récupération de l'ensemble des projets présent en BDD
	 * @return La liste de l'ensemble des projets présent en BDD
	 */
	@Lock(LockType.READ)
	@SuppressWarnings("unchecked")
	public List<Projet> getProjets() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return bdd.createNativeQuery("select * from projet",Projet.class).getResultList();
	}

	/**
	 * Récupération d'une anomalie par son id (correspond à la clé primaire en BDD)
	 * @param id : l'identifiant de l'anomalie recherchée (généré automatique par le modèle JPA)
	 * @return Une anomalie en cas de succés, null sinon
	 */
	@Lock(LockType.READ)
	public Anomalie getAnomalie(long id) {
		return bdd.find(Anomalie.class, id);
	}

	/**
	 * Récupération de l'ensemble des anomalies associées à un projet donné
	 * @param nomProjet : nom du projet pour lequel on recherche les anomalies
	 * @return La liste des anomalies du projet donné
	 */
	@Lock(LockType.READ)
	public List<Anomalie> getAnomaliesOfProject(String nomProjet) {
		Projet projet;
		List<Anomalie> anomalies = new ArrayList<Anomalie>();
		projet = getProjet(nomProjet);
		anomalies = projet.getAnomalies();
		return anomalies;
	}

	/**
	 * Récupération d'une anomalie en fonction de son sujet et du nom du projet qui lui est associé.
	 * L'unicité de l'anomalie correspondant à un couple nomProjet/sujetAnomalie est vérifié à l'enregistrement d'une anomalie par la méthode projetJEE.ejb.GestionAnomalie.anomalieTestValidite(Anomalie, String, boolean).
	 * @param nomProjet : le nom du projet auquel l'anomalie recherché est associée
	 * @param sujetAnomalie : le sujet de l'anomalie recherchée
	 * @return Une anomalie en cas de succés, null sion
	 */
	@Lock(LockType.READ)
	public Anomalie getAnomalieOfProject(String nomProjet, String sujetAnomalie) {
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

	/**
	 * Récupération de l'ensemble des anomalies enregistrées en BDD.
	 * @return La liste de l'ensemble des anomalies présentent en BDD
	 */
	@Lock(LockType.READ)
	@SuppressWarnings("unchecked")
	public List<Anomalie> getAnomalies() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return bdd.createNativeQuery("select * from anomalie",Anomalie.class).getResultList();
	}
	
	/*
	 * Les Méthodes permettant de supprimer des éléments de la BDD
	 */

	/**
	 * Suppression de la BDD d'un projet.
	 * @param projetAsupprimer : le projet à supprimer de la BDD
	 */
	@Lock(LockType.WRITE)
	public void removeProjet(Projet projetAsupprimer) {
		bdd.remove(projetAsupprimer);
	}

	/**
	 * Suppression de la BDD d'une anomalie.
	 * @param anomalieAsupprimer : l'anomalie à supprimer de la BDD
	 */
	@Lock(LockType.WRITE)
	public void removeAnomalie(Anomalie anomalieAsupprimer) {
		bdd.remove(anomalieAsupprimer);
	}
	
	/*
	 * Les méthodes permettant d'écrire dans la BDD
	 */

	/**
	 * Enregistrer un utilisateur en BDD
	 * @param utilisateur : l'utilisateur à enregistrer en BDD
	 * @return L'utilisateur qui a été fournit en paramètre une fois enregisté en BDD
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

	/**
	 * Enregistrer un projet en BDD.
	 * @param projet : le projet à enregistrer en BDD
	 * @return Le projet qui a été fourni en paramètre une fois enregisté en BDD
	 */
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

	/**
	 * Enregistrement d'une anomalie dans la BDD
	 * @param nomProjet : nom du projet rattaché à l'anomalie
	 * @param anomalie : l'anomalie a enregistrer en BDD
	 * @return L'anomalie fournie en paramètre une fois enregistée en BDD
	 */
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Anomalie persisterAnomalie(String nomProjet, Anomalie anomalie) {
		try{
			Projet projetCourant;
			projetCourant = getProjet(nomProjet);
			projetCourant.add(anomalie);
			persisterProjet(projetCourant);
		}catch(Exception e){
        	bdd.getTransaction().rollback();
        }
        return anomalie;
	}
	
	/*
	 * Les tests nécessaires à la mise en persistance
	 */

	/**
	 * Test l'existence d'un projet dans la BDD
	 * @param projet : le projet potentiellement présent en BDD
	 * @return true si le projet en question est présent en BDD, false sinon
	 */
	@Lock(LockType.READ)
	public boolean ProjetExisteDejaEnBase(final Projet projet) {
		return(bdd.find(Projet.class, projet.getNom()) != null);
	}

	/**
	 * Test l'éxistence d'un utilisateur en BDD
	 * @param utilisateurLogin : le login de l'utilisateur potentiellement enregistré en BDD
	 * @return true si l'utilisateur en question est présent en BDD
	 */
	@Lock(LockType.READ)
	public boolean UtilisateurExisteDejaEnBase(final String utilisateurLogin) {
		Utilisateur utilisateurEnBase;
		utilisateurEnBase = bdd.find(Utilisateur.class, utilisateurLogin);
		return(utilisateurEnBase != null);
	}

}
