package services;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;

import dao.Database;
import dao.GladiatorsDAO;
import dto.Game;
import dto.Gladiator;
import dto.responses.CustomResponse;

@Path("/gladiatorservice")
public class GladiatorService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(GladiatorService.class.getName());

	@PUT
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateGladiator(@FormParam("id") Integer id, @FormParam("name") String name,
			@FormParam("str") Integer str, @FormParam("str_progress") Integer str_progress,
			@FormParam("dex") Integer dex, @FormParam("dex_progress") Integer dex_progress,
			@FormParam("spd") Integer spd, @FormParam("spd_progress") Integer spd_progress,
			@FormParam("con") Integer con, @FormParam("con_progress") Integer con_progress,
			@FormParam("int") Integer intel, @FormParam("int_progress") Integer int_progress,
			@FormParam("stamina") Integer stamina, @FormParam("stamina_progress") Integer stamina_progress,
			@FormParam("mart_art") Integer mart_art, @FormParam("mart_art_progress") Integer mart_art_progress) {
		if (id == null || name.isEmpty() || str == null || str_progress == null || dex == null || dex_progress == null
				|| spd == null || spd_progress == null || con == null || con_progress == null || intel == null
				|| int_progress == null || stamina == null || stamina_progress == null || mart_art == null
				|| mart_art_progress == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameters are missing!", null);
			return Response.ok().entity(gameResponse).build();
		}
		Gladiator gladiator = new Gladiator(id, -1, name, str, str_progress, dex, dex_progress, spd, spd_progress, con,
				con_progress, intel, int_progress, stamina, stamina_progress, mart_art, mart_art_progress, "", "");
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GladiatorsDAO project = new GladiatorsDAO();

			boolean isUpdated = project.updateGladiator(connection, gladiator);
			if (isUpdated) {
				CustomResponse<Gladiator> gameResponse = new CustomResponse<>(false, "", gladiator);
				Response response = Response.ok().entity(gameResponse).build();
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

	@Path("id/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGladiator(@PathParam("id") Integer id) throws JSONException {
		if (id == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameter (id) is missing!", null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GladiatorsDAO project = new GladiatorsDAO();
			Gladiator gladiator = project.getGladiator(connection, id);
			CustomResponse<Gladiator> gameResponse = new CustomResponse<>(false, "", gladiator);
			Response response = Response.ok().entity(gameResponse).build();
			return response;

		}

		catch (

		Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	@Path("userid/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getGladiators(@PathParam("id") Integer userid) throws JSONException {
		if (userid == null) {
			CustomResponse<Game> gameResponse = new CustomResponse<>(true, "Required parameter (userid) is missing!",
					null);
			return Response.ok().entity(gameResponse).build();
		}
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			GladiatorsDAO project = new GladiatorsDAO();
			List<Gladiator> gladiators = project.getGladiators(connection, userid);
			CustomResponse<List<Gladiator>> gameResponse = new CustomResponse<>(false, "", gladiators);
			Response response = Response.ok().entity(gameResponse).build();
			return response;

		}

		catch (Exception e) {
			log.error(e); // Console
		}
		return null;
	}

	public Gladiator createGladiator(Integer userid) {
		int str = ThreadLocalRandom.current().nextInt(1, 4);
		int dex = ThreadLocalRandom.current().nextInt(1, 4);
		int spd = ThreadLocalRandom.current().nextInt(1, 4);
		int con = ThreadLocalRandom.current().nextInt(1, 4);
		int intel = ThreadLocalRandom.current().nextInt(1, 4);
		int stamina = ThreadLocalRandom.current().nextInt(1, 4);
		String name = getName();
		Gladiator gladiator = new Gladiator(-1, userid, name, str, 0, dex, 0, spd, 0, con, 0, intel, 0, stamina, 0, 0,
				0, "", "");
		Database database = new Database();
		Connection connection;
		try {
			connection = database.Get_Connection();
			GladiatorsDAO project = new GladiatorsDAO();
			gladiator = project.storeGladiator(connection, gladiator);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return gladiator;
	}

	private String getName() {
		String csvFile = "names.csv";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(csvFile).getFile());
		List<String> lines;
		try {
			lines = FileUtils.readLines(file, "UTF-8");
		} catch (IOException e) {
			lines = new ArrayList<>();
			lines.add("Test, Test");
		}
		int index = ThreadLocalRandom.current().nextInt(lines.size());
		return lines.get(index);

	}
}
