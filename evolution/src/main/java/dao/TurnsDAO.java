package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dto.Turn;

public class TurnsDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(TurnsDAO.class.getName());

	/**
	 * Get turns
	 */
	public List<Turn> getTurns(Connection connection, int gameid, int current) throws Exception {
		try (PreparedStatement ps1 = createPreparedStatementGetTurns(connection, gameid, current);
				ResultSet rs = ps1.executeQuery();) {
			List<Turn> turns = new ArrayList<>();
			while (rs.next()) {
				log.info(rs.getString("action"));
				String value2 = (rs.getString("value2") == null) ? "" : rs.getString("value2");
				String value3 = (rs.getString("value3") == null) ? "" : rs.getString("value3");
				String updated_at = (rs.getString("updated_at") == null) ? "" : rs.getString("updated_at");
				Turn turn = new Turn(rs.getInt("gameid"), rs.getInt("turn"), rs.getString("host"),
						rs.getString("action"), rs.getString("value1"), value2, value3, rs.getString("created_at"),
						updated_at);

				turns.add(turn);
			}
			return turns;

		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	private PreparedStatement createPreparedStatementGetTurns(Connection con, int gameid, int current)
			throws SQLException {
		log.info(current);
		PreparedStatement ps = con.prepareStatement("SELECT * from turns WHERE gameid = ? AND turn > ?");
		ps.setInt(1, gameid);
		ps.setInt(2, current);
		return ps;
	}

	/**
	 * Storing new turn details
	 */
	public List<Turn> storeTurn(Connection connection, Turn turn) throws Exception {
		try (PreparedStatement ps = createPreparedStatementInsertTurn(connection, turn);) {

			int result = ps.executeUpdate();
			// check for successful store
			if (result > 0) {

				log.info(turn.getGamedid());
				log.info(turn.getTurn() - 1);
				return getTurns(connection, turn.getGamedid(), turn.getTurn() - 2);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementInsertTurn(Connection con, Turn turn) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO turns(gameid, host, turn, action, value1, value2, value3, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, NOW())");
		ps.setInt(1, turn.getGamedid());
		ps.setString(2, turn.getHost());
		ps.setInt(3, turn.getTurn());
		ps.setString(4, turn.getAction());
		ps.setString(5, turn.getValue1());
		String value2 = (turn.getValue2() == null) ? "" : turn.getValue2();
		ps.setString(6, value2);
		String value3 = (turn.getValue3() == null) ? "" : turn.getValue3();
		ps.setString(7, value3);
		return ps;
	}
}
