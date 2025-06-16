package esfe.Persistencia;

import esfe.dominio.UserHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserHistoryDAOTest {

    private UserHistoryDAO userHistoryDAO;

    @BeforeEach
    void setUp() {
        userHistoryDAO = new UserHistoryDAO();
    }

    @Test
    void testCreateGetByIDSearch() throws SQLException {
        int idUserExistente = 10; // Cambia por un idUser válido en tu BD
        String actionUnica = "TestAccion_" + System.currentTimeMillis(); // Acción única para evitar colisiones

        UserHistory nuevoHistorial = new UserHistory(
                0,
                idUserExistente,
                LocalDateTime.now(),
                actionUnica,
                1,
                "Detalles de prueba sin borrar"
        );

        // Crear el registro
        UserHistory creado = userHistoryDAO.create(nuevoHistorial);
        assertNotNull(creado, "El historial creado no debe ser nulo");
        assertTrue(creado.getIdHistory() > 0, "El idHistory debe ser mayor a cero");
        assertEquals(actionUnica, creado.getAction());

        // Obtener por ID
        UserHistory obtenido = userHistoryDAO.getByID(creado.getIdHistory());
        assertNotNull(obtenido, "No se encontró historial por ID");
        assertEquals(creado.getIdHistory(), obtenido.getIdHistory());
        assertEquals(actionUnica, obtenido.getAction());

        // Buscar por acción (usa LIKE, por eso puede devolver varios)
        ArrayList<UserHistory> resultadosBusqueda = userHistoryDAO.search(actionUnica);
        assertFalse(resultadosBusqueda.isEmpty(), "La búsqueda no debe devolver resultados vacíos");
        boolean encontrado = resultadosBusqueda.stream()
                .anyMatch(h -> actionUnica.equals(h.getAction()));
        assertTrue(encontrado, "Debe encontrarse la acción exacta en la búsqueda");

        // Nota: No se elimina el registro creado para respetar auditoría
    }
}
