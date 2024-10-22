
package gestionempleados;

/**
 *
 * @author Pintahito
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface EmpleadoRemoto extends Remote {
    void agregarEmpleado(String nombre, String puesto, double salario) throws RemoteException;
    void modificarEmpleado(int id, String nombre, String puesto, double salario) throws RemoteException;
    void eliminarEmpleado(int id) throws RemoteException;
    List<String> buscarEmpleado(String nombre) throws RemoteException;
}
