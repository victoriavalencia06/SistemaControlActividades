package esfe.Persistencia;

import esfe.dominio.UserHistory;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class UserHistoryDAO {
    private final ConnectionManager conn;

    public UserHistoryDAO() {
        conn = ConnectionManager.getInstance();
    }

    public UserHistory create(UserHistory history) throws SQLException {
        String sql = "INSERT INTO UserHistory (idUser, action, timestamp, status, details) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, history.getIdUser());
            ps.setString(2, history.getAction());
            ps.setTimestamp(3, history.getTimestamp() != null ? Timestamp.valueOf(history.getTimestamp()) : null);
            ps.setInt(4, history.getStatus());
            ps.setString(5, history.getDetails());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating history failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return getByID(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating history failed, no ID obtained.");
                }
            }
        }
    }

    public ArrayList<UserHistory> search(String userName) throws SQLException {
        String sql = "SELECT uh.*, u.name AS userName " +
                "FROM UserHistory uh " +
                "JOIN Users u ON uh.idUser = u.idUser " +
                "WHERE u.name LIKE ?";

        ArrayList<UserHistory> historyList = new ArrayList<>();
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, "%" + userName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historyList.add(mapResultSet(rs));
                }
            }
        }
        return historyList;
    }

    public UserHistory getByID(int id) throws SQLException {
        String sql = "SELECT uh.*, u.name AS userName " +
                "FROM UserHistory uh " +
                "JOIN Users u ON uh.idUser = u.idUser " +
                "WHERE uh.idHistory = ?";


        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }

    private UserHistory mapResultSet(ResultSet rs) throws SQLException {
        UserHistory history = new UserHistory(
                rs.getInt("idHistory"),
                rs.getInt("idUser"),
                rs.getTimestamp("timestamp") != null ? rs.getTimestamp("timestamp").toLocalDateTime() : null,
                rs.getString("action"),
                rs.getInt("status"),
                rs.getString("details")
        );
        history.setUserName(rs.getString("userName"));  // asigna el nombre aquí
        return history;
    }

    //Carga los ultimos 500 registros
    public ArrayList<UserHistory> getLastRecords(int limit) throws SQLException {
        String sql = "SELECT TOP " + limit + " uh.*, u.name AS userName " +
                "FROM UserHistory uh " +
                "JOIN Users u ON uh.idUser = u.idUser " +
                "ORDER BY uh.timestamp DESC";
        ArrayList<UserHistory> historyList = new ArrayList<>();
        try (
                Connection connection = conn.connect();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    historyList.add(mapResultSet(rs));
                }
            }
        }
        return historyList;
    }

}
