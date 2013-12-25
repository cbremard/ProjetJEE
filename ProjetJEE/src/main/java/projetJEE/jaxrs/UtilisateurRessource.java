package javaEE.projetJEE.jaxrs;

import javaEE.projetJEE.ejb.GestionUtilisateur;
import javaEE.projetJEE.modele.Utilisateur;
import javaEE.projetJEE.modele.UtilisateurListe;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

@Path("utilisateurs")
public class UtilisateurRessource {
	
	@EJB
	private GestionUtilisateur gestionUtilisateur;
	
	@POST
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Consumes(MediaType.APPLICATION_XML)
	public Response creerUtilisateur(final Utilisateur utilisateur, @Context final UriInfo uri){
		System.out.println("Appel du service de création d'un utilisateur");
		return gestionUtilisateur.addUtilisateur(utilisateur, uri);
	}

	
    @GET
    @Produces({MediaType.APPLICATION_XML})
    public UtilisateurListe getUtilisateur(@DefaultValue("1") @QueryParam("page")  final int page, @DefaultValue("10") @QueryParam("nbItems")  final int nbItems,  @Context final UriInfo uri) {
        System.out.println("Appel du service de récupération des utilisateurs");
        return gestionUtilisateur.getUtilisateurs(page, nbItems, uri);
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{login}")
    public Utilisateur getUtilisateur(@PathParam("login") final String login) {
        System.out.println("Appel du service de récupération d'un utilisateur par son login");
        return gestionUtilisateur.getUtilisateur(login);
    }
    

}
