package projetJEE.ejb;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import projetJEE.modele.Anomalie;
import projetJEE.modele.Utilisateur;
import projetJEE.modele.UtilisateurListe;


@Stateless
public class GestionUtilisateur {
	
	@EJB
	private Dao dao;
	
	/**
	 * Stockage d'un utilisateur dans la base de données
	 * @param utilisateur : l'utilsateur à enregistrer en base de données
	 * @return code 201 en cas de succès, 400 si l'utilisateur n'est pas correctement construit
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
		}else{
			reponse = Response.status(Response.Status.BAD_REQUEST).entity(erreur).build();
		}
		return reponse;
	}

	/**
	 * Récupération d'une liste paginée des utilisateurs enregistrés en BDD
	 * @param page : la page à afficher
	 * @param nbItems : le nombre d'utilisateur à afficher
	 * @param uri : l'uri ayant parmit d'accéder à ce service
	 * @return La liste paginée des utilisateurs enregistrés en BDD
	 */
	public UtilisateurListe getUtilisateurs(final int page, final int nbItems, final UriInfo uri) {
		List<Utilisateur> utilisateurs;
		UtilisateurListe utilisateursPartiel;
		mettreAjourListeDesAnomaliesDesUtilisateurs();
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
			if(nbItems*page < utilisateurs.size()){
				utilisateursPartiel.setPageSuivante(uri.getAbsolutePath()+"?page="+(page+1)+"&nbItems="+nbItems);
			}
		}
		return utilisateursPartiel;
	}

	/**
	 * Récupération d'un utilisateur par son login (correspond à la clé primaire en base de données)
	 * @param login : le login de l'utilisateur recherché
	 * @return Un Utilisateur en cas de succès, null en cas d'échec
	 */
	public Utilisateur getUtilisateur(String login) {
		mettreAjourListeDesAnomaliesDesUtilisateurs();
		return dao.getUtilisateur(login);
	}

	/**
	 * Récupération de la liste des anomalies associées à un utilisateur
	 * @param login : le login de l'utilisateur sur lequel porte les recherches
	 * @return la liste des anomalies associées à l'utilisateur en question 
	 */
	public List<Anomalie> getAnomaliesDeUtilisateur(String login) {
		Utilisateur utilisateur;
		List<Anomalie> anomalies = new ArrayList<Anomalie>();
		utilisateur = dao.getUtilisateur(login);
		if(utilisateur != null){
			mettreAjourListeDesAnomaliesDesUtilisateurs();
			anomalies = utilisateur.getListeAnomalies();
		}
		return anomalies;
	}

	/**
	 * Mise à jour de la liste des anomalies de chaque utilisateur.
	 * 
	 * Les utilisateurs et les projets sont tout deux reliés aux anomalies. 
	 * Or l'application ne maintient à jour que la liaison projets-anomalies.
	 * Ainsi, certains modifications de la liaison projets-anomalies ne vont
	 * pas mettre à jour la liaison utilisateurs-anomalies.
	 * C'est grâce à cette méthode, appelée à chaque manipulation d'un utilisateur,
	 * Que l'on s'assure que les 2 liaisons projets-anomalies et utilisateurs-anomalies
	 * sont concordantes. 
	 */
	private void mettreAjourListeDesAnomaliesDesUtilisateurs() {
		List<Anomalie> anomalies = dao.getAnomalies();
		List<Utilisateur> utilisateurs = dao.getUtilisateurs();
		Utilisateur utilisateur;
		if(utilisateurs!=null){
			for (Utilisateur utilisateurCourant : utilisateurs) {
				utilisateurCourant.setListeAnomalies(new ArrayList<Anomalie>());
				dao.persisterUtilisateur(utilisateurCourant);
			}
		}
		if(anomalies != null){
			for (Anomalie anomalie : anomalies) {
				try{
					utilisateur = dao.getUtilisateur(anomalie.getLoginUtilisateur());
					utilisateur.add(anomalie);
					dao.persisterUtilisateur(utilisateur);
				}catch(Exception e){
					System.err.println("Echec lors de la mise à jour des anomalies de l'utilisateur \""+anomalie.getLoginUtilisateur()+"\"");
				}
			}
		}
	}
	
	/**
	 * Test si l'utilisateur est correctement construit pour être mis en base de données
	 * @param utilisateur : l'utilisateur sur lequel portent les tests
	 * @return	"OK" Si l'utilsateur est correctement construit et peut être persisté en base de données
	 * 			Une description de l'erreur si l'utilisateur n'est pas correct.
	 */
	private String utilisateurTestValidite(Utilisateur utilisateur) {
		String resultat = "";
		if(utilisateur.getLogin().length()==0){
			resultat += "Le login du nouvel utilisateur doit être renseigné. ";
		}else if(dao.UtilisateurExisteDejaEnBase(utilisateur.getLogin())){
			resultat += "Le login "+utilisateur.getLogin()+" éxiste est déjà utilisé. ";
		}
		if(utilisateur.getNom().length()==0){
			resultat += "Le nom du nouvel utilisateur doit être renseigné. ";
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
		if(resultat.length()==0){
			resultat = "OK";
		}
		return resultat;
	}

	/**
	 * Test la validité d'un email
	 * @param email : l'email à tester
	 * @return true si l'email est valide, false sinon
	 */
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
	

}
