package esfe.presentacion;

import esfe.Persistencia.RoleDAO;
import esfe.dominio.Role;
import esfe.utils.CUD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class RoleForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtNombre;
    private JButton btnCrear;
    private JTable tableRol;
    private JButton btnModificar;
    private JButton btnEliminar;

    private RoleDAO roleDAO;
    private MainForm mainForm;

    public RoleForm(MainForm mainForm) {
        this.mainForm = mainForm;
        this.roleDAO = new RoleDAO();

        setContentPane(mainPanel);
        setModal(true);
        setTitle("Buscar Rol");
        pack();
        setLocationRelativeTo(mainForm);

        txtNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtNombre.getText().trim().isEmpty()) {
                    search(txtNombre.getText().trim());
                } else {
                    tableRol.setModel(new DefaultTableModel());
                }
            }
        });

        btnCrear.addActionListener(e -> {
            RoleWriteForm form = new RoleWriteForm(mainForm, CUD.CREATE, new Role());
            form.setVisible(true);
            tableRol.setModel(new DefaultTableModel()); // refrescar
        });

        btnModificar.addActionListener(e -> {
            Role role = getRoleFromTableRow();
            if (role != null) {
                RoleWriteForm form = new RoleWriteForm(mainForm, CUD.UPDATE, role);
                form.setVisible(true);
                tableRol.setModel(new DefaultTableModel()); // refrescar
            }
        });

        btnEliminar.addActionListener(e -> {
            Role role = getRoleFromTableRow();
            if (role != null) {
                RoleWriteForm form = new RoleWriteForm(mainForm, CUD.DELETE, role);
                form.setVisible(true);
                tableRol.setModel(new DefaultTableModel()); // refrescar
            }
        });
    }

    private void search(String query) {
        try {
            ArrayList<Role> roles = roleDAO.search(query);
            createTable(roles);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createTable(ArrayList<Role> roles) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Nombre");
        model.addColumn("Descripción");
        model.addColumn("Estatus");

        tableRol.setModel(model);
        Object[] row = new Object[4];

        for (int i = 0; i < roles.size(); i++) {
            Role r = roles.get(i);
            model.addRow(row);
            model.setValueAt(r.getIdRole(), i, 0);
            model.setValueAt(r.getName(), i, 1);
            model.setValueAt(r.getDescription(), i, 2);
            model.setValueAt(r.getStrEstatus(), i, 3);
        }

        hideCol(0);
    }

    private void hideCol(int colIndex) {
        tableRol.getColumnModel().getColumn(colIndex).setMaxWidth(0);
        tableRol.getColumnModel().getColumn(colIndex).setMinWidth(0);
        tableRol.getTableHeader().getColumnModel().getColumn(colIndex).setMaxWidth(0);
        tableRol.getTableHeader().getColumnModel().getColumn(colIndex).setMinWidth(0);
    }

    private Role getRoleFromTableRow() {
        try {
            int selectedRow = tableRol.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null,
                        "Selecciona una fila de la tabla.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            int id = (int) tableRol.getValueAt(selectedRow, 0);
            Role role = roleDAO.getByID(id);

            if (role.getIdRole() == 0) {
                JOptionPane.showMessageDialog(null,
                        "No se encontró ningún rol.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return role;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            return null;

        }
    }
}
