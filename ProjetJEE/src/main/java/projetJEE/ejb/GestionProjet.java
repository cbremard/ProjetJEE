package projetJEE.ejb;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import projetJEE.modele.Projet;

@Stateless
public class GestionProjet {
	
	@EJB
	private Dao dao;

	/**
	 * Enregistrement d'un projet en BDD
	 * @param projet : le projet à enregistrer
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si le projet n'est pas correctement construit
	 */
	public Response addProjet(Projet projet, UriInfo uri) {
		String erreur;
		URI uriReponse;
		Response reponse;
		erreur = projetTestValidite(projet);
		if(erreur=="OK"){
			dao.persisterProjet(projet);
			uriReponse = uri.getBaseUriBuilder().path("projets").path(projet.getNom()).build();
			reponse = Response.created(uriReponse).build();
			}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	/**
	 * Modifier un projet
	 * @param newProjet : le projet contenant les modification à apporter
	 * @param nomOldProjet : nom du projet sur lequel porte les modifications
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si les modifcations ne peuvent pas être prises en compte
	 */
	public Response modifierProjet(final Projet newProjet, final String nomOldProjet, UriInfo uri) {
		Response reponse;
		Projet oldProjet;
		oldProjet = getProjet(nomOldProjet);
		if (oldProjet != null){
			reponse = addProjet(newProjet, uri);
			if(reponse.getStatus()==201){
				removeProjet(oldProjet);
			}
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+nomOldProjet+"\" n'hésite pas.").build();
		}
		return reponse;
	}

	/**
	 * Récupérer un projet par son nom (correspond à la cléprimaire en base de données)
	 * @param nom : le nom du projet recherché
	 * @return Un projet en cas de succès, null sinon
	 */
	public Projet getProjet(String nom) {
		return dao.getProjet(nom);
	}

	/**
	 * Récupération de l'ensemble des projets présent en base de données
	 * @return La liste de l'ensemble des projets présent en base de données
	 */
	public List<Projet> getProjets() {
		return dao.getProjets();
	}

	/**
	 * Suppression de la base de données d'un projet.
	 * @param projetAsupprimer : le projet à supprimer de la base de données
	 */
	private void removeProjet(Projet oldProjet) {
		dao.removeProjet(oldProjet);
	}

	/**
	 * Test la validité d'un projet
	 * @param projet : le projet à tester
	 * @return true si le projet est valide, false sinon
	 */
	private String projetTestValidite(Projet projet) {
		String resultat;
		if(projet.getNom().length()==0){
			resultat = "Veuillez renseigner un nom de projet";
		}else if(dao.ProjetExisteDejaEnBase(projet)){
			resultat = "Le projet \""+projet.getNom()+"\" est déjà présent en base de données.";
		}else{
			resultat = "OK";
		}
		return resultat;
	}

}
