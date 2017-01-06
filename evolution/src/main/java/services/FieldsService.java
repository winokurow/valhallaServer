package services;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;

import dao.Database;
import dao.FieldsDAO;
import dao.GameDAO;
import dto.GameResponse;
import dto.StringResponse;

@Path("/fieldservice")
public class FieldsService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(FieldsService.class.getName());

	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setGameStatus(@FormParam("fieldid") String fieldid) {
		if (fieldid.isEmpty()) {
			GameResponse userResponse = new GameResponse(true, "Required parameters (fieldid) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			boolean result = project.setGameField(connection, fieldid, "1");
			if (!result) {
				GameResponse gameResponse = new GameResponse(true, "Unknown error occurred in game creation!", null);
				return Response.ok().entity(gameResponse).build();
			} else {
				StringResponse gameResponse = new StringResponse(false, "", "1");
				Response response = Response.ok().entity(gameResponse).build();
				return response;
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	@Path("{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGame(@PathParam("id") String id) throws JSONException {
		if (id == null) {
			StringResponse response = new StringResponse(true, "Required parameters (id) is missing!", null);
			return Response.ok().entity(response).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			FieldsDAO project = new FieldsDAO();
			String cells = project.getField(connection, id);
			StringResponse res = new StringResponse(false, "", cells);
			Response response = Response.ok().entity(res).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}
}
