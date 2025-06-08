package esfe.utils;

import java.nio.charset.StandardCharsets; // Clase que define juegos de caracteres estandar, como UTF-8, utilizado para codificar la contraseña
import java.security.MessageDigest; // Clase que proporciona funcionalidades para algoritmos de mensajes criptográficos, como SHA
import java.security.NoSuchAlgorithmException; // Clase para manejar excepciones que ocurren cuando un algoritmo criptográfico solicitado no está
import java.util.Base64;

public class PasswordHasher {
    /**
     * Hashea una contraseña utilizando el algoritmoo SHA-256 y la codifica en Base64.
     *
     * @param password La contraseña en texto que se va a hashear.
     * @return Una cadena que representa la contraseña hasheada y codificada en Base64.
     * Rertorna null si el algoritmo SHA-256 no está disponible en el entorno.
     */
    public static String hashPassword(String password) {
        try {
            // Obtener una instancia del algoritmo de resumen de mensaje SHA-256.
            MessageDigest digest = MessageDigest.getInstance( "SHA-256");

            // Convierte la contraseña a bytes utilizando la codificación UTF-8 y calcula su hash.
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Codifica el array  de bytes del hash resultante a una cadena utilizando la codificación Base64
            // para que sea facilmente almacenable y transportble.
            return  Base64.getEncoder().encodeToString(hashBytes);

        }catch (NoSuchAlgorithmException ex){
            // Captura la excepción que ocurre si el algoritmo SHA-256 no está disponible.
            // En este caso, retorna null para indicar que el hash falló.
            return null;
        }
    }

}
