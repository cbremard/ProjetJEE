package projetJEE.jaxrs;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import projetJEE.ejb.GestionAnomalie;
import projetJEE.ejb.GestionProjet;
import projetJEE.modele.Anomalie;
import projetJEE.modele.Note;
import projetJEE.modele.Projet;

@Path("projets")
public class ProjetRessource {

	@EJB
	private GestionProjet gestionProjet;
	@EJB
	private GestionAnomalie gestionAnomalie;

	/**
	 * Enregistrement d'un projet en BDD
	 * @param projet : le projet à enregistrer
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si le projet n'est pas correctement construit
	 */
	@POST
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Consumes(MediaType.APPLICATION_XML)
	public Response creerProjet(final Projet projet, @Context final UriInfo uri){
		System.out.println("Appel du service de création d'un projet");
		return gestionProjet.addProjet(projet, uri);
	}

	/**
	 * Modifier un projet
	 * @param newProjet : le projet contenant les modification à apporter
	 * @param nomOldProjet : nom du projet sur lequel porte les modifications
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si les modifcations ne peuvent pas être prises en compte
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}")
	public Response modifierProjet(final Projet newProjet, @PathParam("nom") final String nomOldProjet, @Context final UriInfo uri){
		System.out.println("Appel du service de modification d'un projet");
		return gestionProjet.modifierProjet(newProjet, nomOldProjet, uri);
	}

	/**
	 * Enregistrement en BDD d'une anomalie associée à un projet.
	 * @param anomalie : l'anomalie à enregistrer en BDD
	 * @param nomProjet : nom du projet associée à l'anomalie
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si l'anomlie n'est pas correctement formée
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies")
	public Response addAnomalie(final Anomalie anomalie, @PathParam("nom") final String nomProjet, @Context final UriInfo uri){
		System.out.println("Appel du service d'ajout d'une anomalie à un projet");
		return gestionAnomalie.addAnomalie(anomalie, nomProjet, uri);
	}

	/**
	 * Modifier une anomalie déjà présente en BDD
	 * @param anomalieModifie : la nouvelle anomalie contenant les modifications
	 * @param nomProjet : le nom du projet associé
	 * @param sujetAncinneAnomalie : le sujet de l'anomalie a modifier
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si les modifications a apporter ne sont pas valides
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies/{sujet}")
	public Response modifierAnomalie(final Anomalie anomalieModifie,
									@PathParam("nom") final String nomProjet,
									@PathParam("sujet") final String sujetAncinneAnomalie,
									@Context final UriInfo uri){
		System.out.println("Appel du service de modification d'une anomalie d'un projet");
		return gestionAnomalie.modifierAnomalie(anomalieModifie, nomProjet, sujetAncinneAnomalie, uri);
	}

	/**
	 * Passer une anomalie à l'état AFFECTEE
	 * @param nomProjet : le nom du projet associé à l'anomalie à modifier
	 * @param sujetAnomalie : le sujet de l'anomalie à modifier
	 * @param note : la note à ajouter lors de ce changement d'état
	 * @param uri : l'URI ayant parmit d'accéder à ce service 
	 * @return code 201 en cas de succès, 400 sinon
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies/{sujet}/AFFECTEE")
	public Response changeEtatToAffectee(final Note note, @PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujetAnomalie, @Context final UriInfo uri){
		System.out.println("Appel du service de modification de l'état d'une anomalie d'un projet pour un état AFFECTEE");
		try {
			return gestionAnomalie.changerEtatDuneAnomalie(nomProjet, sujetAnomalie, note, uri, "AFFECTEE");
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	/**
	 * Passer une anomalie à l'état RESOLUE
	 * @param nomProjet : le nom du projet associé à l'anomalie à modifier
	 * @param sujetAnomalie : le sujet de l'anomalie à modifier
	 * @param note : la note à ajouter lors de ce changement d'état
	 * @param uri : l'URI ayant parmit d'accéder à ce service 
	 * @return code 201 en cas de succès, 400 sinon
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies/{sujet}/RESOLUE")
	public Response changeEtatToResolue(final Note note, @PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujetAnomalie, @Context final UriInfo uri){
		System.out.println("Appel du service de modification de l'état d'une anomalie d'un projet pour un état RESOLUE");
		try {
			return gestionAnomalie.changerEtatDuneAnomalie(nomProjet, sujetAnomalie, note, uri, "RESOLUE");
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	/**
	 * Passer une anomalie à l'état FERMEE
	 * @param nomProjet : le nom du projet associé à l'anomalie à modifier
	 * @param sujetAnomalie : le sujet de l'anomalie à modifier
	 * @param note : la note à ajouter lors de ce changement d'état
	 * @param uri : l'URI ayant parmit d'accéder à ce service 
	 * @return code 201 en cas de succès, 400 sinon
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies/{sujet}/FERMEE")
	public Response changeEtatToFermee(final Note note, @PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujetAnomalie, @Context final UriInfo uri){
		System.out.println("Appel du service de modification de l'état d'une anomalie d'un projet pour un état FERMEE");
		try {
			return gestionAnomalie.changerEtatDuneAnomalie(nomProjet, sujetAnomalie, note, uri,"FERMEE");
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	/**
	 * Affectatiion d'un utilisateur à une anomalie
	 * @param login : le login de l'utilisateur à affecter à l'anomalie souhaitée
	 * @param nomProjet : le nom du projet auquel l'anomalie est rattachée
	 * @param sujetAnomalie : le sujet de l'anomalie
	 * @param uri : l'URI ayant parmit d'accéder à ce service
	 * @return code 201 en cas de succès, 400 si la manipaluation n'a pas était correctement initialisé
	 */
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies/{sujet}/addUtilisateur")
	public Response addUtilisateurToAnomalie(@QueryParam("login")  final String login, @PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujet, @Context final UriInfo uri){
		System.out.println("Appel du service d'ajout d'un utilisateur à une anomalie d'un projet");
		return gestionAnomalie.addUtilisateurToAnomalie(login, nomProjet, sujet, uri);
	}

	/**
	 * Récupération de l'ensemble des anomalies associées à un projet donné
	 * @param nomProjet : nom du projet pour lequel on recherche les anomalies
	 * @return La liste des anomalies du projet donné
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{nom}/anomalies")
    public List<Anomalie> getAnomalies(@PathParam("nom") final String nomProjet) {
        System.out.println("Appel du service de récupération des anomalies d'un projet");
        return gestionAnomalie.getAnomaliesOfProject(nomProjet);
    }

	/**
	 * Récupération d'une anomalie en fonction de son sujet et du nom du projet qui lui est associé.
	 * L'unicité de l'anomalie correspondant à un couple nomProjet/sujetAnomalie est vérifié à l'enregistrement d'une anomalie par la méthode projetJEE.ejb.GestionAnomalie.anomalieTestValidite(Anomalie, String, boolean).
	 * @param nomProjet : le nom du projet auquel l'anomalie recherché est associée
	 * @param sujetAnomalie : le sujet de l'anomalie recherchée
	 * @return Une anomalie en cas de succès, null sion
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{nom}/anomalies/{sujet}")
    public Anomalie getAnomalie(@PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujetAnomalie) {
        System.out.println("Appel du service de récupération de l'anomalie \""+sujetAnomalie+"\" du projet \""+nomProjet+"\"");
        return gestionAnomalie.getAnomalieOfProject(nomProjet, sujetAnomalie);
    }

	/**
	 * Récupérer un projet par son nom (correspond à la cléprimaire en base de données)
	 * @param nom : le nom du projet recherché
	 * @return Un projet en cas de succès, null sinon
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{nom}")
    public Projet getProjet(@PathParam("nom") final String nom) {
        System.out.println("Appel du service de récupération d'un projet par son nom");
        return gestionProjet.getProjet(nom);
    }

	/**
	 * Récupération de l'ensemble des projets présent en base de données
	 * @return La liste de l'ensemble des projets présent en base de données
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Projet> getProjets() {
        System.out.println("Appel du service de récupération de l'ensemble des projets");
        return gestionProjet.getProjets();
    }

	/**
	 * Récupération d'une anomalie par son id (correspond à la clé primaire en base de données)
	 * @param id : l'identifiant de l'anomalie recherchée (généré automatique par le modèle JPA)
	 * @return Une anomalie en cas de succès, null sinon
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("anomalies/{id}")
    public Anomalie getAnomalies(@PathParam("id") final long id) {
        System.out.println("Appel du service de récupération des anomalies d'un projet");
        return gestionAnomalie.getAnomalie(id);
    }
}
