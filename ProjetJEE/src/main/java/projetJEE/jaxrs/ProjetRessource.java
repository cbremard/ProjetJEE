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

	@POST
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Consumes(MediaType.APPLICATION_XML)
	public Response creerProjet(final Projet projet, @Context final UriInfo uri){
		System.out.println("Appel du service de création d'un projet");
		return gestionProjet.addProjet(projet, uri);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}")
	public Response modifierProjet(final Projet newProjet, @PathParam("nom") final String nomOldProjet, @Context final UriInfo uri){
		System.out.println("Appel du service de modification d'un projet");
		return gestionProjet.modifierProjet(newProjet, nomOldProjet, uri);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies")
	public Response addAnomalie(final Anomalie anomalie, @PathParam("nom") final String nomProjet, @Context final UriInfo uri){
		System.out.println("Appel du service d'ajout d'une anomalie à un projet");
		return gestionAnomalie.addAnomalie(anomalie, nomProjet, uri);
	}
	
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
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	@Path("{nom}/anomalies/{sujet}/addUtilisateur")
	public Response addUtilisateurToAnomalie(@QueryParam("login")  final String login, @PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujet, @Context final UriInfo uri){
		System.out.println("Appel du service d'ajout d'un utilisateur à une anomalie d'un projet");
		return gestionAnomalie.addUtilisateurToAnomalie(login, nomProjet, sujet, uri);
	}

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{nom}/anomalies")
    public List<Anomalie> getAnomalies(@PathParam("nom") final String nomProjet) {
        System.out.println("Appel du service de récupération des anomalies d'un projet");
        return gestionAnomalie.getAnomaliesOfProject(nomProjet);
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{nom}/anomalies/{sujet}")
    public Anomalie getAnomalie(@PathParam("nom") final String nomProjet, @PathParam("sujet") final String sujetAnomalie) {
        System.out.println("Appel du service de récupération de l'anomalie \""+sujetAnomalie+"\" du projet \""+nomProjet+"\"");
        return gestionAnomalie.getAnomalieOfProject(nomProjet, sujetAnomalie);
    }
	
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{nom}")
    public Projet getProjet(@PathParam("nom") final String nom) {
        System.out.println("Appel du service de récupération d'un projet par son nom");
        return gestionProjet.getProjet(nom);
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public List<Projet> getProjets() {
        System.out.println("Appel du service de récupération de l'ensemble des projets");
        return gestionProjet.getProjets();
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("anomalies/{id}")
    public Anomalie getAnomalies(@PathParam("id") final long id) {
        System.out.println("Appel du service de récupération des anomalies d'un projet");
        return gestionAnomalie.getAnomalie(id);
    }
}
