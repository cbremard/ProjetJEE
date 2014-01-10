package projetJEE.jaxrs;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import projetJEE.ejb.GestionUtilisateur;
import projetJEE.modele.Utilisateur;

@DeclareRoles({"ADMIN","USER"})
@Path("utilisateurs")
public class UtilisateurRessource {
	
	@EJB
	private GestionUtilisateur gestionUtilisateur;

	/**
	 * Stockage d'un utilisateur dans la base de données
	 * @param utilisateur : l'utilsateur à enregistrer en base de données
	 * @return code 201 en cas de succès, 400 si l'utilisateur n'est pas correctement construit
	 */
	@POST
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Consumes(MediaType.APPLICATION_XML)
	public Response creerUtilisateur(final Utilisateur utilisateur, @Context final UriInfo uri, @Context SecurityContext security){
		System.out.println("Appel du service de création d'un utilisateur");
		if (security.isUserInRole("ADMIN")){
			return gestionUtilisateur.addUtilisateur(utilisateur, uri);
		}else{
			return Response.status(Response.Status.UNAUTHORIZED).entity("L'utilisateur \""+security.getUserPrincipal().getName()+"\" ne peut pas accéder à ce service réservé aux administrateurs").build();
		}
	}

	/**
	 * Récupération d'une liste paginée des utilisateurs enregistrés en BDD
	 * @param page : la page à afficher
	 * @param nbItems : le nombre d'utilisateur à afficher
	 * @param uri : l'uri ayant parmit d'accéder à ce service
	 * @return La liste paginée des utilisateurs enregistrés en BDD
	 */
	@RolesAllowed("ADMIN")
    @GET
    @Produces({MediaType.APPLICATION_XML})
	public Response getUtilisateur(@DefaultValue("1") @QueryParam("page")  final int page, @DefaultValue("10") @QueryParam("nbItems")  final int nbItems,  @Context final UriInfo uri, @Context SecurityContext security) {
		System.out.println("Appel du service de récupération des utilisateurs");
		if(security.isUserInRole("ADMIN")){
			return gestionUtilisateur.getUtilisateurs(page, nbItems, uri);
		}else{
			return Response.status(Response.Status.UNAUTHORIZED).entity("L'utilisateur \""+security.getUserPrincipal().getName()+"\" ne peut pas accéder à ce service réservé aux administrateurs").build();
		}
	}

	/**
	 * Récupération d'un utilisateur par son login (correspond à la clé primaire en base de données)
	 * @param login : le login de l'utilisateur recherché
	 * @return Un Utilisateur en cas de succès, null en cas d'échec
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{login}")
    public Response getUtilisateur(@PathParam("login") final String login) {
        System.out.println("Appel du service de récupération d'un utilisateur par son login");
        return gestionUtilisateur.getUtilisateur(login);
    }

	/**
	 * Récupération de la liste des anomalies associées à un utilisateur
	 * @param login : le login de l'utilisateur sur lequel porte les recherches
	 * @return la liste des anomalies associées à l'utilisateur en question 
	 */
    @GET
    @Produces({MediaType.APPLICATION_XML})
    @Path("{login}/anomalies")
    public Response getAnomaliesDeUtilisateur(@PathParam("login") final String login, @Context SecurityContext security) {
        System.out.println("Appel du service de récupération des anomalies affectées à un utilisateur");
        if(security.isUserInRole("USER")){
        	return gestionUtilisateur.getAnomaliesDeUtilisateur(login);
        }else{
			return Response.status(Response.Status.UNAUTHORIZED).entity("L'utilisateur \""+security.getUserPrincipal().getName()+"\" ne peut pas accéder à ce service réservé aux utilisateurs du système").build();
		}
    }
    

}
