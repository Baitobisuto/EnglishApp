package com.example.englishapp;

public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
    private String username;
    private String password; // Añadido el campo password
    private int avatar;
    private int puntuacion;
    private int vidas;
    private int nivelCompletado;

    // Constructor vacío (necesario para algunas librerías de serialización)
    public Usuario() {}

    // Constructor con todos los campos (opcional, pero útil)
    public Usuario(int id, String nombre, String apellidos, String username, String password, int avatar, int puntuacion, int vidas) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.puntuacion = puntuacion;
        this.vidas = vidas;
    }

    public int getNivelCompletado() {
        return nivelCompletado;
    }

    public void setNivelCompletado(int nivelCompletado) {
        this.nivelCompletado = nivelCompletado;
    }

    // Getters y setters para todos los campos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", avatar=" + avatar +
                ", puntuacion=" + puntuacion +
                ", vidas=" + vidas +
                '}';
    }
}