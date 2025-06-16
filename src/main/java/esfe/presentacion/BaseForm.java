package esfe.presentacion;

import esfe.dominio.Action;
import esfe.utils.Audit;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseForm extends JDialog {
    public BaseForm(String formName) {
        Audit.log(Action.valueOf("FORM_OPEN_" + formName));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Audit.log(Action.valueOf("FORM_CLOSE_" + formName));
                dispose();
            }
        });
    }
}
