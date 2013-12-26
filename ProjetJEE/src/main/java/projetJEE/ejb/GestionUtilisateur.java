package projetJEE.ejb;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import projetJEE.modele.Utilisateur;
import projetJEE.modele.UtilisateurListe;


@Stateless
public class GestionUtilisateur {
	
	@EJB
	private Dao dao;
	
	/**
	 * Stockage d'un utilisateur dans la base de données
	 * @param utilisateur : l'utilsateur à persister
	 * @return l'utilisateur fourni en paramètre
	 */
	public Response addUtilisateur(final Utilisateur utilisateur, final UriInfo uri){
		String erreur;
		URI uriReponse;
		Response reponse;
		
		erreur = utilisateurTestValidite(utilisateur);
		if(erreur=="OK"){
			dao.persisterUtilisateur(utilisateur);
			uriReponse = uri.getAbsolutePathBuilder().path(utilisateur.getLogin()).build();
			reponse = Response.created(uriReponse).build();
		}else if(erreur=="Utilisateur non habilité"){
			reponse = Response.status(401).entity("Utilisateur non habilité").build();
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	public UtilisateurListe getUtilisateurs(final int page, final int nbItems, final UriInfo uri) {
		List<Utilisateur> utilisateurs;
		UtilisateurListe utilisateursPartiel;
		utilisateurs = dao.getUtilisateurs();
		utilisateursPartiel = new UtilisateurListe();
		if(page>0 && nbItems>0 && nbItems*(page-1)<utilisateurs.size()){
			for (int i = nbItems*(page-1); i < Math.min(utilisateurs.size(),nbItems*page); i++) {
				utilisateursPartiel.add(utilisateurs.get(i));
			}
			utilisateursPartiel.setPage(page);
			if(page>1){
				utilisateursPartiel.setPagePrecedente(uri.getAbsolutePath()+"?page="+(page-1)+"&nbItems="+nbItems);
			}
			if(nbItems*page <= utilisateurs.size()){
				utilisateursPartiel.setPageSuivante(uri.getAbsolutePath()+"?page="+(page+1)+"&nbItems="+nbItems);
			}
		}
		return utilisateursPartiel;
	}
	
	/**
	 * Test si l'utilisateur est correctement construit pour être mis en base de données
	 * @param utilisateur : l'utilisateur sur lequel portent les tests
	 * @return	"OK" Si l'utilsateur est correctement construit et peut être persisté en base de données
	 * 			Une description de l'erreur si l'utilisateur n'est pas correct.
	 */
	private String utilisateurTestValidite(Utilisateur utilisateur) {
		String resultat = "";
		if(utilisateur.getNom().length()==0){
			resultat += "Le nom de du nouvel utilisateur doit être renseigné. ";
		}
		if(utilisateur.getPrenom().length()==0){
			resultat += "Le prénom du nouvel utilisateur doit être renseigné. ";
		}
		if(utilisateur.getEmail().length()==0){
			resultat += "L'email du nouvel utilisateur doit être renseigné. ";
		}else if(!emailTestValidite(utilisateur.getEmail())){
			resultat += "L'email renseigné n'est pas valide. ";
		}
		if(utilisateur.getMotDePasse().length()==0){
			resultat += "Le mot de passe du nouvel utilisateur doit être renseigné. ";
		}else if(utilisateur.getMotDePasse().length()<6){
			resultat += "Le mot de passe du nouvel utilisateur doit dépasser 6 caractères. ";
		}
		if(dao.UtilisateurExisteDejaEnBase(utilisateur.getLogin())){
			resultat = "Utilisateur non habilité";
		}
		if(resultat.length()==0){
			resultat = "OK";
		}
		return resultat;
	}


	private boolean emailTestValidite(String email) {
		boolean resultat;
		int indexDeLarobase;
		indexDeLarobase = email.indexOf("@");
		resultat = indexDeLarobase>0;
		if(resultat){
			resultat = email.substring(indexDeLarobase).contains(".");
		}
		return resultat;
	}

	public Utilisateur getUtilisateur(String login) {
		return dao.getUtilisateur(login);
	}
	

}
