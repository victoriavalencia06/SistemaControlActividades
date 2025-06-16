package esfe.presentacion;

import esfe.Persistencia.UserHistoryDAO;
import esfe.dominio.UserHistory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Importa la clase DefaultTableModel, utilizada para crear y manipular modelos de datos para JTable.
import java.awt.event.KeyAdapter; // Importa la clase KeyAdapter, una clase adaptadora para recibir eventos de teclado.
import java.awt.event.KeyEvent; // Importa la clase KeyEvent, que representa un evento de teclado.
import java.sql.SQLException;
import java.util.ArrayList;


public class UserHistoryForm extends BaseForm{
    private JPanel mainPanel;
    private JTextField txtNombreUsuario;
    private JTable tbUserHistory;

    private UserHistoryDAO userHistoryDAO;
    private MainForm mainForm;

    public UserHistoryForm(MainForm mainForm){
        super("UserHistoryForm");
        this.mainForm = mainForm;
        this.userHistoryDAO = new UserHistoryDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Buscar Historial de usuario");
        pack();
        setSize(615, 575); // Ajusta estas medidas a lo que prefieras
        System.out.println(getSize());
        setLocationRelativeTo(mainForm);

        // Aquí carga los últimos 500 registros al abrir el formulario
        try {
            ArrayList<UserHistory> lastRecords = userHistoryDAO.getLastRecords(500);
            createTable(lastRecords);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos iniciales: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }

        txtNombreUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtNombreUsuario.getText().trim().isEmpty()) {
                    search(txtNombreUsuario.getText().trim());
                } else {
                    tbUserHistory.setModel(new DefaultTableModel());
                }
            }
        });
    }

    private void search(String query) {
        try {
            ArrayList<UserHistory> userHistories = userHistoryDAO.search(query);
            createTable(userHistories);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void createTable(ArrayList<UserHistory> userHistories) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Usuario");
        model.addColumn("Accion");
        model.addColumn("Fecha/hora");
        model.addColumn("Estatus");
        model.addColumn("Detalles");

        tbUserHistory.setModel(model);
        Object[] row = new Object[6];

        for (int i = 0; i < userHistories.size(); i++) {
            UserHistory r = userHistories.get(i);
            model.addRow(row);
            model.setValueAt(r.getIdHistory(), i, 0);
            model.setValueAt(r.getUserName(), i, 1);  // muestra nombre en lugar de id
            model.setValueAt(r.getAction(), i, 2);
            model.setValueAt(r.getTimestamp(), i, 3);
            model.setValueAt(r.getStrStatus(), i, 4);
            model.setValueAt(r.getDetails(), i, 5);
        }

        hideCol(0);
        ajustarAnchoColumnas();
    }
    private void hideCol(int colIndex) {
        tbUserHistory.getColumnModel().getColumn(colIndex).setMaxWidth(0);
        tbUserHistory.getColumnModel().getColumn(colIndex).setMinWidth(0);
        tbUserHistory.getTableHeader().getColumnModel().getColumn(colIndex).setMaxWidth(0);
        tbUserHistory.getTableHeader().getColumnModel().getColumn(colIndex).setMinWidth(0);
    }
    private void ajustarAnchoColumnas() {
        // Ajustar columna "Detalles" (índice 5)
        tbUserHistory.getColumnModel().getColumn(5).setPreferredWidth(60);
        tbUserHistory.getColumnModel().getColumn(5).setMaxWidth(60);
        tbUserHistory.getColumnModel().getColumn(5).setMinWidth(60);

        // Ajustar columna "Estatus" (índice 4)
        tbUserHistory.getColumnModel().getColumn(4).setPreferredWidth(60);
        tbUserHistory.getColumnModel().getColumn(4).setMaxWidth(60);
        tbUserHistory.getColumnModel().getColumn(4).setMinWidth(60);

        // Ajustar columna "timestamp" (índice 3)
        tbUserHistory.getColumnModel().getColumn(3).setPreferredWidth(150);
        tbUserHistory.getColumnModel().getColumn(3).setMaxWidth(150);
        tbUserHistory.getColumnModel().getColumn(3).setMinWidth(150);

        // Ajustar columna "Usuario" (índice 1)
        tbUserHistory.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbUserHistory.getColumnModel().getColumn(1).setMaxWidth(100);
        tbUserHistory.getColumnModel().getColumn(1).setMinWidth(100);
    }
}
