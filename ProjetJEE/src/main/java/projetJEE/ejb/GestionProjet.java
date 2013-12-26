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

	public Projet getProjet(String nom) {
		return dao.getProjet(nom);
	}

	public List<Projet> getProjets() {
		return dao.getProjets();
	}

	private void removeProjet(Projet oldProjet) {
		dao.removeProjet(oldProjet);
	}

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
