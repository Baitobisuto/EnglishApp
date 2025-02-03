package com.example.englishapp;

import android.os.StrictMode;
import android.util.Log;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLDatabaseManager {
    private static final String SSH_HOST = "mvs.sytes.net";
    private static final int SSH_PORT = 11160;
    private static final String SSH_USER = "sshuser";
    private static final String SSH_PASSWD = "1234";
    private static final String SSH_PRIVATE_KEY = "-----BEGIN OPENSSH PRIVATE KEY-----\n" +
            "b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAABlwAAAAdzc2gtcn\n" +
            "NhAAAAAwEAAQAAAYEAxzsxXgczgOq+WwqyDB6ZrA7LpquDYxYnYy0Zmc7omgKbKhMFKD7N\n" +
            "o3VSDo0igUNJjkNhsyAfPDAn1EFYllj/ERkIzqAhJJgi+ymnnTMPu9LO7i5QsR64qQIILQ\n" +
            "6d9jqnj+eBWFXg/J7pjeBAp9tdkYnfEXILs50111M+FMjRZMADIHtpfjyNAUq6k08SAu8v\n" +
            "gLCoVP3ZVB/nADadSwhdEEFE0joxqtfDU0ldYhcxWxv7GstwJNgNNwvqZSHe/QCLrNm5MD\n" +
            "TCNTXlqM3GSixwSMGL0TFKp/xK39eof5uLhK/WwiDGPUZnR/8YhTwpG1nZ6gVQ0byYKPoK\n" +
            "lI8Vnc5Uk9JDrhux/+AWvl+Vy1rwe31DkAl7GeGE9N29Nho05lkOWkDfRYsHo2YTLXEbl8\n" +
            "iuPPIjx+ZTW4AHv0nDHIHuO6SDQdyZsNrzQYJAMpYkarPf2aSVQ8XxAlFF+DIEo1BZjj/v\n" +
            "idYvU5rhKS2wj12xD/gF9VXbybYzIYN64ooCGeHRAAAFiM7o+cbO6PnGAAAAB3NzaC1yc2\n" +
            "EAAAGBAMc7MV4HM4DqvlsKsgwemawOy6arg2MWJ2MtGZnO6JoCmyoTBSg+zaN1Ug6NIoFD\n" +
            "SY5DYbMgHzwwJ9RBWJZY/xEZCM6gISSYIvspp50zD7vSzu4uULEeuKkCCC0OnfY6p4/ngV\n" +
            "hV4Pye6Y3gQKfbXZGJ3xFyC7OdNddTPhTI0WTAAyB7aX48jQFKupNPEgLvL4CwqFT92VQf\n" +
            "5wA2nUsIXRBBRNI6MarXw1NJXWIXMVsb+xrLcCTYDTcL6mUh3v0Ai6zZuTA0wjU15ajNxk\n" +
            "oscEjBi9ExSqf8St/XqH+bi4Sv1sIgxj1GZ0f/GIU8KRtZ2eoFUNG8mCj6CpSPFZ3OVJPS\n" +
            "Q64bsf/gFr5flcta8Ht9Q5AJexnhhPTdvTYaNOZZDlpA30WLB6NmEy1xG5fIrjzyI8fmU1\n" +
            "uAB79JwxyB7jukg0HcmbDa80GCQDKWJGqz39mklUPF8QJRRfgyBKNQWY4/74nWL1Oa4Skt\n" +
            "sI9dsQ/4BfVV28m2MyGDeuKKAhnh0QAAAAMBAAEAAAGABMPiLOUQMsuqjOPnGUocE9Twww\n" +
            "ZEKLkNbR5Vh+u+fXyFeI6m520FHx09vrJaemwBFOGAszeX96kRMs7zTvlG9II8RzDmSglH\n" +
            "gOlgpG5luHDU2TaLYIkjEovFZUp9tbd2bYc0fUfkjzY/0NBly34AMWxyROkM5Zljgchwai\n" +
            "/Zbkb3bC33enj5SGggqUhRXneZE8Nl6059BbmTnVoxDevfyfU9r6eLq5HojpPpI2D5obu8\n" +
            "GEp4XrE6M7BZxVx3iGacZc8QjhICjaMZs8p3UZVRh730PZY26tZtAloMU9T6tmH16dtiXB\n" +
            "47hT5hgpp22Acb5auE3buIjOsHsnpEpEsWgUpEVNmAKYZQHnQgy2mkLVeOojxk4GWcI7EZ\n" +
            "Izj8o8bVt+aG5h1JTS24ujWHe0D5wqyL7HupO7mors7OkcAPF9jyas6z+XHV/ufwcFSeq2\n" +
            "4nS0zGFrV4gJDLB6W84+mMSI7k7dHtTXLOdNQe4MvOARjAL67I9UQGsRJ5+LXT/qDnAAAA\n" +
            "wEOTvBfMWdAs+LcjtjvcxYEULZok5d1oOtLXE/ThVSgTH4i5TU4jyO7i8TjZ7iJWtlAePf\n" +
            "q8HoFviUD78eHNkZdzREkgilPvnwVGnvIHa9+goQxuaDLzR1LdF4IojKT/tSaj+a4pwx/3\n" +
            "YaVK6tn5PupWfOt1m+mUKxwg3TebNsC0wxCibnbEYLwN0lrPbE0WNdxteECd+rQtKzFyc9\n" +
            "LCqe0AskuORX3ZGx88VMj+64mBcM5la24ospxvs4TFDtDkywAAAMEA7e9dJxYS6zHn4nTp\n" +
            "onE6Rpm6/Fx8mSWTf4y7VSVWGk1pdc9tahfLvHlTGM+b8nnF/uyIrJe2RiCWSDMR260U37\n" +
            "EN04b5m5ktMzWwgCj+IOqX1HOwOJSfxU3mHKIPpwioUWF1cBTlaWZUotn2WZVMFUUzgAQe\n" +
            "55DioSCWwx6OmCIYZTu1H1J176iKSDltOAeTPxU6wdaaCfWeVJf/2gmSzWocC4yenc7gUm\n" +
            "RRbsOhmP3PTpy/2qSHXHgOOwAQi9FDAAAAwQDWW4+KSxY8mTU9Ig0SHDP1AtPDsLYKsyNd\n" +
            "gcWUuzP33uh4Hq7kj26i+YWRqiF0Ak4YblqubcHiQ8FNGZtOi5n01zUueMqEC9l+thAoa0\n" +
            "GlqSGEtLkH7VlBUx0xIUh7F6WG1P5t+DrPwdzEMxUcJ/LpUd+JMlu/0AxMi22/mpm2nGxf\n" +
            "LlyAQKO90FhfSbBpfrJwk4lNAqiKj34VopYyIuVwiSaWPKSnu9mz0vk7HGuXhH9Bt0ok92\n" +
            "JCxYR34gUUFVsAAAAQc3NodXNlckBzZXJ2ZXIxNwECAw==\n" +
            "-----END OPENSSH PRIVATE KEY-----";

    private static final String DB_HOST = "127.0.0.1";
    private static final int DB_PORT = 5432;
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";
    private static final String DB_NAME = "englishapp";

    private static int localPort; // Puerto local asignado dinámicamente para el reenvío de puertos
    private static Session sshSession; // Sesión SSH para manejar el túnel

    /**
     * Establece una conexión a la base de datos PostgreSQL a través de un túnel SSH.
     *
     * @return Una referencia a la conexión a la base de datos.
     * @throws SQLException Si ocurre un error durante la conexión.
     */
    public static Connection connect() throws SQLException {
        JSch jsch = new JSch();
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        try {
            // Agregar clave privada
            jsch.addIdentity("key",SSH_PRIVATE_KEY.getBytes(), null, null);

            // Configuración de la sesión SSH
            sshSession = jsch.getSession(SSH_USER,SSH_HOST, SSH_PORT);
            sshSession.setPassword(SSH_PASSWD);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            //config.put("PreferredAuthentications", "password");
            sshSession.setConfig(config);
            sshSession.connect();

            // Configurar el reenvío de puertos
            localPort = sshSession.setPortForwardingL(0, DB_HOST, DB_PORT);

            // Establecer la conexión a la base de datos PostgreSQL
            String jdbcUrl = "jdbc:postgresql://localhost:" + localPort + "/" + DB_NAME;
            Connection connection = DriverManager.getConnection(jdbcUrl, DB_USER, DB_PASSWORD);

            if (connection == null) {
                System.out.println("No se pudo conectar a la base de datos PostgreSQL. Asegúrate de que la URL y las credenciales sean correctas.");
            }
            return connection;
        } catch (Exception e) {
            Log.i("eror",e.getMessage());
            throw new SQLException("Error al conectar con la base de datos PostgreSQL a través de SSH.");
        }
    }

    /**
     * Cierra la conexión a la base de datos PostgreSQL y la sesión SSH.
     *
     * @param connection La conexión que se debe cerrar.
     * @throws SQLException Si ocurre un error durante la desconexión.
     */
    public static void disconnect(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        if (sshSession != null && sshSession.isConnected()) {
            sshSession.disconnect();
        }
    }

    // Método para comprobar la conexión
    public static boolean checkConnection() {
        boolean connectedOK = false;

        while (!connectedOK) {
            try {
                Connection connection = connect();
                if (connection != null && !connection.isClosed()) {
                    connectedOK = true;
                    disconnect(connection);
                }
            } catch (SQLException e) {

            }
        }
        return connectedOK;
    }
}