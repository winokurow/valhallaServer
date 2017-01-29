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
				Turn turn = new Turn(rs.getInt("gameid"), rs.getInt("turn"), rs.getString("host"),
						rs.getString("action"), rs.getString("value1"), rs.getString("value2"), rs.getString("value3"),
						rs.getString("created_at"), rs.getString("updated_at"));

				turns.add(turn);
			}

		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementGetTurns(Connection con, int gameid, int current)
			throws SQLException {
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

				return getTurns(connection, turn.getGamedid(), turn.getTurn() - 1);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementInsertTurn(Connection con, Turn turn) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO games(gameid, host, turn, action, value1, value2, value3, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, NOW())");
		ps.setInt(1, turn.getGamedid());
		ps.setString(2, turn.getHost());
		ps.setInt(3, turn.getTurn());
		ps.setString(4, turn.getAction());
		ps.setString(5, turn.getValue1());
		ps.setString(6, turn.getValue2());
		ps.setString(7, turn.getValue3());
		return ps;
	}
}
