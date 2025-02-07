package com.example.englishapp.DAO;

import android.content.Context;

import com.example.englishapp.ConexionMethods;
import com.example.englishapp.SQLDatabaseManager;
import com.example.englishapp.Usuario;
import com.jcraft.jsch.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuariosDAO extends ConexionMethods {

    private final Context context;

    public UsuariosDAO(Context context) {
        this.context = context;
    }

    public void createUsuariosTable() {
        String query = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id_usuario SERIAL PRIMARY KEY,"
                + "nombre VARCHAR(255) NOT NULL,"
                + "apellidos VARCHAR(255) NOT NULL,"
                + "username VARCHAR(255) NOT NULL UNIQUE,"
                + "password VARCHAR(255) NOT NULL,"
                + "avatar INTEGER,"
                + "puntuacion INTEGER DEFAULT 0,"
                + "vidas INTEGER DEFAULT 3"
                + ");";

        try (Connection connection = SQLDatabaseManager.connect(); // Usar SQLDatabaseManager.connect()
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(query);
            System.out.println("Tabla 'usuarios' creada correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean usuarioExiste(String username) {
        boolean exists = false;

        try (Connection conn = SQLDatabaseManager.connect(); // Try-with-resources
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM usuarios WHERE username = ?")) {

            if (conn == null) return false; // Check for null connection

            ps.setString(1, username);
            try (ResultSet resultSet = ps.executeQuery()) { // Try-with-resources
                if (resultSet.next()) {
                    exists = true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return exists;
    }

    public Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;

        try (Connection conn = SQLDatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios WHERE id_usuario = ?")) {

            if (conn == null) return null;

            ps.setInt(1, idUsuario);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    usuario = new Usuario();
                    usuario.setId(resultSet.getInt("id_usuario"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setApellidos(resultSet.getString("apellidos"));
                    usuario.setUsername(resultSet.getString("username"));
                    usuario.setPassword(resultSet.getString("password"));
                    usuario.setAvatar(resultSet.getInt("avatar"));
                    usuario.setPuntuacion(resultSet.getInt("puntuacion"));
                    usuario.setVidas(resultSet.getInt("vidas")); // Recuperar las vidas
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public Usuario obtenerUsuario(String username) {
        Usuario usuario = null;

        try (Connection conn = SQLDatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM usuarios WHERE username = ?")) {

            if (conn == null) return null;

            ps.setString(1, username);

            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    usuario = new Usuario();
                    usuario.setId(resultSet.getInt("id_usuario"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setApellidos(resultSet.getString("apellidos"));
                    usuario.setUsername(resultSet.getString("username"));
                    usuario.setPassword(resultSet.getString("password"));
                    usuario.setAvatar(resultSet.getInt("avatar"));
                    usuario.setPuntuacion(resultSet.getInt("puntuacion"));
                    usuario.setVidas(resultSet.getInt("vidas"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public boolean registrarUsuario(String username, String password, String name, String lastname, int avatar) {
        boolean okCreate = false;

        try (Connection conn = SQLDatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO usuarios (nombre, apellidos, username, password, avatar) VALUES (?, ?, ?, ?, ?)")) {

            if (conn == null) return false;

            // Cifrar la contraseña antes de guardarla
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            ps.setString(1, name);
            ps.setString(2, lastname);
            ps.setString(3, username);
            ps.setString(4, hashedPassword);
            ps.setInt(5, avatar);

            int rowsAffected = ps.executeUpdate();
            okCreate = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return okCreate;
    }



    public boolean actualizarPuntuacion(int idUsuario, int puntuacion) {
        try (Connection conn = SQLDatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE usuarios SET puntuacion = ? WHERE id_usuario = ?")) {

            if (conn == null) return false;

            ps.setInt(1, puntuacion);
            ps.setInt(2, idUsuario);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarVidas(int idUsuario, int vidas) {
        try (Connection conn = SQLDatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE usuarios SET vidas = ? WHERE id_usuario = ?")) {

            if (conn == null) return false;

            ps.setInt(1, vidas);
            ps.setInt(2, idUsuario);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarUsuario(Usuario usuario) {
        try (Connection conn = SQLDatabaseManager.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE usuarios SET nombre = ?, apellidos = ?, username = ?, password = ?, avatar = ?, puntuacion = ?, vidas = ? WHERE id_usuario = ?")) {

            if (conn == null) return false;

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getUsername());
            ps.setString(4, usuario.getPassword());
            ps.setInt(5, usuario.getAvatar());
            ps.setInt(6, usuario.getPuntuacion());
            ps.setInt(7, usuario.getVidas());
            ps.setInt(8, usuario.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}



