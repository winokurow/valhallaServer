package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import dto.Level;

public class LevelDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(LevelDAO.class.getName());

	/**
	 * Get level
	 */
	public Level getLevel(Connection connection, int levelNumber) throws Exception {
		try (PreparedStatement ps1 = createPreparedStatementGetLevel(connection, levelNumber);
				ResultSet rs = ps1.executeQuery();) {
			while (rs.next()) {
				Level level = new Level(levelNumber, rs.getInt("gladiators_quantity"));
				return level;
			}

		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementGetLevel(Connection con, int level) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from levels WHERE level = ?");
		ps.setInt(1, level);
		return ps;
	}

}
