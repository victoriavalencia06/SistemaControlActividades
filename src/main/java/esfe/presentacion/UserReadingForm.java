package esfe.presentacion;

import esfe.Persistencia.UserDAO; // Importa la interfaz o clase UserDAO, que define las operaciones de acceso a datos para la entidad User.

import javax.swing.*; // Importa el paquete Swing, que proporciona clases para crear interfaces gráficas de usuario.
import javax.swing.table.DefaultTableModel; // Importa la clase DefaultTableModel, utilizada para crear y manipular modelos de datos para JTable.
import esfe.dominio.User; // Importa la clase User, que representa la entidad de usuario en el dominio de la aplicación.
import esfe.utils.CUD; // Importa el enum  CUD (Create, Update, Delete).
import esfe.utils.Audit;
import esfe.dominio.Action;
import java.awt.event.KeyAdapter; // Importa la clase KeyAdapter, una clase adaptadora para recibir eventos de teclado.
import java.awt.event.KeyEvent; // Importa la clase KeyEvent, que representa un evento de teclado.
import java.sql.SQLException;
import java.util.ArrayList; // Importa la clase ArrayList, una implementación de la interfaz List que permite almacenar colecciones dinámicas de objetos.


public class UserReadingForm extends BaseForm {
    private JPanel mainPanel;
    private JTextField txtName;
    private JButton btnCreate;
    private JTable tableUsers;
    private JButton btnUpdate;
    private JButton btnDelete;

    private UserDAO userDAO; // Instancia de UserDAO para realizar operaciones de base de datos de usuarios.
    private MainForm mainForm; // Referencia a la ventana principal de la aplicación.

    // Constructor de la clase UserReadingForm. Recibe una instancia de MainForm como parámetro.
    public UserReadingForm(MainForm mainForm) {
        super("UserReadingForm"); // 👈 Esto activa registro de apertura y cierre
        this.mainForm = mainForm; // Asigna la instancia de MainForm recibida a la variable local.
        userDAO = new UserDAO(); // Crea una nueva instancia de UserDAO al instanciar este formulario.
        setContentPane(mainPanel); // Establece el panel principal como el contenido de este diálogo.
        setModal(true); // Hace que este diálogo sea modal, bloqueando la interacción con la ventana principal hasta que se cierre.
        setTitle("Buscar Usuario"); // Establece el título de la ventana del diálogo.
        pack(); // Ajusta el tamaño de la ventana para que todos sus componentes se muestren correctamente.
        setLocationRelativeTo(mainForm); // Centra la ventana del diálogo relative a la ventana principal.
        loadLast500Users();

        // Agrega un listener de teclado al campo de texto txtNombre.
        txtName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtName.getText().trim().isEmpty()) {
                    // Si hay texto, aplicar filtro
                    search(txtName.getText());
                } else {
                    // Si no hay texto, cargar últimos 500
                    loadLast500Users();
                }
            }
        });

        // Agrega un ActionListener al botón btnCreate.
        btnCreate.addActionListener(s -> {
            Audit.log(Action.USER_CREATE);
            UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.CREATE, new User());
            userWriteForm.setVisible(true);
            loadLast500Users();  // Recarga la tabla con los últimos 500 usuarios
        });

        // Agrega un ActionListener al botón btnUpdate.
        btnUpdate.addActionListener(s -> {
            User user = getUserFromTableRow();
            if (user != null) {
                Audit.log(Action.USER_UPDATE);
                UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.UPDATE, user);
                userWriteForm.setVisible(true);
                loadLast500Users();
            }
        });

        // Agrega un ActionListener al botón btnEliminar.
        btnDelete.addActionListener(s -> {
            User user = getUserFromTableRow();
            if (user != null) {
                Audit.log(Action.USER_DELETE);
                UserWriteForm userWriteForm = new UserWriteForm(this.mainForm, CUD.DELETE, user);
                userWriteForm.setVisible(true);
                loadLast500Users();
            }
        });
    }

    private void loadLast500Users() {
        try {
            ArrayList<User> users = userDAO.getLast500Users();
            createTable(users);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void search(String query) {
        try {
            // Llama al método 'search' del UserDAO para buscar usuarios cuya información
            // coincida con la cadena de búsqueda 'query'. La implementación específica
            ArrayList<User> users = userDAO.search(query);
            // Llama al método 'createTable' para actualizar la tabla de usuarios
            // en la interfaz gráfica con los resultados de la búsqueda.
            createTable(users);
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso de búsqueda
            // (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error al usuario.
            return; // Sale del método 'search' después de mostrar el error.
        }
    }
    public void createTable(ArrayList<User> users) {

        // Crea un nuevo modelo de tabla por defecto (DefaultTableModel).
        // Se sobrescribe el método isCellEditable para hacer que todas las celdas de la tabla no sean editables.
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Retorna false para indicar que ninguna celda debe ser editable.
            }
        };

        // Define las columnas de la tabla. Los nombres de las columnas corresponden
        // a los atributos que se mostrarán de cada objeto User.
        model.addColumn("Id");
        model.addColumn("Nombre");
        model.addColumn("Email");
        model.addColumn("Estatus");
        model.addColumn("Rol");


        // Establece el modelo de tabla creado como el modelo de datos para la
        // JTable 'tableUsers' (la tabla que se muestra en la interfaz gráfica).
        this.tableUsers.setModel(model);

        // Declara un array de objetos 'row' que se utilizará temporalmente para agregar filas.
        Object row[] = null;

        // Itera a través de la lista de objetos User proporcionada.
        for (int i = 0; i < users.size(); i++) {
            // Obtiene el objeto User actual de la lista.
            User user = users.get(i);
            // Agrega una nueva fila vacía al modelo de la tabla.
            model.addRow(row);
            // Establece el valor del ID del usuario en la celda correspondiente de la fila actual (columna 0).
            model.setValueAt(user.getId(), i, 0);
            // Establece el valor del nombre del usuario en la celda correspondiente de la fila actual (columna 1).
            model.setValueAt(user.getName(), i, 1);
            // Establece el valor del email del usuario en la celda correspondiente de la fila actual (columna 2).
            model.setValueAt(user.getEmail(), i, 2);
            // Establece el valor del estatus del usuario (probablemente obtenido a través de un método 'getStrEstatus()')
            // en la celda correspondiente de la fila actual (columna 3).
            model.setValueAt(user.getStrEstatus(), i, 3);
            model.setValueAt(user.getRoleName(), i, 4);  // Nombre del rol (no el ID)

        }

        // Llama al método 'hideCol' para ocultar la columna con índice 0 (la columna del ID).
        // Esto es común cuando el ID es necesario internamente pero no se quiere mostrar al usuario.
        hideCol(0);
    }

    private void hideCol(int pColumna) {
        // Obtiene el modelo de columnas de la JTable y establece el ancho máximo de la columna especificada a 0.
        // Esto hace que la columna no sea visible en la vista de datos de la tabla.
        this.tableUsers.getColumnModel().getColumn(pColumna).setMaxWidth(0);
        // Establece el ancho mínimo de la columna especificada a 0.
        // Esto asegura que la columna no ocupe espacio incluso si el layout manager intenta ajustarla.
        this.tableUsers.getColumnModel().getColumn(pColumna).setMinWidth(0);
        // Realiza las mismas operaciones para el encabezado de la tabla.
        // Esto asegura que el nombre de la columna también se oculte y no ocupe espacio en la parte superior de la tabla.
        this.tableUsers.getTableHeader().getColumnModel().getColumn(pColumna).setMaxWidth(0);
        this.tableUsers.getTableHeader().getColumnModel().getColumn(pColumna).setMinWidth(0);
    }

    // Método privado para obtener el objeto User seleccionado de la fila de la tabla.
    private User getUserFromTableRow() {
        User user = null; // Inicializa la variable user a null.
        try {
            // Obtiene el índice de la fila seleccionada en la tabla.
            int filaSelect = this.tableUsers.getSelectedRow();
            int id = 0; // Inicializa la variable id a 0.

            // Verifica si se ha seleccionado alguna fila en la tabla.
            if (filaSelect != -1) {
                // Si se seleccionó una fila, obtiene el valor de la primera columna  ID de esa fila.
                id = (int) this.tableUsers.getValueAt(filaSelect, 0);
            } else {
                // Si no se seleccionó ninguna fila, muestra un mensaje de advertencia al usuario.
                JOptionPane.showMessageDialog(null,
                        "Seleccionar una fila de la tabla.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null; // Retorna null ya que no se puede obtener un usuario sin una fila seleccionada.
            }

            // Llama al método 'getById' del UserDAO para obtener el objeto User correspondiente al ID obtenido de la tabla.
            user = userDAO.getByID(id);

            // Verifica si se encontró un usuario con el ID proporcionado.
            if (user.getId() == 0) {
                // Si el ID del usuario devuelto es 0 (o alguna otra indicación de que no se encontró),
                // muestra un mensaje de advertencia al usuario.
                JOptionPane.showMessageDialog(null,
                        "No se encontró ningún usuario.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null; // Retorna null ya que no se encontró ningún usuario con ese ID.
            }

            // Si se encontró un usuario, lo retorna.
            return user;
        } catch (Exception ex) {
            // Captura cualquier excepción que ocurra durante el proceso (por ejemplo, errores de base de datos).
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE); // Muestra un mensaje de error al usuario con la descripción de la excepción.
            return null; // Retorna null en caso de error.
        }
    }
}
