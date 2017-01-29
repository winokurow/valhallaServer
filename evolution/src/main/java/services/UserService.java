package services;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;

import dao.Database;
import dao.UserDAO;
import dto.Game;
import dto.User;
import dto.responses.CustomResponse;

@Path("/userservice")
public class UserService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(UserService.class.getName());

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(@FormParam("email") String email, @FormParam("password") String password) {
		if (email.isEmpty() || password.isEmpty()) {
			CustomResponse<User> userResponse = new CustomResponse<>(true,
					"Required parameters (email or password) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			UserDAO project = new UserDAO();
			User user = project.getUserByEmailAndPassword(connection, email, password);
			if (user != null) {
				CustomResponse<User> userResponse = new CustomResponse<>(false, "", user);
				Response response = Response.ok().entity(userResponse).build();
				return response;
			} else {
				CustomResponse<User> userResponse = new CustomResponse<>(true,
						"Login credentials are wrong. Please try again!", null);
				return Response.ok().entity(userResponse).build();
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;

	}

	@Path("uuid/{uuid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGames(@PathParam("uuid") String uuid) throws JSONException {
		if (uuid.isEmpty()) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters (id) is missing!",
					null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			UserDAO project = new UserDAO();
			User user = project.getUserByUUID(connection, uuid);
			CustomResponse<User> gameResponse = new CustomResponse<>(false, "", user);
			Response response = Response.ok().entity(gameResponse).build();
			return response;

		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;
	}
}
