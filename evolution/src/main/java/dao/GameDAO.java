package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;

import dto.Game;
import dto.User;

public class GameDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(GameDAO.class.getName());

	/**
	 * Storing new game returns game details
	 */
	public Game storeGame(Connection connection, String user1id, String user2id, String status) throws Exception {
		try (PreparedStatement ps = createPreparedStatementInsertGame(connection, user1id, user2id, status);) {

			int result = ps.executeUpdate();
			// check for successful store
			if (result > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					String id = Integer.toString(rs.getInt(1));
					Game game = getGame(connection, id);
					log.info("++++++++" + game.getStatus());
					return game;

				}
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementInsertGame(Connection con, String user1id, String user2id,
			String status) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO games(gamer1id, gamer2id, status, created_at) VALUES(?, ?, ?, NOW())",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, user1id);
		ps.setString(2, user2id);
		ps.setString(3, status);
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
		PreparedStatement ps = con.prepareStatement("UPDATE games SET status=?, updated_at=NOW() WHERE id = ?");
		ps.setString(2, uiid);
		ps.setString(1, status);
		return ps;
	}

	/**
	 * Update game user2
	 */
	public boolean setGameUser2(Connection connection, String uiid, String user) throws Exception {

		try (PreparedStatement ps = createPreparedStatementSetGameGamer2(connection, uiid, user);) {

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

	private PreparedStatement createPreparedStatementSetGameGamer2(Connection con, String uiid, String user)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE games SET gamer2id=?, updated_at=NOW() WHERE id = ?");
		ps.setString(2, uiid);
		ps.setString(1, user);
		return ps;
	}

	/**
	 * Update game field
	 */
	public boolean setGameField(Connection connection, String uiid, String field) throws Exception {

		try (PreparedStatement ps = createPreparedStatementSetGameField(connection, uiid, field);) {

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

	private PreparedStatement createPreparedStatementSetGameField(Connection con, String uiid, String fieldid)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement("UPDATE games SET field=?, updated_at=NOW() WHERE id = ?");
		ps.setString(1, fieldid);
		ps.setString(2, uiid);
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
				Game game = new Game(rs.getString("id"), user1.getId(), user1.getName(), user1.getPoints(), id, name,
						points, rs.getString("status"), rs.getString("field"), rs.getString("created_at"),
						rs.getString("updated_at"));
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
	 * Get game
	 */
	public Game getGame(Connection connection, String id) throws Exception {
		Game game = null;
		try (PreparedStatement ps1 = createPreparedStatementGetGame(connection, id);
				ResultSet rs = ps1.executeQuery();) {
			while (rs.next()) {
				UserDAO userdao = new UserDAO();
				User user1 = userdao.getUserByUUID(connection, rs.getString("gamer1id"));
				String gamer2id = "";
				String gamer2name = "";
				int gamer2points = 0;
				if (!(rs.getString("gamer2id").isEmpty())) {
					User user2 = userdao.getUserByUUID(connection, rs.getString("gamer2id"));
					gamer2id = user2.getId();
					gamer2name = user2.getName();
					gamer2points = user2.getPoints();
				}
				log.info("rs.getString(status)" + rs.getString("status"));
				game = new Game(rs.getString("id"), user1.getId(), user1.getName(), user1.getPoints(), gamer2id,
						gamer2name, gamer2points, rs.getString("status"), rs.getString("field"),
						rs.getString("created_at"), rs.getString("updated_at"));
				log.info(game.getStatus());
			}

		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return game;
	}

	private PreparedStatement createPreparedStatementGetGame(Connection con, String id) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from games WHERE id = ?");
		ps.setString(1, id);
		return ps;
	}

	/**
	 * Check game is existed or not
	 */
	public Game getGameByUser(Connection connection, String user) throws Exception {
		try (PreparedStatement ps = createPreparedStatementIsGameExisted(connection, user);
				ResultSet rs = ps.executeQuery();) {
			Game game = null;
			while (rs.next()) {
				UserDAO userdao = new UserDAO();
				User user1 = userdao.getUserByUUID(connection, rs.getString("gamer1id"));
				String gamer2id = "";
				String gamer2name = "";
				int gamer2points = 0;
				if (!(rs.getString("gamer2id").isEmpty())) {
					User user2 = userdao.getUserByUUID(connection, rs.getString("gamer2id"));
					gamer2id = user2.getId();
					gamer2name = user2.getName();
					gamer2points = user2.getPoints();
				}
				log.info(rs.getString("id"));
				game = new Game(rs.getString("id"), user1.getId(), user1.getName(), user1.getPoints(), gamer2id,
						gamer2name, gamer2points, rs.getString("status"), rs.getString("field"),
						rs.getString("created_at"), rs.getString("updated_at"));
			}
			return game;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	private PreparedStatement createPreparedStatementIsGameExisted(Connection con, String user) throws SQLException {
		PreparedStatement ps = con
				.prepareStatement("SELECT * from games WHERE (gamer1id = ? OR gamer2id = ?) AND NOT status=?");
		ps.setString(1, user);
		ps.setString(2, user);
		ps.setString(3, "ENDED");
		log.info("SELECT * from games WHERE (gamer1id = " + user + " OR gamer2id = " + user
				+ ") AND NOT status='ENDED'");
		return ps;
	}
}
