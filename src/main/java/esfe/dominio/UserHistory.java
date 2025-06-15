package esfe.dominio;

import java.time.LocalDateTime;

public class UserHistory {
    private int idHistory;
    private int idUser;
    private String action;
    private LocalDateTime timestamp;
    private int status;
    private String details;

    public UserHistory() {
    }

    public UserHistory(int idHistory, int idUser, LocalDateTime timestamp, String action, int status, String details) {
        this.idHistory = idHistory;
        this.idUser = idUser;
        this.timestamp = timestamp;
        this.action = action;
        this.status = status;
        this.details = details;
    }

    public int getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(int idHistory) {
        this.idHistory = idHistory;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStrStatus(){
        String str="";
        switch (status) {
            case 1:
                str = "ACTIVO";
                break;
            case 2:
                str = "INACTIVO";
                break;
            default:
                str = "";
        }
        return str;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
