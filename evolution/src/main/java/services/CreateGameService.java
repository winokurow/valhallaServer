package services;

import java.sql.Connection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;

import dao.Database;
import dao.GameDAO;
import data.GameStatus;
import dto.Game;
import dto.responses.CustomResponse;

@Path("/creategameservice")
public class CreateGameService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(CreateGameService.class.getName());

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response postGame(@FormParam("user") String user) {
		if (user.isEmpty()) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters (user) is missing!",
					null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			Game game = project.getGameByUser(connection, user);
			if (game != null) {
				CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Game already existed", null);
				return Response.ok().entity(gameResponse).build();
			} else {
				game = project.storeGame(connection, user, "", GameStatus.WAITING.asString());
				if (game != null) {
					CustomResponse<Game> gameResponse = new CustomResponse<>(false, "", game);
					Response response = Response.ok().entity(gameResponse).build();
					return response;
				} else {
					CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Something goes wrong", null);
					return Response.ok().entity(gameResponse).build();
				}
			}
		}

		catch (Exception e) {
			log.error(e.getMessage()); // Console
		}
		return null;
	}

	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeGame(@FormParam("uiid") String id, @FormParam("status") String status,
			@FormParam("userid") String userid) {
		if (id.isEmpty() || status.isEmpty()) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters are missing!", null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			boolean result = project.setGameStatus(connection, id, status);
			if (result) {
				if (userid != null) {
					result = project.setGameUser2(connection, id, userid);
				}
			}
			if (!result) {
				CustomResponse<Game> gameResponse = new CustomResponse<>(true,
						"Unknown error occurred in game creation!", null);
				return Response.ok().entity(gameResponse).build();
			} else {
				CustomResponse<Game> gameResponse = new CustomResponse<>(false, "", null);
				Response response = Response.ok().entity(gameResponse).build();
				return response;
			}
		}

		catch (Exception e) {
			log.error(e.getMessage()); // Console
		}
		return null;
	}

	@Path("game/{uiid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGame(@PathParam("uiid") String id) throws JSONException {
		if (id == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters (id) is missing!",
					null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			Game game = project.getGame(connection, id);
			log.info(game.getStatus());
			CustomResponse<Game> gameResponse = new CustomResponse<>(false, "", game);
			Response response = Response.ok().entity(gameResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e.getMessage()); // Console
		}
		return null;
	}

	@Path("games/{status}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGames(@PathParam("status") String status) throws JSONException {
		if (status == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters (status) is missing!",
					null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			List<Game> games = project.getGames(connection, status);
			CustomResponse<List<Game>> gameResponse = new CustomResponse<>(false, "", games);
			Response response = Response.ok().entity(gameResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e.getMessage()); // Console
		}
		return null;
	}

	@Path("userid/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGamesByUser(@PathParam("id") String id) throws JSONException {
		if (id == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameter (id) is missing!", null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			Game game = project.getGameByUser(connection, id);
			CustomResponse<Game> gameResponse = new CustomResponse<>(false, "", game);
			Response response = Response.ok().entity(gameResponse).build();
			return response;
		}

		catch (Exception e) {
			log.error(e.getMessage()); // Console
		}
		return null;
	}
}
