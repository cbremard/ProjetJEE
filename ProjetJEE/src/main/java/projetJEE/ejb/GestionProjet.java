package javaEE.projetJEE.ejb;

import java.net.URI;
import java.util.List;

import javaEE.projetJEE.modele.Anomalie;
import javaEE.projetJEE.modele.AnomalieAffectation;
import javaEE.projetJEE.modele.Projet;
import javaEE.projetJEE.modele.Utilisateur;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless
public class GestionProjet {
	
	@EJB
	private DaoProjet bdd;

	public Response addProjet(Projet projet, UriInfo uri) {
		String erreur;
		URI uriReponse;
		Response reponse;
		erreur = projetTestValidite(projet);
		if(erreur=="OK"){
			bdd.persister(projet);
			uriReponse = uri.getAbsolutePathBuilder().path(projet.getNom()).build();
			reponse = Response.created(uriReponse).build();
			}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	public Response modifierProjet(final Projet newProjet, final String nomOldProjet, UriInfo uri) {
		Response reponse;
		Projet oldProjet;
		oldProjet= getProjet(nomOldProjet);
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

	public Response addAnomalie(Anomalie anomalie, String nomProjet, UriInfo uri) {
		String erreur;
		URI uriReponse;
		Response reponse;
		erreur = anomalieTestValidite(anomalie, nomProjet);
		if(erreur=="OK"){
			anomalie.setNomProjet(nomProjet);
			bdd.persisterAnomalie(nomProjet, anomalie);
			uriReponse = uri.getBaseUriBuilder().path("projets").path("anomalies").path(""+anomalie.getId()).build();
			reponse = Response.created(uriReponse).build();
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	public Response addUtilisateurToAnomalie(String login, String nomProjet, String sujet, UriInfo uri) {
		URI uriReponse;
		Response reponse=null;
		Anomalie anomalie;
		AnomalieAffectation anomalieAffectation = new AnomalieAffectation();
		if(bdd.find(Utilisateur.class, login) == null){
			reponse = Response.status(Response.Status.BAD_REQUEST).entity("L'utilisateur "+login+" n'hésite pas dans la base de bonnées").build();
		}else if(getAnomalieOfProject(nomProjet, sujet)==null){
			reponse = Response.status(Response.Status.BAD_REQUEST).entity("l'anomalie \""+sujet+"\" n'éxiste pas au sein du projet \""+nomProjet+"\".").build();
		}else{
			anomalie = getAnomalieOfProject(nomProjet, sujet);
			anomalieAffectation.setRefToUtilisateur(uri.getBaseUriBuilder().path("utilisateurs").path(login).build().toString());
			anomalie.setAffectation(anomalieAffectation);
			anomalie.setLoginUtilisateur(login);
			bdd.persisterAnomalie(nomProjet, anomalie);
			uriReponse = uri.getBaseUriBuilder().path("projets").path("anomalies").path(""+anomalie.getId()).build();
			reponse = Response.created(uriReponse).build();
		}
		return reponse;
	}

	public Projet getProjet(String nom) {
		return bdd.getProjet(nom);
	}

	public List<Projet> getProjets() {
		return bdd.getProjets();
	}

	private void removeProjet(Projet oldProjet) {
		bdd.removeProjet(oldProjet);
	}

	private String projetTestValidite(Projet projet) {
		String resultat;
		if(projet.getNom().length()==0){
			resultat = "Veuillez renseigner un nom de projet";
		}else if(bdd.existeDejaEnBase(projet)){
			resultat = "Le projet \""+projet.getNom()+"\" est déjà présent en base de données.";
		}else{
			resultat = "OK";
		}
		return resultat;
	}

	private String anomalieTestValidite(Anomalie anomalie, String nomProjet) {
		String resultat = "";
		List<Anomalie> anomalies;
		if(anomalie.getSujet().length()==0){
			resultat += "Veuillez renseigner le sujet de l'anomalie. ";
		}
		if(anomalie.getDescription().length()==0){
			resultat += "Veuillez renseigner la description de l'anomalie. ";
		}
		anomalies = getAnomaliesOfProject(nomProjet);
		for (Anomalie anomalieCourante : anomalies) {
			if(anomalieCourante.getNomProjet().equals(nomProjet) && anomalie.getSujet().equals(anomalieCourante.getSujet())){
				resultat += "Le sujet \""+anomalie.getSujet()+"\" a déjà été attribué à une anomalie du projet \""+nomProjet+"\". ";
				break;
			}
		}
		if(resultat.length()==0){
			resultat = "OK";
		}
		return resultat;
	}

	public Anomalie getAnomalie(long id) {
		return bdd.getAnomalie(id);
	}

	public List<Anomalie> getAnomaliesOfProject(String nomProjet) {
		return bdd.getAnomaliesOfProject(nomProjet);
	}

	public Anomalie getAnomalieOfProject(String nomProjet, String sujetAnomalie) {
		return bdd.getAnomalieOfProject(nomProjet, sujetAnomalie);
	}

}
