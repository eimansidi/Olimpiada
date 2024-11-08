package com.eiman.olimpiada.model;

/**
 * Clase modelo que representa la entidad Equipo en la base de datos.
 */
public class Equipo {
    private int id;
    private String nombre;
    private String iniciales;

    /**
     * Obtiene el ID del equipo.
     *
     * @return El ID del equipo.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del equipo.
     *
     * @param id El ID del equipo.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del equipo.
     *
     * @return El nombre del equipo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del equipo.
     *
     * @param nombre El nombre del equipo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene las iniciales del equipo.
     *
     * @return Las iniciales del equipo.
     */
    public String getIniciales() {
        return iniciales;
    }

    /**
     * Establece las iniciales del equipo.
     *
     * @param iniciales Las iniciales del equipo.
     */
    public void setIniciales(String iniciales) {
        this.iniciales = iniciales;
    }
}
