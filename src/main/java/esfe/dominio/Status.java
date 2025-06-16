package esfe.dominio;

public enum Status {
    ACTIVO(1), INACTIVO(2);
    public final int code;
    Status(int code) { this.code = code; }
}
