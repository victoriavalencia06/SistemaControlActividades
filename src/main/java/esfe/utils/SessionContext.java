package esfe.utils;

import esfe.dominio.User;
public class SessionContext {
    private static User currentUser;

    private SessionContext() {}

    public static void set(User u) {
        currentUser = u;
    }

    public static User get() {
        return currentUser;
    }

    public static int getUserId() {
        return currentUser != null ? currentUser.getId() : 0;
    }
}
