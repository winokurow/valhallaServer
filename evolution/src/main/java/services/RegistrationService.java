package services;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import dao.Database;
import dao.UserDAO;
import dto.User;
import dto.UserResponse;

@Path("/registrationservice")
public class RegistrationService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(RegistrationService.class.getName());

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response postUser(@FormParam("name") String name, @FormParam("email") String email,
			@FormParam("password") String password) {
		if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
			UserResponse userResponse = new UserResponse(true,
					"Required parameters (name, email or password) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			UserDAO project = new UserDAO();
			if (project.isUserExisted(connection, email)) {
				UserResponse userResponse = new UserResponse(true, "User already existed with " + email, null);
				return Response.ok().entity(userResponse).build();
			} else {
				User user = project.storeUser(connection, name, email, password);
				if (user != null) {
					UserResponse userResponse = new UserResponse(false, "", user);
					Response response = Response.ok().entity(userResponse).build();
					return response;
				} else {
					UserResponse userResponse = new UserResponse(true, "Unknown error occurred in registration!", null);
					return Response.ok().entity(userResponse).build();
				}
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;

	}

	@GET
	@Produces("text/html")
	public Response defaultReverser() throws JSONException {

		StringBuilder sb = new StringBuilder();
		sb.append("ANKARA");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("original", sb.toString());
		jsonObject.put("reversed", sb.reverse().toString());

		String result = "" + jsonObject;
		return Response.status(200).entity(result).build();
	}
}
