package com.example.englishapp;

public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
    private String username;
    private String password;
    private int avatar;
    private int puntuacion;
    private int vidas;
    private int nivelVocabulario;
    private int nivelGramatica;
    private int nivelReading;

    public Usuario() {}


    public Usuario(int id, String nombre, String apellidos, String username, String password, int avatar, int puntuacion, int vidas, int nivelVocabulario, int nivelGramatica, int nivelReading) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.puntuacion = puntuacion;
        this.vidas = vidas;
        this.nivelVocabulario = nivelVocabulario;
        this.nivelGramatica = nivelGramatica;
        this.nivelReading = nivelReading;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public int getNivelVocabulario() {
        return nivelVocabulario;
    }

    public void setNivelVocabulario(int nivelVocabulario) {
        this.nivelVocabulario = nivelVocabulario;
    }

    public int getNivelGramatica() {
        return nivelGramatica;
    }

    public void setNivelGramatica(int nivelGramatica) {
        this.nivelGramatica = nivelGramatica;
    }

    public int getNivelReading() {
        return nivelReading;
    }

    public void setNivelReading(int nivelReading) {
        this.nivelReading = nivelReading;
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
                ", nivelVocabulario=" + nivelVocabulario +
                ", nivelGramatica=" + nivelGramatica +
                ", nivelReading=" + nivelReading +
                '}';
    }
}