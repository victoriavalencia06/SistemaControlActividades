package esfe.dominio;

public enum Action {
    LOGIN_OK, LOGIN_FAIL, LOGOUT,
    USER_CREATE, USER_UPDATE, USER_DELETE, USER_PASSWORD_CHANGE,
    ROLE_CREATE, ROLE_UPDATE, ROLE_DELETE,
    FORM_OPEN_LoginForm, FORM_CLOSE_LoginForm,
    FORM_OPEN_RoleForm, FORM_CLOSE_RoleForm,
    FORM_OPEN_UserWriteForm, FORM_CLOSE_UserWriteForm,
    FORM_OPEN_ChangePasswordForm, FORM_CLOSE_ChangePasswordForm,
    ROLE_CREATE_SUCCESS, ROLE_UPDATE_SUCCESS, ROLE_DELETE_SUCCESS,
    USER_CREATE_SUCCESS, USER_UPDATE_SUCCESS, USER_DELETE_SUCCESS,
    FORM_OPEN_UserReadingForm, FORM_CLOSE_UserReadingForm,
    FORM_OPEN_RoleWriteForm, FORM_CLOSE_RoleWriteForm,
    FORM_OPEN_UserHistoryForm, FORM_CLOSE_UserHistoryForm,
    // …Agrega más según tus formularios
}
