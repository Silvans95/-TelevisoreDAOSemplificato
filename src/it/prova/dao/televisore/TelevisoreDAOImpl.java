package it.prova.dao.televisore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.dao.AbstractMySQLDAO;
import it.prova.model.Televisore;

public class TelevisoreDAOImpl extends AbstractMySQLDAO implements TelevisoreDAO {

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Televisore> list() throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		ArrayList<Televisore> result = new ArrayList<Televisore>();
		Televisore televisoreTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from televisore")) {

			while (rs.next()) {
				televisoreTemp = new Televisore();
				televisoreTemp.setMarca(rs.getString("MARCA"));
				televisoreTemp.setModello(rs.getString("MODELLO"));
				televisoreTemp.setDataProduzione(rs.getDate("DATAPRODUZIONE"));
				televisoreTemp.setId(rs.getLong("ID"));
				result.add(televisoreTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Televisore get(Long idInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Televisore result = null;
		try (PreparedStatement ps = connection.prepareStatement("select * from televisore where id=?")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Televisore();
					result.setMarca(rs.getString("MARCA"));
					result.setModello(rs.getString("MODELLO"));
					result.setDataProduzione(rs.getDate("DATAPRODUZIONE"));
					result.setId(rs.getLong("ID"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Televisore televisoreInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (televisoreInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO Televisore (marca, modello, dataproduzione) VALUES (?, ?, ?);")) {
			ps.setString(1, televisoreInput.getMarca());
			ps.setString(2, televisoreInput.getModello());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(3, new java.sql.Date(televisoreInput.getDataProduzione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Televisore televisoreInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (televisoreInput == null || televisoreInput.getId() == null || televisoreInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection
				.prepareStatement("UPDATE televisore SET marca=?, modello=?, dataproduzione=? where id=?;")) {
			ps.setString(1, televisoreInput.getMarca());
			ps.setString(2, televisoreInput.getModello());
			// quando si fa il setDate serve un tipo java.sql.Date
			ps.setDate(3, new java.sql.Date(televisoreInput.getDataProduzione().getTime()));
			ps.setLong(4, televisoreInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Televisore televisoreInput) throws Exception {
		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (televisoreInput == null || televisoreInput.getId() == null || televisoreInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM televisore WHERE ID=?")) {
			ps.setLong(1, televisoreInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Televisore> findByExample(Televisore input) throws Exception {

		// prima di tutto cerchiamo di capire se possiamo effettuare le operazioni
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Televisore> result = new ArrayList<Televisore>();
		Televisore televisoreTemp = null;

		String query = "select * from televisore where 1=1 ";
		if (input.getMarca() != null && !input.getMarca().isEmpty()) {
			query += " and marca like '" + input.getMarca() + "%' ";
		}
		if (input.getModello() != null && !input.getModello().isEmpty()) {
			query += " and modello like '" + input.getModello() + "%' ";
		}

		if (input.getDataProduzione() != null) {
			query += " and dataproduzione='" + new java.sql.Date(input.getDataProduzione().getTime()) + "' ";
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				televisoreTemp = new Televisore();
				televisoreTemp.setMarca(rs.getString("MARCA"));
				televisoreTemp.setModello(rs.getString("MODELLO"));
				televisoreTemp.setDataProduzione(rs.getDate("DATAPRODUZIONE"));
				televisoreTemp.setId(rs.getLong("ID"));
				result.add(televisoreTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}
