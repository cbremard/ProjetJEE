package projetJEE.ejb;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import projetJEE.modele.Anomalie;
import projetJEE.modele.AnomalieAffectation;
import projetJEE.modele.Note;
import projetJEE.modele.Projet;

@Stateless
public class GestionAnomalie {
	
	@EJB
	private Dao dao;

	public Response addAnomalie(Anomalie anomalie, String nomProjet, UriInfo uri) {
		String erreur;
		URI uriReponse;
		Response reponse;
		erreur = anomalieTestValidite(anomalie, nomProjet, true);
		if(erreur=="OK"){
			anomalie.setNomProjet(nomProjet);
			dao.persisterAnomalie(nomProjet, anomalie);
			uriReponse = uri.getBaseUriBuilder().path("projets").path("anomalies").path(""+anomalie.getId()).build();
			reponse = Response.created(uriReponse).build();
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	public Response modifierAnomalie(Anomalie anomalieModifie,
		String nomProjet, String sujetAncinneAnomalie, UriInfo uri) {
		String erreur;
		URI uriReponse;
		Response reponse;
		Projet projet;
		int index;
		long idToRemove = -1;
		erreur = anomalieTestValidite(anomalieModifie, nomProjet, false);
		if(erreur=="OK"){
			projet = dao.getProjet(nomProjet);
			if(projet != null){
				if(projet.getAnomalies() != null){
					for (index = 0; index<projet.getAnomalies().size(); index++) {
						if(projet.getAnomalies().get(index).getSujet().equals(sujetAncinneAnomalie)){
							idToRemove = projet.getAnomalies().get(index).getId();
							projet.getAnomalies().set(index, anomalieModifie);
							break;
						}
					}
					if(idToRemove!=-1){
						dao.persisterProjet(projet);
						dao.removeAnomalie(dao.getAnomalie(idToRemove));
						uriReponse = uri.getBaseUriBuilder().path("projets").path("anomalies").path(""+projet.getAnomalies().get(index).getId()).build();
						reponse = Response.created(uriReponse).build();
					}else{
						reponse = Response.status(Response.Status.BAD_REQUEST).entity("L'anomalie \""+sujetAncinneAnomalie+"\" n'est pas présente dans le projet \""+nomProjet+"\".").build();
					}
				}else{
					reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+ nomProjet+"\" ne contient pas d'anomalies.").build();
				}
			}else{
				reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+ nomProjet+"\" est inconnu.").build();
			}
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	public Response addUtilisateurToAnomalie(String login, String nomProjet, String sujetAnomalie, UriInfo uri) {
		URI uriReponse;
		Response reponse;
		Anomalie newAnomalie;
		AnomalieAffectation anomalieAffectation = new AnomalieAffectation();
		Projet projet;
		Note note = new Note();
		int index;
		long idToRemove = -1;
		if(dao.getUtilisateur(login) != null){
			projet = dao.getProjet(nomProjet);
			if(projet != null){
				if(projet.getAnomalies() != null){
					for (index = 0; index<projet.getAnomalies().size(); index++) {
						if(projet.getAnomalies().get(index).getSujet().equals(sujetAnomalie)){
							idToRemove = projet.getAnomalies().get(index).getId();
							newAnomalie = projet.getAnomalies().get(index);
							anomalieAffectation.setRefToUtilisateur(uri.getBaseUriBuilder().path("utilisateurs").path(login).build().toString());
							newAnomalie.setAffectation(anomalieAffectation);
							newAnomalie.setLoginUtilisateur(login);
							newAnomalie.setEtatToAffectee();
							note.setDescription("Changement d'état à l'état AFFECTEE puisque l'utilisateur \""+login+"\" a été affecté.");
							newAnomalie.addNote(note);
							projet.getAnomalies().set(index, newAnomalie);
							break;
						}
					}
					if(idToRemove!=-1){
						dao.persisterProjet(projet);
						dao.removeAnomalie(dao.getAnomalie(idToRemove));
						uriReponse = uri.getBaseUriBuilder().path("projets").path("anomalies").path(""+projet.getAnomalies().get(index).getId()).build();
						reponse = Response.created(uriReponse).build();
					}else{
						reponse = Response.status(Response.Status.BAD_REQUEST).entity("L'anomalie \""+sujetAnomalie+"\" n'est pas présente dans le projet \""+nomProjet+"\".").build();
					}
				}else{
					reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+ nomProjet+"\" ne contient pas d'anomalies.").build();
				}
			}else{
				reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+nomProjet+"\" n'hésite pas dans la base de bonnées").build();
			}
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity("L'utilisateur \""+login+"\" n'hésite pas dans la base de bonnées").build();
		}
		return reponse;
	}

	private String anomalieTestValidite(Anomalie anomalie, String nomProjet, boolean regarderSiLAnomalieExisteDeja) {
		String resultat = "";
		List<Anomalie> anomalies;
		if(anomalie.getSujet().length()==0){
			resultat += "Veuillez renseigner le sujet de l'anomalie. ";
		}
		if(anomalie.getDescription().length()==0){
			resultat += "Veuillez renseigner la description de l'anomalie. ";
		}
		if(regarderSiLAnomalieExisteDeja){
			anomalies = getAnomaliesOfProject(nomProjet);
			for (Anomalie anomalieCourante : anomalies) {
				if(anomalie.getSujet().equals(anomalieCourante.getSujet())){
					resultat += "Le sujet \""+anomalie.getSujet()+"\" est déjà utilisé par une anomalie du projet \""+nomProjet+"\". ";
					break;
				}
			}
		}
		if(resultat.length()==0){
			resultat = "OK";
		}
		return resultat;
	}

	public Anomalie getAnomalie(long id) {
		return dao.getAnomalie(id);
	}

	public List<Anomalie> getAnomaliesOfProject(String nomProjet) {
		return dao.getAnomaliesOfProject(nomProjet);
	}

	public Anomalie getAnomalieOfProject(String nomProjet, String sujetAnomalie) {
		return dao.getAnomalieOfProject(nomProjet, sujetAnomalie);
	}

	public Response changerEtatDuneAnomalie(String nomProjet,
			String sujetAnomalie, Note note, UriInfo uri,String newEtat) throws Exception {
		URI uriReponse;
		Response reponse;
		Projet projet;
		Anomalie newAnomalie;
		int index;
		long idToRemove = -1;
		projet = dao.getProjet(nomProjet);
		if(projet != null){
			if(projet.getAnomalies() != null){
				for (index = 0; index<projet.getAnomalies().size(); index++) {
					if(projet.getAnomalies().get(index).getSujet().equals(sujetAnomalie)){
						newAnomalie = projet.getAnomalies().get(index);
						idToRemove = newAnomalie.getId();
						switch(newEtat){
						case "AFFECTEE": newAnomalie.setEtatToAffectee();
                    	break;
						case "RESOLUE": newAnomalie.setEtatToResolue();
                    	break;
						case "FERMEE": newAnomalie.setEtatToFermee();
                    	break;
						default: throw new Exception("Invalid argument in projetJEE.ejb.gestionAnomalie.changerEtatDuneAnomalie");
						}
						newAnomalie.addNote(note);
						projet.getAnomalies().set(index, newAnomalie);
						break;
					}
				}
				if(idToRemove!=-1){
					dao.persisterProjet(projet);
					dao.removeAnomalie(dao.getAnomalie(idToRemove));
					uriReponse = uri.getBaseUriBuilder().path("projets").path("anomalies").path(""+projet.getAnomalies().get(index).getId()).build();
					reponse = Response.created(uriReponse).build();
				}else{
					reponse = Response.status(Response.Status.BAD_REQUEST).entity("L'anomalie \""+sujetAnomalie+"\" n'est pas présente dans le projet \""+nomProjet+"\".").build();
				}
			}else{
				reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+ nomProjet+"\" ne contient pas d'anomalies.").build();
			}
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity("Le projet \""+ nomProjet+"\" est inconnu.").build();
		}
		return reponse;
	}

}
