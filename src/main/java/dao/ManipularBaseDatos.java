package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import model.*;

public class ManipularBaseDatos {

	private Connection conn;

	/**
	 * Constructor
	 */
	public ManipularBaseDatos() {
		conn = iniciarBD.getConnection();
		try {
			crearTablas();
			// crearValoresPorDefecto();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Si el fichero no está abierto lo abre
	 * 
	 * @throws SQLException
	 */
	private void crearTablas() throws SQLException {

		String sql = null;
		if (iniciarBD.typeDB.equals("sqlite")) {

			sql = "CREATE TABLE IF NOT EXISTS empleados (" + 
						"uuid STRING PRIMARY KEY," + 
						"nombre STRING NOT NULL," + 
						"salario STRING," + 
						"fNacimiento STRING," + 
						"uuidDepa STRING" + 
					")";
			conn.createStatement().executeUpdate(sql);

			sql = "CREATE TABLE IF NOT EXISTS departamentos(" + 
						"uuid STRING PRIMARY KEY," + 
						"nombre STRING," + 
						"uuidJefe STRING"
					+ ")";
			conn.createStatement().executeUpdate(sql);
		} else if (iniciarBD.typeDB.equals("mariadb")) {
			sql = "CREATE TABLE IF NOT EXISTS empleados (" + 
					"uuid VARCHAR(36) PRIMARY KEY," + 
					"nombre VARCHAR(25) NOT NULL," + 
					"salario VARCHAR(25) NOT NULL," + 
					"fNacimiento VARCHAR(25) NOT NULL," + 
					"uuidDepa VARCHAR(36)" + 
				")";
		conn.createStatement().executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS departamentos(" + 
					"uuid VARCHAR(36) PRIMARY KEY," + 
					"nombre VARCHAR(25) NOT NULL," + 
					"uuidJefe VARCHAR(36)" +
				 ")";
		conn.createStatement().executeUpdate(sql);
		}

	}

	/**
	 * Cierra la agenda
	 */
	public void close() {
		iniciarBD.close();
	}

	/**
	 * Mostrar los departamentos
	 * 
	 * @return cadena con los departamentos
	 */
	public String showDepartamentos() {
		try {
			String result = "";
			ResultSet rst = conn.createStatement().executeQuery("SELECT * FROM departamentos");

			while (rst.next()) {
				result += rst.getString(1) + ", " + rst.getString(2) + ", " + rst.getString(3) + "\n";
			}

			return result;
		} catch (SQLException e) {
			return "";
		}
	}

	/**
	 * Mostrar los empleados
	 * 
	 * @return cadena con los empleados
	 */
	public String showEmpleados() {
		try {
			String result = "";
			ResultSet rst = conn.createStatement().executeQuery("SELECT * FROM empleados");

			while (rst.next()) {
				result += rst.getString(1) + ", " + rst.getString(2) + ", " + rst.getString(3) + ", " + rst.getString(4)
						+ ", " + rst.getString(5) + "\n";
			}

			return result;
		} catch (SQLException e) {
			return "";
		}
	}

	/**
	 * Añade un nuevo empleado a la empresa
	 * 
	 * @param c
	 * @param uuid
	 * @return true si ha sido añadido, false en caso contrario
	 */
	public boolean addEmpleado(Empleado c, UUID idDepartamento) {
		String sql = "";
		try {
			sql = "INSERT INTO empleados (uuid, nombre, salario, fNacimiento, uuidDepa) VALUES (?,?,?,?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getId().toString());
			ps.setString(2, c.getNombre());
			ps.setString(3, c.getSalario());
			ps.setString(4, c.getFNacimiento().toString());
			if(idDepartamento == null) {
				ps.setString(5, null);
			}else {
				ps.setString(5, idDepartamento.toString());
			}
			
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Añade un nuevo departamento a la empresa
	 * 
	 * @param c
	 * @param uuid
	 * @return true si ha sido añadido, false en caso contrario
	 */
	public boolean addDepartamento(Departamento c, UUID idEmpleado) {
		String sql = "";
		try {
			sql = "INSERT INTO departamentos (uuid, nombre, uuidJefe) VALUES (?,?,?)";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getId().toString());
			ps.setString(2, c.getNombre());
			if(idEmpleado==null) {
				ps.setString(3, null);
			}else {
				ps.setString(3, idEmpleado.toString());
			}
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Modificar un departamento de la empresa
	 * 
	 * @param c
	 * @return true si ha sido modificado, false en caso contrario
	 */

	public boolean modifyDepartamento(Departamento c) {
		String sql = "";
		try {
			sql = "UPDATE departamentos SET nombre=?, uuidJefe=? WHERE uuid=?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getNombre());
			Empleado e1 = c.getEmpleado();
			ps.setString(2, e1.getId().toString());
			ps.setString(3, c.getId().toString());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Modificar un empleado de la empresa
	 * 
	 * @param c
	 * @return true si ha sido modificado, false en caso contrario
	 */

	public boolean modifyEmpleado(Empleado c) {
		String sql = "";
		try {
			sql = "UPDATE empleados SET nombre=?, salario =?, fNacimiento=?, uuidDepa = ? WHERE uuid=?";

			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, c.getNombre());
			ps.setString(2, c.getSalario());
			ps.setString(3, c.getFNacimiento().toString());
			Departamento d1 = c.getDepartamento();
			ps.setString(4, d1.getId().toString());
			ps.setString(5, c.getId().toString());

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Comprobar existencia empleado
	 * 
	 * @param uuid
	 * @return Empleado con departamento null
	 */
	public Empleado findEmpleado(UUID uuid) {
		try {
			Empleado a = null;
			String sql = "SELECT * FROM empleados WHERE uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uuid.toString());

			ResultSet rst = ps.executeQuery();
			while (rst.next()) {
				a = new Empleado(UUID.fromString(rst.getString(1)), rst.getString(2), rst.getString(3),
						StringToLdate(rst.getString(4)));
			}

			return a;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Comprobar existencia departamento
	 * 
	 * @param uuid
	 * @return Departamento con empleado null
	 */
	public Departamento findDepartamento(UUID uuid) {
		try {
			Departamento a = null;
			String sql = "SELECT * FROM departamentos WHERE uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uuid.toString());

			ResultSet rst = ps.executeQuery();
			while (rst.next()) {
				a = new Departamento(UUID.fromString(rst.getString(1)), rst.getString(2));
			}

			return a;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public LocalDate StringToLdate(String fecha) {
		LocalDate ld;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		ld = LocalDate.parse(fecha, formatter);
		return ld;
	}

	/**
	 * Eliminar empleado
	 * 
	 * @param uuid
	 * @return true si elimina correctamente, false si no
	 */
	public boolean deleteEmpleado(String uuid) {
		String sql = "";
		try {
			// Actualizar el campo uuidJefe a NULL en los departamentos donde el empleado es jefe
			sql = "UPDATE departamentos SET uuidJefe = NULL WHERE uuidJefe = ?";
			PreparedStatement updatePs = conn.prepareStatement(sql);
			updatePs.setString(1, uuid);
			updatePs.executeUpdate();

			sql = "DELETE FROM empleados WHERE uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uuid);

			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Eliminar departamento
	 * 
	 * @param uuid
	 * @return true si elimina correctamente, false si no
	 */
	public boolean deleteDepartamento(String uuid) {
		String sql = "";
		try {
			sql = "UPDATE empleados SET uuidDepa = NULL WHERE uuidDepa = ?";
			PreparedStatement updatePs = conn.prepareStatement(sql);
			updatePs.setString(1, uuid);
			updatePs.executeUpdate();

			sql = "DELETE FROM departamentos WHERE uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uuid);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Buscar empleado
	 * 
	 * @param String
	 * @return objeto Empleado
	 */
	public Empleado selectEmpleado(String empleadoUUID) {

		try {
			String sql = "SELECT * FROM empleados WHERE uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, empleadoUUID);
			ResultSet rst = ps.executeQuery();

			if (rst.next()) {
				UUID uuid = UUID.fromString(rst.getString("uuid"));
				String nombre = rst.getString("nombre");
				String salario = rst.getString("salario");
				LocalDate fechaNacimiento = StringToLdate(rst.getString("fNacimiento"));
			
				if(rst.getString("uuidDepa")==null) {
					return new Empleado(uuid, nombre, salario, fechaNacimiento);
				}else {
					UUID uuidDepartamento = UUID.fromString(rst.getString("uuidDepa"));
					return new Empleado(uuid, nombre, salario, fechaNacimiento, findDepartamento(uuidDepartamento));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null; // Devuelve null si no se encontró al empleado
	}

	/**
	 * Buscar departamento 
	 * 
	 * @param String
	 * @return objeto Departamento
	 */	
	public Departamento selectDepartamento(String departamentoUUID) {

		try {
			String sql = "SELECT * FROM departamentos WHERE uuid = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, departamentoUUID);
			ResultSet rst = ps.executeQuery();
			if (rst.next()) {
				UUID uuid = UUID.fromString(rst.getString("uuid"));
				String nombre = rst.getString("nombre");
				if(rst.getString("uuidJefe")==null) {
					return new Departamento(uuid, nombre);
				}else {
					UUID jefeUUID = UUID.fromString(rst.getString("uuidJefe"));
					return new Departamento(uuid, nombre, findEmpleado(jefeUUID));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return null; // Devuelve null si no se encontró el departamento
	}
}
