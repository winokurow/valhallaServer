package dao;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import common.PasswordAuthentication;
import dto.User;

public class UserDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(UserDAO.class.getName());

	/**
	 * Storing new user returns user details
	 */
	public User storeUser(Connection connection, String name, String email, String password, int points, int level)
			throws Exception {
		String uuid = uniqid("", true);
		PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
		String hash = passwordAuthentication.hash(password.toCharArray());

		try (PreparedStatement ps = createPreparedStatementInsertUser(connection, uuid, name, email, hash, points,
				level);) {

			int result = ps.executeUpdate();
			// check for successful store
			if (result > 0) {
				User user = getUserByUUID(connection, uuid);
				return user;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementInsertUser(Connection con, String uuid, String name, String email,
			String password, int points, int level) throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO users(unique_id, name, email, encrypted_password, points, level, created_at) VALUES(?, ?, ?, ?, ?, ?, NOW())");
		ps.setString(1, uuid);
		ps.setString(2, name);
		ps.setString(3, email);
		ps.setString(4, password);
		ps.setInt(5, points);
		ps.setInt(6, level);
		return ps;
	}

	/**
	 * Get user by email and password
	 */
	public User getUserByEmailAndPassword(Connection connection, String email, String password) throws Exception {
		try (PreparedStatement ps = createPreparedStatementGetUser(connection, email);
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				String encrypted_password = rs.getString("encrypted_password");
				PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

				boolean isCorrect = passwordAuthentication.authenticate(password.toCharArray(), encrypted_password);
				if (isCorrect) {
					return new User(rs.getString("id"), rs.getString("unique_id"), rs.getString("name"),
							rs.getString("email"), rs.getInt("points"), rs.getInt("level"), rs.getString("created_at"),
							rs.getString("updated_at"));
				}
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementGetUser(Connection con, String email) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from users WHERE email = ?");
		ps.setString(1, email);
		return ps;
	}

	/**
	 * Get user by uuid
	 */
	public User getUserByUUID(Connection connection, String uuid) throws Exception {
		try (PreparedStatement ps = createPreparedStatementGetUserUUID(connection, uuid);
				ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				return new User(rs.getString("id"), rs.getString("unique_id"), rs.getString("name"),
						rs.getString("email"), rs.getInt("points"), rs.getInt("level"), rs.getString("created_at"),
						rs.getString("updated_at"));
			}

		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementGetUserUUID(Connection con, String email) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from users WHERE id = ?");
		ps.setString(1, email);
		return ps;
	}

	/**
	 * Check user is existed or not
	 */
	public boolean isUserExisted(Connection connection, String email) throws Exception {
		try (PreparedStatement ps = createPreparedStatementIsUserExisted(connection, email);
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

	private PreparedStatement createPreparedStatementIsUserExisted(Connection con, String email) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT email from users WHERE email = ?");
		ps.setString(1, email);
		return ps;
	}

	/***
	 * Copy of uniqid in php http://php.net/manual/fr/function.uniqid.php
	 * 
	 * @param prefix
	 * @param more_entropy
	 * @return
	 */
	private String uniqid(String prefix, boolean more_entropy) {
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
