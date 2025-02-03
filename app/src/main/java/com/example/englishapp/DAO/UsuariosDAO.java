package com.example.englishapp.DAO;

import com.example.englishapp.ConexionMethods;
import com.example.englishapp.SQLDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsuariosDAO extends ConexionMethods {

    private Statement sentencia = null;
    private Connection connection;
    private String query;


    public void createUsuariosTable() {
        query = "CREATE TABLE IF NOT EXISTS public.usuarios ("
                + "id_usuario SERIAL PRIMARY KEY,"
                + "nombre VARCHAR(255) NOT NULL,"
                + "apellidos VARCHAR(255) NOT NULL,"
                + "username VARCHAR(255) NOT NULL UNIQUE,"
                + "password VARCHAR(255) NOT NULL,"
                + "avatar INTEGER"
                + ");";

        try {
            if (initDBConnection()) {
                connection = this.connection;
                sentencia = connection.createStatement();
                sentencia.executeUpdate(query);
                System.out.println("Tabla 'usuarios' creada correctamente.");
            } else {
                System.out.println("Error: No se pudo establecer la conexión a la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sentencia != null) {
                    sentencia.close();
                }
                closeDBConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public boolean registrarUsuario(String username, String password, String name, String lastname) {
        boolean okCreate = false;

        try (Connection conn = SQLDatabaseManager.connect(); // Try-with-resources
             PreparedStatement ps = conn.prepareStatement("INSERT INTO usuarios (nombre, apellidos, username, password) VALUES (?, ?, ?, ?)")) {

            if (conn == null) return false; // Check for null connection

            ps.setString(1, name);
            ps.setString(2, lastname);
            ps.setString(3, username);
            ps.setString(4, password);
            int rowsAffected = ps.executeUpdate();
            okCreate = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return okCreate;
    }
}