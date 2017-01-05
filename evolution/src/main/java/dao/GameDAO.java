package dao;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import data.GameStatus;
import dto.Game;
import dto.User;

public class GameDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(GameDAO.class.getName());

	/**
	 * Storing new game returns game details
	 */
	public Game storeGame(Connection connection, String user1id, String user2id, String status) throws Exception {
		String uuid = uniqid("", true);
		try (PreparedStatement ps = createPreparedStatementInsertGame(connection, uuid, user1id, user2id, status);) {

			int result = ps.executeUpdate();
			// check for successful store
			if (result > 0) {
				try (PreparedStatement ps1 = createPreparedStatementGetGame(connection, uuid);
						ResultSet rs = ps1.executeQuery();) {
					Game game = null;
					while (rs.next()) {
						UserDAO userdao = new UserDAO();
						User user = userdao.getUserByUUID(connection, rs.getString("gamer1id"));

						game = new Game(rs.getString("unique_id"), user.getId(), user.getName(), user.getPoints(), "",
								"", 0, rs.getString("status"), rs.getString("created_at"));
					}
					return game;
				} catch (Exception e) {
					log.error(e);
					throw e;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementGetGame(Connection con, String uiid) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from games WHERE unique_id = ?");
		ps.setString(1, uiid);
		return ps;
	}

	/**
	 * Update game status
	 */
	public boolean setGameStatus(Connection connection, String uiid, String status) throws Exception {

		try (PreparedStatement ps = createPreparedStatementSetGameStatus(connection, uiid, status);) {

			int result = ps.executeUpdate();
			// check for successful store
			if (result > 0) {
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	private PreparedStatement createPreparedStatementSetGameStatus(Connection con, String uiid, String status)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE games SET status=?, updated_at=NOW() WHERE unique_id = ?");
		ps.setString(2, uiid);
		ps.setString(1, status);
		return ps;
	}

	private PreparedStatement createPreparedStatementInsertGame(Connection con, String unique_id, String user1id,
			String user2id, String status) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO games(unique_id, gamer1id, gamer2id, status, created_at) VALUES(?, ?, ?, ?, NOW())");
		ps.setString(1, unique_id);
		ps.setString(2, user1id);
		ps.setString(3, user2id);
		ps.setString(4, status);
		return ps;
	}

	/**
	 * Get all games
	 */
	public List<Game> getGames(Connection connection, String status) throws Exception {
		List<Game> returnValue = new ArrayList<>();
		try (PreparedStatement ps = createPreparedStatementGetGames(connection, status);
				ResultSet rs = ps.executeQuery();) {

			while (rs.next()) {
				UserDAO userdao = new UserDAO();
				User user1 = userdao.getUserByUUID(connection, rs.getString("gamer1id"));
				String id = "";
				String name = "";
				int points = 0;
				if (!(rs.getString("gamer2id").isEmpty())) {
					User user2 = userdao.getUserByUUID(connection, rs.getString("gamer2id"));
					id = user2.getId();
					name = user2.getName();
					points = user2.getPoints();
				}
				Game game = new Game(rs.getString("unique_id"), user1.getId(), user1.getName(), user1.getPoints(), id,
						name, points, rs.getString("status"), rs.getString("created_at"));
				returnValue.add(game);
			}
		} catch (

		Exception e) {
			throw e;
		}
		return returnValue;
	}

	private PreparedStatement createPreparedStatementGetGames(Connection con, String status) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from games WHERE status = ?");
		ps.setString(1, status);
		return ps;
	}

	/**
	 * Check game is existed or not
	 */
	public boolean isGameExisted(Connection connection, String user) throws Exception {
		try (PreparedStatement ps = createPreparedStatementIsGameExisted(connection, user);
				ResultSet rs = ps.executeQuery();) {

			int count = 0;
			while (rs.next()) {
				++count;
				// Get data from the current row and use it
			}
			return count > 0;
		} catch (

		Exception e) {
			throw e;
		}
	}

	private PreparedStatement createPreparedStatementIsGameExisted(Connection con, String user) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"SELECT unique_id from games WHERE gamer1id = ? AND NOT status='" + GameStatus.ENDED.asString() + "'");
		ps.setString(1, user);
		return ps;
	}

	/***
	 * Copy of uniquwid in php http://php.net/manual/fr/function.uniqid.php
	 * 
	 * @param prefix
	 * @param more_entropy
	 * @return
	 */
	public String uniqid(String prefix, boolean more_entropy) {
		long time = System.currentTimeMillis();
		// String uniqid = String.format("%fd%05f",
		// Math.floor(time),(time-Math.floor(time))*1000000);
		// uniqid = uniqid.substring(0, 13);
		String uniqid = "";
		if (!more_entropy) {
			uniqid = String.format("%s%08x%05x", prefix, time / 1000, time);
		} else {
			SecureRandom sec = new SecureRandom();
			byte[] sbuf = sec.generateSeed(8);
			ByteBuffer bb = ByteBuffer.wrap(sbuf);

			uniqid = String.format("%s%08x%05x", prefix, time / 1000, time);
			uniqid += "." + String.format("%.8s", "" + bb.getLong() * -1);
		}

		return uniqid;
	}

}
