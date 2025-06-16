package esfe.Persistencia;

import esfe.dominio.Role;

import java.sql.*;
import java.util.ArrayList;

public class RoleDAO {
    private final ConnectionManager conn;

    public RoleDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Role create(Role role) throws SQLException {
        String sql = "INSERT INTO [Role] (name, status, description) VALUES (?, ?, ?)";
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, role.getName());
            ps.setInt(2, role.getStatus());
            ps.setString(3, role.getDescription());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating role failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return getByID(generatedId);
                } else {
                    throw new SQLException("Creating role failed, no ID obtained.");
                }
            }
        }
    }

    public boolean update(Role role) throws SQLException {
        String sql = "UPDATE [Role] SET name = ?, status = ?, description = ? WHERE idRole = ?";
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, role.getName());
            ps.setInt(2, role.getStatus());
            ps.setString(3, role.getDescription());
            ps.setInt(4, role.getIdRole());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(Role role) throws SQLException {
        String sql = "DELETE FROM [Role] WHERE idRole = ?";
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, role.getIdRole());
            return ps.executeUpdate() > 0;
        }
    }

    public ArrayList<Role> search(String name) throws SQLException {
        String sql = "SELECT idRole, name, status, description FROM [Role] WHERE name LIKE ?";
        ArrayList<Role> roles = new ArrayList<>();
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Role role = new Role(
                            rs.getInt("idRole"),
                            rs.getString("name"),
                            rs.getInt("status"),
                            rs.getString("description")
                    );
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public Role getByID(int id) throws SQLException {
        String sql = "SELECT idRole, name, status, description FROM [Role] WHERE idRole = ?";
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(
                            rs.getInt("idRole"),
                            rs.getString("name"),
                            rs.getInt("status"),
                            rs.getString("description")
                    );
                }
            }
        }
        return null;
    }

    public ArrayList<Role> getFirstRoles(int limit) throws SQLException {
        ArrayList<Role> roles = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Role ORDER BY idRole";

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Role r = new Role();
                    r.setIdRole(rs.getInt("idRole"));
                    r.setName(rs.getString("name"));
                    r.setDescription(rs.getString("description"));
                    r.setStatus(rs.getInt("status"));
                    roles.add(r);
                }
            }
        }
        return roles;
    }

}
