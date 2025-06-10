package esfe.Persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import esfe.dominio.Role;
import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class RoleDAOTest {
    private RoleDAO roleDAO;

    @BeforeEach
    void setUp() {
        roleDAO = new RoleDAO();
    }

    private Role create(Role role) throws SQLException {
        Role res = roleDAO.create(role);

        assertNotNull(res, "El rol creado no debería ser nulo.");
        assertEquals(role.getName(), res.getName(), "El nombre del rol creado debe ser igual al original.");
        assertEquals(role.getStatus(), res.getStatus(), "El status del rol creado debe ser igual al original.");
        assertEquals(role.getDescription(), res.getDescription(), "La descripción del rol creado debe ser igual al original.");

        return res;
    }

    private void update(Role role) throws SQLException {
        String originalName = role.getName();
        String originalDescription = role.getDescription();
        int originalStatus = role.getStatus();

        role.setName(role.getName() + "_u");
        role.setDescription("updated_" + role.getDescription());
        role.setStatus(role.getStatus() == 1 ? 2 : 1); // Cambia el estado

        boolean res = roleDAO.update(role);
        assertTrue(res, "La actualización del rol debería ser exitosa.");

        getById(role);

        // Restaurar valores originales para otras pruebas
        role.setName(originalName);
        role.setDescription(originalDescription);
        role.setStatus(originalStatus);
    }

    private void getById(Role role) throws SQLException {
        Role res = roleDAO.getByID(role.getIdRole());

        assertNotNull(res, "El rol obtenido por ID no debería ser nulo.");
        assertEquals(role.getIdRole(), res.getIdRole(), "El ID del rol obtenido debe ser igual al original.");
        assertEquals(role.getName(), res.getName(), "El nombre del rol obtenido debe ser igual al esperado.");
        assertEquals(role.getStatus(), res.getStatus(), "El status del rol obtenido debe ser igual al esperado.");
        assertEquals(role.getDescription(), res.getDescription(), "La descripción del rol obtenido debe ser igual al esperado.");
    }

    private void search(Role role) throws SQLException {
        ArrayList<Role> roles = roleDAO.search(role.getName());
        boolean find = false;

        for (Role roleItem : roles) {
            if (roleItem.getName().contains(role.getName())) {
                find = true;
            } else {
                find = false;
                break;
            }
        }
        assertTrue(find, "El nombre buscado no fue encontrado: " + role.getName());
    }

    private void delete(Role role) throws SQLException {
        boolean res = roleDAO.delete(role);
        assertTrue(res, "La eliminación del rol debería ser exitosa.");

        Role res2 = roleDAO.getByID(role.getIdRole());
        assertNull(res2, "El rol debería haber sido eliminado y no encontrado por su ID.");
    }

    @Test
    void testRoleDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String roleName = "TestRole" + num;

        Role role = new Role(0, roleName, 1, "Descripción de prueba");

        Role testRole = create(role);
        getById(testRole);
        update(testRole);
        search(testRole);
        delete(testRole);
    }

    @Test
    void createRole() throws SQLException {
        Role role = new Role(0, "Admin", 1, "Rol de administrador");
        Role res = roleDAO.create(role);
        assertNotNull(res);
    }
}
