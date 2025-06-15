package esfe.Persistencia;

import esfe.dominio.UserHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UserHistoryDAOTest {
    private UserHistoryDAO userHistoryDAO;

    @BeforeEach
    void setUp() {
        userHistoryDAO = new UserHistoryDAO();
    }

    private UserHistory create(UserHistory history) throws SQLException {
        UserHistory res = userHistoryDAO.create(history);

        assertNotNull(res, "El historial creado no debería ser nulo.");
        assertEquals(history.getIdUser(), res.getIdUser(), "El idUser debe coincidir.");
        assertEquals(history.getAction(), res.getAction(), "La acción debe coincidir.");
        assertEquals(history.getStatus(), res.getStatus(), "El estado debe coincidir.");
        assertEquals(history.getDetails(), res.getDetails(), "Los detalles deben coincidir.");

        // Verifica timestamp solo si no es null
        if (history.getTimestamp() != null) {
            assertNotNull(res.getTimestamp(), "El timestamp no debería ser nulo.");
            assertEquals(history.getTimestamp().withNano(0), res.getTimestamp().withNano(0), "El timestamp debe coincidir.");
        }

        return res;
    }

    private void update(UserHistory history) throws SQLException {
        int originalStatus = history.getStatus();
        String originalAction = history.getAction();
        String originalDetails = history.getDetails();
        LocalDateTime originalTimestamp = history.getTimestamp();

        history.setAction(history.getAction() + "_updated");
        history.setDetails("updated_" + history.getDetails());
        history.setStatus(history.getStatus() == 1 ? 2 : 1);
        history.setTimestamp(LocalDateTime.now());

        boolean res = userHistoryDAO.update(history);
        assertTrue(res, "La actualización del historial debería ser exitosa.");

        getById(history);

        // Restaurar para siguientes pruebas
        history.setAction(originalAction);
        history.setDetails(originalDetails);
        history.setStatus(originalStatus);
        history.setTimestamp(originalTimestamp);
    }

    private void getById(UserHistory history) throws SQLException {
        UserHistory res = userHistoryDAO.getByID(history.getIdHistory());

        assertNotNull(res, "El historial no debería ser nulo.");
        assertEquals(history.getIdHistory(), res.getIdHistory());
        assertEquals(history.getIdUser(), res.getIdUser());
        assertEquals(history.getAction(), res.getAction());
        assertEquals(history.getStatus(), res.getStatus());
        assertEquals(history.getDetails(), res.getDetails());

        if (history.getTimestamp() != null && res.getTimestamp() != null) {
            assertEquals(history.getTimestamp().withNano(0), res.getTimestamp().withNano(0));
        }
    }

    private void search(UserHistory history) throws SQLException {
        ArrayList<UserHistory> result = userHistoryDAO.search(history.getAction());
        assertFalse(result.isEmpty(), "La búsqueda no devolvió resultados.");
        boolean found = result.stream().anyMatch(h -> h.getAction().contains(history.getAction()));
        assertTrue(found, "No se encontró una acción que coincida.");
    }

    private void delete(UserHistory history) throws SQLException {
        boolean res = userHistoryDAO.delete(history);
        assertTrue(res, "La eliminación debería ser exitosa.");

        UserHistory deleted = userHistoryDAO.getByID(history.getIdHistory());
        assertNull(deleted, "El historial debería haber sido eliminado.");
    }

    @Test
    void testUserHistoryDAO() throws SQLException {
        Random random = new Random();
        int idUser = random.nextInt(1000) + 1;
        String action = "LoginTest" + idUser;

        UserHistory history = new UserHistory(
                0, // idHistory
                idUser,
                LocalDateTime.now(),
                action,
                1,
                "Detalles de prueba"
        );

        UserHistory testHistory = create(history);
        getById(testHistory);
        update(testHistory);
        search(testHistory);
        delete(testHistory);
    }

    @Test
    void createHistoryWithNullTimestamp() throws SQLException {
        UserHistory history = new UserHistory(
                0,
                123,
                null,
                "NullTimestampAction",
                1,
                "Historial sin timestamp"
        );

        UserHistory res = userHistoryDAO.create(history);
        assertNotNull(res, "El historial creado no debe ser nulo.");
        assertNull(res.getTimestamp(), "El timestamp debe ser null.");
    }
}
