package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;

import dto.Gladiator;

public class GladiatorsDAO {

	/* Get actual class name to be printed on */
	static Logger log = Logger.getLogger(GladiatorsDAO.class.getName());

	/**
	 * Get Gladiators
	 */
	public List<Gladiator> getGladiators(Connection connection, int userid) throws Exception {
		try (PreparedStatement ps1 = createPreparedStatementGetGladiators(connection, userid);
				ResultSet rs = ps1.executeQuery();) {
			List<Gladiator> gladiators = new ArrayList<>();
			while (rs.next()) {
				Gladiator gladiator = new Gladiator(rs.getInt("id"), rs.getInt("userid"), rs.getString("name"),
						rs.getInt("str"), rs.getInt("str_progress"), rs.getInt("dex"), rs.getInt("dex_progress"),
						rs.getInt("spd"), rs.getInt("spd_progress"), rs.getInt("con"), rs.getInt("con_progress"),
						rs.getInt("intel"), rs.getInt("int_progress"), rs.getInt("stamina"),
						rs.getInt("stamina_progress"), rs.getInt("mart_art"), rs.getInt("mart_art"),
						rs.getString("created_at"), rs.getString("updated_at"));

				gladiators.add(gladiator);
			}
			return gladiators;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

	private PreparedStatement createPreparedStatementGetGladiators(Connection con, int userid) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from gladiators WHERE userid = ?");
		ps.setInt(1, userid);
		return ps;
	}

	/**
	 * Get Gladiator
	 */
	public Gladiator getGladiator(Connection connection, int id) throws Exception {
		try (PreparedStatement ps1 = createPreparedStatementGetGladiator(connection, id);
				ResultSet rs = ps1.executeQuery();) {
			while (rs.next()) {
				Gladiator gladiator = new Gladiator(rs.getInt("id"), rs.getInt("userid"), rs.getString("name"),
						rs.getInt("str"), rs.getInt("str_progress"), rs.getInt("dex"), rs.getInt("dex_progress"),
						rs.getInt("spd"), rs.getInt("spd_progress"), rs.getInt("con"), rs.getInt("con_progress"),
						rs.getInt("intel"), rs.getInt("int_progress"), rs.getInt("stamina"),
						rs.getInt("stamina_progress"), rs.getInt("mart_art"), rs.getInt("mart_art"),
						rs.getString("created_at"), rs.getString("updated_at"));
				return gladiator;
			}

		} catch (Exception e) {
			log.error(e);
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementGetGladiator(Connection con, int userid) throws SQLException {
		PreparedStatement ps = con.prepareStatement("SELECT * from gladiators WHERE id = ?");
		ps.setInt(1, userid);
		return ps;
	}

	/**
	 * Storing new Gladiator details
	 */
	public Gladiator storeGladiator(Connection connection, Gladiator gladiator) throws Exception {
		try (PreparedStatement ps = createPreparedStatementInsertGladiator(connection, gladiator);) {

			int result = ps.executeUpdate();
			// check for successful store

			if (result > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					String id = Integer.toString(rs.getInt(1));
					return getGladiator(connection, Integer.parseInt(id));
				}

			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	private PreparedStatement createPreparedStatementInsertGladiator(Connection con, Gladiator gladiator)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"INSERT INTO gladiators(userid, name, str, str_progress, dex, dex_progress, spd, spd_progress, con, con_progress, intel, int_progress, stamina, stamina_progress, mart_art, mart_art_progress, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())",
				Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, gladiator.getUserid());
		ps.setString(2, gladiator.getName());
		ps.setInt(3, gladiator.getStr());
		ps.setInt(4, gladiator.getStr_progress());
		ps.setInt(5, gladiator.getDex());
		ps.setInt(6, gladiator.getDex_progress());
		ps.setInt(7, gladiator.getSpd());
		ps.setInt(8, gladiator.getSpd_progress());
		ps.setInt(9, gladiator.getCon());
		ps.setInt(10, gladiator.getCon_progress());
		ps.setInt(11, gladiator.getIntel());
		ps.setInt(12, gladiator.getIntel_progress());
		ps.setInt(13, gladiator.getStamina());
		ps.setInt(14, gladiator.getStamina_progress());
		ps.setInt(15, gladiator.getMart_art());
		ps.setInt(16, gladiator.getMart_art_progress());
		return ps;
	}

	/**
	 * Update gladiator
	 */
	public boolean updateGladiator(Connection connection, Gladiator gladiator) throws Exception {

		try (PreparedStatement ps = createPreparedStatementUpdateGladiator(connection, gladiator);) {

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

	private PreparedStatement createPreparedStatementUpdateGladiator(Connection con, Gladiator gladiator)
			throws SQLException {
		PreparedStatement ps = con.prepareStatement(
				"UPDATE gladiators SET name=?, str=?, str_progress=?, dex=?, dex_progress=?, spd=?, spd_progress=?, con=?, con_progress=?, mart_art=?, mart_art_progress=?, updated_at=NOW() WHERE id = ?");
		ps.setString(1, gladiator.getName());
		ps.setInt(2, gladiator.getStr());
		ps.setInt(3, gladiator.getStr_progress());
		ps.setInt(4, gladiator.getDex());
		ps.setInt(5, gladiator.getDex_progress());
		ps.setInt(6, gladiator.getSpd());
		ps.setInt(7, gladiator.getSpd_progress());
		ps.setInt(8, gladiator.getCon());
		ps.setInt(9, gladiator.getCon_progress());
		ps.setInt(10, gladiator.getMart_art());
		ps.setInt(11, gladiator.getMart_art_progress());
		ps.setInt(12, gladiator.getId());
		return ps;
	}
}
