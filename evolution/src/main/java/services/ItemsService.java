package services;

import java.sql.Connection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONException;

import dao.Database;
import dao.ItemsDAO;
import dto.Items;
import dto.responses.CustomResponse;

@Path("/itemsservice")
public class ItemsService {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(ItemsService.class.getName());

	@Path("{items}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getItems() throws JSONException {

		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			ItemsDAO project = new ItemsDAO();
			Items returnValue = project.getItems(connection);
			CustomResponse<Items> levelResponse = new CustomResponse<>(false, "", returnValue);
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
