package gestionempleados;

/**
 *
 * @author Pintahito
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.util.List;

public class Cliente extends JFrame {

    private EmpleadoRemoto empleadoService;
    private JTextField txtNombre, txtPuesto, txtSalario;
    private JTextArea txtResultado;
    private JComboBox<String> listaEmpleados;
    private JButton btnAgregar, btnBuscar, btnEditar, btnEliminar;
    private int empleadoSeleccionadoId = -1; // Guardar el ID del empleado seleccionado

    public Cliente() {
        try {
            empleadoService = (EmpleadoRemoto) Naming.lookup("rmi://localhost:1099/EmpleadoService");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configuración de la ventana principal
        setTitle("Gestión de Empleados");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla

        // Panel superior - Título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(42, 157, 143));
        JLabel lblTitulo = new JLabel("Gestión de Empleados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central - Formulario de entrada
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espacio interno
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(15);
        JLabel lblPuesto = new JLabel("Puesto:");
        txtPuesto = new JTextField(15);
        JLabel lblSalario = new JLabel("Salario:");
        txtSalario = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblNombre, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelFormulario.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblPuesto, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelFormulario.add(txtPuesto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panelFormulario.add(lblSalario, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelFormulario.add(txtSalario, gbc);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAgregar = new JButton("Agregar");
        btnBuscar = new JButton("Buscar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");

        // Colores de botones
        btnAgregar.setBackground(new Color(231, 111, 81));
        btnAgregar.setForeground(Color.WHITE);
        btnBuscar.setBackground(new Color(244, 162, 97));
        btnBuscar.setForeground(Color.WHITE);
        btnEditar.setBackground(new Color(233, 196, 106));
        btnEditar.setForeground(Color.WHITE);
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);

        btnEditar.setEnabled(false); // Desactivado hasta seleccionar un empleado
        btnEliminar.setEnabled(false); // Desactivado hasta seleccionar un empleado

        panelBotones.add(btnAgregar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        panelFormulario.add(panelBotones, gbc);

        add(panelFormulario, BorderLayout.CENTER);

        // Panel inferior - Resultados
        JPanel panelResultados = new JPanel();
        panelResultados.setLayout(new BorderLayout());
        JLabel lblResultados = new JLabel("Resultados:");
        lblResultados.setFont(new Font("Arial", Font.BOLD, 14));
        txtResultado = new JTextArea(6, 30);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);
        txtResultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtResultado);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panelResultados.add(lblResultados, BorderLayout.NORTH);
        panelResultados.add(scrollPane, BorderLayout.CENTER);

        listaEmpleados = new JComboBox<>();
        listaEmpleados.setVisible(false); // Se mostrará cuando se haga una búsqueda
        listaEmpleados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listaEmpleados.getSelectedIndex() != -1) {
                    String[] datos = ((String) listaEmpleados.getSelectedItem()).split(" - ");
                    empleadoSeleccionadoId = Integer.parseInt(datos[0]);  // Ahora puedes obtener el ID correctamente
                    txtNombre.setText(datos[1]);  // Nombre
                    txtPuesto.setText(datos[2]);  // Puesto
                    txtSalario.setText(datos[3].replace("$", "")); // Salario, quitando el signo de dólar

                    btnEditar.setEnabled(true); // Habilitar los botones al seleccionar un empleado
                    btnEliminar.setEnabled(true);
                }
            }
        });

        panelResultados.add(listaEmpleados, BorderLayout.SOUTH);
        add(panelResultados, BorderLayout.SOUTH);

        // Acciones de los botones
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombre.getText();
                    String puesto = txtPuesto.getText();
                    double salario = Double.parseDouble(txtSalario.getText());
                    empleadoService.agregarEmpleado(nombre, puesto, salario);
                    txtResultado.setText("Empleado agregado exitosamente.");
                    limpiarCampos();
                } catch (Exception ex) {
                    txtResultado.setText("Error al agregar el empleado: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombre.getText();
                    List<String> resultados = empleadoService.buscarEmpleado(nombre);
                    listaEmpleados.removeAllItems(); // Limpiar la lista
                    if (resultados.isEmpty()) {
                        txtResultado.setText("No se encontraron empleados con el nombre: " + nombre);
                    } else {
                        for (String emp : resultados) {
                            listaEmpleados.addItem(emp); // Añadir empleados encontrados a la lista
                        }
                        listaEmpleados.setVisible(true); // Mostrar la lista desplegable
                    }
                } catch (Exception ex) {
                    txtResultado.setText("Error al buscar empleados: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombre = txtNombre.getText();
                    String puesto = txtPuesto.getText();
                    double salario = Double.parseDouble(txtSalario.getText());
                    empleadoService.modificarEmpleado(empleadoSeleccionadoId, nombre, puesto, salario);
                    txtResultado.setText("Empleado actualizado exitosamente.");
                    limpiarCampos();
                } catch (Exception ex) {
                    txtResultado.setText("Error al actualizar el empleado: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    empleadoService.eliminarEmpleado(empleadoSeleccionadoId);
                    txtResultado.setText("Empleado eliminado exitosamente.");
                    limpiarCampos();
                } catch (Exception ex) {
                    txtResultado.setText("Error al eliminar el empleado: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    // Método para limpiar campos y desactivar botones
    private void limpiarCampos() {
        txtNombre.setText("");
        txtPuesto.setText("");
        txtSalario.setText("");
        listaEmpleados.setVisible(false);
        btnEditar.setEnabled(false);
        btnEliminar.setEnabled(false);
        empleadoSeleccionadoId = -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Cliente cliente = new Cliente();
            cliente.setVisible(true);
        });
    }
}
