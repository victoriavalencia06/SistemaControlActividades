package esfe.presentacion;

import esfe.Persistencia.RoleDAO;
import esfe.dominio.Role;
import esfe.utils.CBOption;
import esfe.utils.CUD;
import esfe.utils.Audit;
import esfe.dominio.Action;
import javax.swing.*;

public class RoleWriteForm extends BaseForm {
    private JPanel mainPanel;
    private JTextField txtNombre;
    private JComboBox comboEstado;
    private JTextField txtDescripcion;
    private JButton btnGuardar;
    private JButton btnCancelar;

    private RoleDAO roleDAO;
    private MainForm mainForm;
    private CUD cud;
    private Role role;

    public RoleWriteForm(MainForm mainForm, CUD cud, Role role) {
        super("RoleWriteForm"); // 👈 Esto activa registro de apertura y cierre
        this.mainForm = mainForm;
        this.cud = cud;
        this.role = role;
        this.roleDAO = new RoleDAO();

        setContentPane(mainPanel);
        setModal(true);
        init();
        pack();
        setLocationRelativeTo(mainForm);

        btnCancelar.addActionListener(e -> this.dispose());
        btnGuardar.addActionListener(e -> ok());
    }

    private void init() {
        initCBEstado();

        switch (this.cud) {
            case CREATE:
                setTitle("Crear Rol");
                btnGuardar.setText("Guardar");
                break;
            case UPDATE:
                setTitle("Modificar Rol");
                btnGuardar.setText("Guardar");
                break;
            case DELETE:
                setTitle("Eliminar Rol");
                btnGuardar.setText("Eliminar");
                break;
        }

        setValuesControls(this.role);
    }

    private void initCBEstado() {
        DefaultComboBoxModel<CBOption> model = (DefaultComboBoxModel<CBOption>) comboEstado.getModel();
        model.removeAllElements();
        model.addElement(new CBOption("ACTIVO", (byte) 1));
        model.addElement(new CBOption("INACTIVO", (byte) 2));
    }

    private void setValuesControls(Role role) {
        txtNombre.setText(role.getName());
        txtDescripcion.setText(role.getDescription());
        comboEstado.setSelectedItem(new CBOption(null, role.getStatus()));

        if (this.cud == CUD.CREATE) {
            comboEstado.setSelectedItem(new CBOption(null, (byte) 1));
        }

        if (this.cud == CUD.DELETE) {
            txtNombre.setEditable(false);
            txtDescripcion.setEditable(false);
            comboEstado.setEnabled(false);
        }
    }

    private boolean getValuesControls() {
        boolean res = false;

        CBOption selectedStatus = (CBOption) comboEstado.getSelectedItem();
        byte status = selectedStatus != null ? (byte) selectedStatus.getValue() : 0;

        if (txtNombre.getText().trim().isEmpty()) {
            return res;
        } else if (status == 0) {
            return res;
        } else if (this.cud != CUD.CREATE && this.role.getIdRole() == 0) {
            return res;
        }

        this.role.setName(txtNombre.getText().trim());
        this.role.setDescription(txtDescripcion.getText().trim());
        this.role.setStatus(status);

        res = true;
        return res;
    }

    private void ok() {
        try {
            boolean valid = getValuesControls();
            if (!valid) {
                JOptionPane.showMessageDialog(null,
                        "Los campos con * son obligatorios",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean result = false;

            switch (this.cud) {
                case CREATE:
                    Role r = roleDAO.create(this.role);
                    if (r.getIdRole() > 0) {
                        result = true;
                    }
                    break;
                case UPDATE:
                    result = roleDAO.update(this.role);
                    break;
                case DELETE:
                    result = roleDAO.delete(this.role);
                    break;
            }

            if (result) {
                // 🔍 Registrar acción de historial
                switch (this.cud) {
                    case CREATE:
                        Audit.log(Action.ROLE_CREATE_SUCCESS);
                        break;
                    case UPDATE:
                        Audit.log(Action.ROLE_UPDATE_SUCCESS);
                        break;
                    case DELETE:
                        Audit.log(Action.ROLE_DELETE_SUCCESS);
                        break;
                }

                JOptionPane.showMessageDialog(null,
                        "Transacción realizada exitosamente",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "No se logró realizar ninguna acción",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "ERROR", JOptionPane.ERROR_MESSAGE);

        }
    }
}
