package javaEE.projetJEE.ejb;

import java.util.List;

import javaEE.projetJEE.modele.Utilisateur;

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

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DaoUtilisateur {

	@PersistenceContext
	private EntityManager em;
	
	@PostConstruct
    public void initialisation() {
		Utilisateur utilisateur1 = new Utilisateur();
		utilisateur1.setLogin("coco");
		utilisateur1.setPrenom("Corentin");
		utilisateur1.setNom("Brémard");
		utilisateur1.setMotDePasse("coco");
		utilisateur1.setEmail("corentin.bremard@eleve.ensai.fr");
		utilisateur1.setAdmin(true);
		persister(utilisateur1);
		Utilisateur utilisateur2 = new Utilisateur();
		utilisateur2.setLogin("nyfa");
		utilisateur2.setPrenom("Fanny");
		utilisateur2.setNom("Thomas");
		utilisateur2.setMotDePasse("nyfa");
		utilisateur2.setEmail("fanny.thomas@eleve.ensai.fr");
		utilisateur2.setAdmin(true);
		persister(utilisateur2);
		Utilisateur utilisateur3 = new Utilisateur();
		utilisateur3.setLogin("michu");
		utilisateur3.setPrenom("Madame");
		utilisateur3.setNom("Michu");
		utilisateur3.setMotDePasse("michu");
		utilisateur3.setEmail("madame.michu@email.com");
		utilisateur3.setAdmin(false);
		persister(utilisateur3);
		Utilisateur utilisateur4 = new Utilisateur();
		utilisateur4.setLogin("login1");
		utilisateur4.setPrenom("Prenom1");
		utilisateur4.setNom("Nom1");
		utilisateur4.setMotDePasse("login1");
		utilisateur4.setEmail("prenom1.nom1@email.truc");
		utilisateur4.setAdmin(false);
		persister(utilisateur4);
		Utilisateur utilisateur5 = new Utilisateur();
		utilisateur5.setLogin("login2");
		utilisateur5.setPrenom("Prenom2");
		utilisateur5.setNom("Nom2");
		utilisateur5.setMotDePasse("login1");
		utilisateur5.setEmail("prenom2.nom2@email.chose");
		utilisateur5.setAdmin(false);
		persister(utilisateur5);
		Utilisateur utilisateur6 = new Utilisateur();
		utilisateur6.setLogin("login3");
		utilisateur6.setPrenom("Prenom3");
		utilisateur6.setNom("Nom3");
		utilisateur6.setMotDePasse("login3");
		utilisateur6.setEmail("prenom3.nom3@email.machin");
		utilisateur6.setAdmin(false);
		persister(utilisateur6);
		Utilisateur utilisateur7 = new Utilisateur();
		utilisateur7.setLogin("login4");
		utilisateur7.setPrenom("Prenom4");
		utilisateur7.setNom("Nom4");
		utilisateur7.setMotDePasse("login4");
		utilisateur7.setEmail("prenom4.nom4@email.bidule");
		utilisateur7.setAdmin(false);
		persister(utilisateur7);
		Utilisateur utilisateur8 = new Utilisateur();
		utilisateur8.setLogin("login5");
		utilisateur8.setPrenom("Prenom5");
		utilisateur8.setNom("Nom5");
		utilisateur8.setMotDePasse("login5");
		utilisateur8.setEmail("prenom5.nom5@email.uk");
		utilisateur8.setAdmin(false);
		persister(utilisateur8);
		
	}
	
    
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Utilisateur persister(final Utilisateur utilisateur) {
        try{
        	em.persist(utilisateur);
        }catch(Exception e){
        	em.getTransaction().rollback();
        }
        return utilisateur;
    }
	
	public Utilisateur getUtilisateur(String login) {
		return em.find(Utilisateur.class, login);
	}
	
	@SuppressWarnings("unchecked")
	public List<Utilisateur> getUtilisateurs() {
		//TODO Vérifier s'il n'y a pas d'autre moyens. Le but étant de ne pas avoir besoin d'écrire une requête SQL.
		return em.createNativeQuery("SELECT * FROM UTILISATEUR",Utilisateur.class).getResultList();
	}

	public boolean existeDejaEnBase(String utilisateurLogin) {
		Utilisateur utilisateurEnBase;
		utilisateurEnBase = em.find(Utilisateur.class, utilisateurLogin);
		return(utilisateurEnBase != null);
	}
	

}
