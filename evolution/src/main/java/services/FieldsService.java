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
import dto.responses.CustomResponse;

@Path("/fieldservice")
public class FieldsService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(FieldsService.class.getName());

	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setGameStatus(@FormParam("gameid") String gameid) {
		if (gameid.isEmpty()) {
			CustomResponse<String> fieldResponse = new CustomResponse<>(true,
					"Required parameters (gameid) is missing!", null);
			return Response.ok().entity(fieldResponse).build();

		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			boolean result = project.setGameField(connection, gameid, "1");
			if (!result) {
				CustomResponse<String> fieldResponse = new CustomResponse<>(true,
						"Unknown error occurred in game creation!", null);
				return Response.ok().entity(fieldResponse).build();
			} else {
				CustomResponse<String> fieldResponse = new CustomResponse<>(false, "", "1");
				Response response = Response.ok().entity(fieldResponse).build();
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
			CustomResponse<String> fieldResponse = new CustomResponse<>(true, "Required parameters (id) is missing!",
					null);
			return Response.ok().entity(fieldResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			FieldsDAO project = new FieldsDAO();
			String cells = project.getField(connection, id);
			CustomResponse<String> fieldResponse = new CustomResponse<>(false, "", cells);
			Response response = Response.ok().entity(fieldResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}
}
