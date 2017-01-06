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
import dto.GameResponse;
import dto.GamesResponse;

@Path("/creategameservice")
public class CreateGameService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(CreateGameService.class.getName());

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response postGame(@FormParam("user") String user) {
		if (user.isEmpty()) {
			GameResponse userResponse = new GameResponse(true, "Required parameters (user) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			if (project.isGameExisted(connection, user)) {
				GameResponse userResponse = new GameResponse(true, "Game already existed", null);
				return Response.ok().entity(userResponse).build();
			} else {
				Game game = project.storeGame(connection, user, "", GameStatus.WAITING.asString());
				if (game != null) {
					GameResponse gameResponse = new GameResponse(false, "", game);
					Response response = Response.ok().entity(gameResponse).build();
					return response;

				} else {
					GameResponse gameResponse = new GameResponse(true, "Unknown error occurred in game creation!",
							null);
					return Response.ok().entity(gameResponse).build();
				}
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response setGameStatus(@FormParam("uiid") String user, @FormParam("status") String status) {
		if (user.isEmpty() || status.isEmpty()) {
			GameResponse userResponse = new GameResponse(true, "Required parameters (user) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			boolean result = project.setGameStatus(connection, user, status);
			if (!result) {
				GameResponse gameResponse = new GameResponse(true, "Unknown error occurred in game creation!", null);
				return Response.ok().entity(gameResponse).build();
			} else {
				GameResponse gameResponse = new GameResponse(false, "", null);
				Response response = Response.ok().entity(gameResponse).build();
				return response;
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	@Path("game/{uiid}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGame(@PathParam("uiid") String uiid) throws JSONException {
		if (uiid == null) {
			GameResponse userResponse = new GameResponse(true, "Required parameters (uiid) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			Game game = project.getGame(connection, uiid);
			GameResponse gamesResponse = new GameResponse(false, "", game);
			Response response = Response.ok().entity(gamesResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	@Path("games/{status}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGames(@PathParam("status") String status) throws JSONException {
		if (status == null) {
			GameResponse userResponse = new GameResponse(true, "Required parameters (status) is missing!", null);
			return Response.ok().entity(userResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GameDAO project = new GameDAO();
			List<Game> games = project.getGames(connection, status);
			GamesResponse gamesResponse = new GamesResponse(false, "", games);
			Response response = Response.ok().entity(gamesResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}
}
