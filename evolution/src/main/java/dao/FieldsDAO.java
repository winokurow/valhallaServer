package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class FieldsDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(FieldsDAO.class.getName());

	/**
	 * Get game
	 */
	public String getField(Connection connection, String id) throws Exception {
		try (PreparedStatement ps1 = createPreparedStatementGetField(connection, id);
				ResultSet rs = ps1.executeQuery();) {
			String returnValue = "";
			while (rs.next()) {
				returnValue = rs.getString("cells");
			}
			return returnValue;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	private PreparedStatement createPreparedStatementGetField(Connection con, String fieldId) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from fields WHERE mapid = ?");
		ps.setString(1, fieldId);
		return ps;
	}

}
