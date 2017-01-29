package services;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import dao.Database;
import dao.UserDAO;
import dto.Gladiator;
import dto.User;
import dto.responses.CustomResponse;

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
			CustomResponse<User> userResponse = new CustomResponse<>(true,
					"Required parameters (name, email or password) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			UserDAO project = new UserDAO();
			if (project.isUserExisted(connection, email)) {
				CustomResponse<User> userResponse = new CustomResponse<>(true, "User already existed with " + email,
						null);
				return Response.ok().entity(userResponse).build();
			} else {
				User user = project.storeUser(connection, name, email, password, 1, 1);
				if (user != null) {
					GladiatorService gladiatorService = new GladiatorService();
					Gladiator gladiator = gladiatorService.createGladiator(Integer.parseInt(user.getId()));
					if (gladiator != null) {
						CustomResponse<User> userResponse = new CustomResponse<>(false, "", user);
						Response response = Response.ok().entity(userResponse).build();
						return response;
					} else {
						CustomResponse<User> userResponse = new CustomResponse<>(true,
								"Unknown error occurred in registration!", null);
						return Response.ok().entity(userResponse).build();
					}

				} else {
					CustomResponse<User> userResponse = new CustomResponse<>(true,
							"Unknown error occurred in registration!", null);
					return Response.ok().entity(userResponse).build();
				}
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;

	}
}
