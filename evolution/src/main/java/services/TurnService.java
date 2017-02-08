package services;

import java.sql.Connection;
import java.util.List;

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
import dao.TurnsDAO;
import dto.Game;
import dto.Turn;
import dto.responses.CustomResponse;

@Path("/turnservice")
public class TurnService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(TurnService.class.getName());

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response postTurn(@FormParam("gameid") Integer gameid, @FormParam("host") String host,
			@FormParam("turn") Integer turnNumber, @FormParam("action") String action,
			@FormParam("value1") String value1, @FormParam("value2") String value2,
			@FormParam("value3") String value3) {
		if (gameid == null || host.isEmpty() || turnNumber == null || action.isEmpty() || value1.isEmpty()) {
			CustomResponse<Turn> turnResponse = new CustomResponse<>(true, "Required parameters is missing!", null);
			return Response.ok().entity(turnResponse).build();
		}
		Turn turn = new Turn(gameid, turnNumber, host, action, value1, value2, value3, "", "");
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			TurnsDAO project = new TurnsDAO();

			List<Turn> turns = project.storeTurn(connection, turn);
			log.info(turns.size());
			if (turns.size() > 1) {
				CustomResponse<Turn> TurnResponse = new CustomResponse<>(false, "", turns.get(0));
				Response response = Response.ok().entity(TurnResponse).build();
				return response;

			} else {
				CustomResponse<Game> gameResponse = new CustomResponse<>(true,
						"Unknown error occurred in game creation!", null);
				return Response.ok().entity(gameResponse).build();
			}
		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	@Path("game/{uiid}/current/{current}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTurn(@PathParam("uiid") Integer gameid, @PathParam("current") Integer current)
			throws JSONException {
		if (gameid == null || current == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters is missing!", null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			TurnsDAO project = new TurnsDAO();
			List<Turn> turns = project.getTurns(connection, gameid, current);
			CustomResponse<List<Turn>> gameResponse = new CustomResponse<>(false, "", turns);
			Response response = Response.ok().entity(gameResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}

}
