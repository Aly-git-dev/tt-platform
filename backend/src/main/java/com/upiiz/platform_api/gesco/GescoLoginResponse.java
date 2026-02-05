package com.upiiz.platform_api.gesco;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GescoLoginResponse {

    @JsonProperty("estatus")
    private String estatus;

    @JsonProperty("datos")
    private Datos datos;

    public boolean isEstatus() {
        return "true".equalsIgnoreCase(estatus);
    }
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Datos getDatos() { return datos; }
    public void setDatos(Datos datos) { this.datos = datos; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Datos {

        @JsonProperty("boleta")
        private String boleta;

        @JsonProperty("mail")
        private String mail;

        @JsonProperty("Nombre")
        private String nombre;

        @JsonProperty("Carrera")
        private String carrera;

        @JsonProperty("token")
        private String token;

        public String getBoleta() { return boleta; }
        public void setBoleta(String boleta) { this.boleta = boleta; }

        public String getMail() { return mail; }
        public void setMail(String mail) { this.mail = mail; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCarrera() { return carrera; }
        public void setCarrera(String carrera) { this.carrera = carrera; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
