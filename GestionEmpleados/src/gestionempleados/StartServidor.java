
package gestionempleados;

/**
 *
 * @author Pintahito
 */

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class StartServidor {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            EmpleadoRemoto service = new ServidorImpl();
            Naming.rebind("rmi://localhost:1099/EmpleadoService", service);
            System.out.println("Servidor listo...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

