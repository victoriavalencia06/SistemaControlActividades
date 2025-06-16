package esfe.utils;

import esfe.Persistencia.UserHistoryDAO;
import esfe.dominio.*;

import java.time.LocalDateTime;
public class Audit {
    private static final UserHistoryDAO dao = new UserHistoryDAO();

    private Audit() {}

    public static void log(Action action) {
        log(action, Status.ACTIVO, "");
    }

    public static void log(Action action, Status status, String details) {
        try {
            dao.create(new UserHistory(
                    0,
                    SessionContext.getUserId(),
                    LocalDateTime.now(),
                    action.name(),
                    status.code,
                    details
            ));
        } catch (Exception ex) {
            ex.printStackTrace(); // Reemplazar por Logger si deseas
        }
    }
}
