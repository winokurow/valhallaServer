package services;

import java.sql.Connection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;

import dao.Database;
import dao.LevelDAO;
import dto.Level;
import dto.responses.CustomResponse;

@Path("/levelservice")
public class LevelService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(LevelService.class.getName());

	@Path("{level}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGame(@PathParam("level") Integer level) throws JSONException {
		if (level == null) {
			CustomResponse<Level> levelResponse = new CustomResponse<>(true, "Required parameters (level) is missing!",
					null);
			return Response.ok().entity(levelResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			LevelDAO project = new LevelDAO();
			Level returnValue = project.getLevel(connection, level);
			CustomResponse<Level> levelResponse = new CustomResponse<>(false, "", returnValue);
			Response response = Response.ok().entity(levelResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}
}
