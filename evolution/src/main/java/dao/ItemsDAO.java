package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dto.Items;

public class ItemsDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(ItemsDAO.class.getName());

	/**
	 * Get i
	 */
	public Items getItems(Connection connection) throws Exception {
		try (PreparedStatement ps1 = createPreparedStatementGetItems(connection); ResultSet rs = ps1.executeQuery();) {
			Items items = null;
			while (rs.next()) {
				items = new Items(rs.getString("id"), rs.getString("name"), rs.getString("usageTyp"),
						rs.getString("typ"), rs.getString("range"), rs.getString("damage"), rs.getString("damageTyp"),
						rs.getString("bonus"), rs.getString("speed"), rs.getString("description"), rs.getString("fans"),
						rs.getString("dex"), rs.getString("isThrow"), rs.getString("stamina"),
						rs.getString("created_at"), rs.getString("updated_at"));
			}
			return items;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	private PreparedStatement createPreparedStatementGetItems(Connection con) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from items");
		return ps;
	}
}
