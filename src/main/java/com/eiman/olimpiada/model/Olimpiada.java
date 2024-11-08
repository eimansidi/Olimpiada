package com.eiman.olimpiada.model;

/**
 * Clase modelo que representa la entidad Olimpiada en la base de datos.
 */
public class Olimpiada {
    private int id;
    private String nombre;
    private int anio;
    private String temporada;
    private String ciudad;

    /**
     * Obtiene el ID de la olimpiada.
     *
     * @return El ID de la olimpiada.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID de la olimpiada.
     *
     * @param id El ID de la olimpiada.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la olimpiada.
     *
     * @return El nombre de la olimpiada.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la olimpiada.
     *
     * @param nombre El nombre de la olimpiada.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el año de la olimpiada.
     *
     * @return El año de la olimpiada.
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Establece el año de la olimpiada.
     *
     * @param anio El año de la olimpiada.
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Obtiene la temporada de la olimpiada (verano o invierno).
     *
     * @return La temporada de la olimpiada.
     */
    public String getTemporada() {
        return temporada;
    }

    /**
     * Establece la temporada de la olimpiada (verano o invierno).
     *
     * @param temporada La temporada de la olimpiada.
     */
    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    /**
     * Obtiene la ciudad donde se celebró la olimpiada.
     *
     * @return La ciudad de la olimpiada.
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde se celebró la olimpiada.
     *
     * @param ciudad La ciudad de la olimpiada.
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
