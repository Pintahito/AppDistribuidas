
package gestionempleados;

/**
 *
 * @author Pintahito
 */

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorImpl extends UnicastRemoteObject implements EmpleadoRemoto {
    private Connection conn;

    protected ServidorImpl() throws RemoteException {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gestion_empleados", "root", "1022");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void agregarEmpleado(String nombre, String puesto, double salario) throws RemoteException {
        try {
            String query = "INSERT INTO Empleado (nombre, puesto, salario) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, puesto);
            ps.setDouble(3, salario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modificarEmpleado(int id, String nombre, String puesto, double salario) throws RemoteException {
        try {
            String query = "UPDATE Empleado SET nombre = ?, puesto = ?, salario = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, nombre);
            ps.setString(2, puesto);
            ps.setDouble(3, salario);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminarEmpleado(int id) throws RemoteException {
        try {
            String query = "DELETE FROM Empleado WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

@Override
public List<String> buscarEmpleado(String nombre) throws RemoteException {
    List<String> empleados = new ArrayList<>();
    try {
        String query = "SELECT * FROM Empleado WHERE nombre LIKE ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, "%" + nombre + "%");
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            // Incluye el ID en el formato de salida
            String empleadoInfo = rs.getInt("id") + " - " + rs.getString("nombre") + " - " 
                                + rs.getString("puesto") + " - $" + rs.getDouble("salario");
            empleados.add(empleadoInfo);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return empleados;
}

}

