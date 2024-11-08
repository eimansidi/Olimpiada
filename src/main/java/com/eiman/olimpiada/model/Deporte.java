package com.eiman.olimpiada.model;

/**
 * Clase modelo que representa la entidad Deporte en la base de datos.
 */
public class Deporte {
    private int id;
    private String nombre;

    /**
     * Obtiene el ID del deporte.
     *
     * @return El ID del deporte.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del deporte.
     *
     * @param id El ID del deporte.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del deporte.
     *
     * @return El nombre del deporte.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del deporte.
     *
     * @param nombre El nombre del deporte.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
