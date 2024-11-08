package com.eiman.olimpiada.model;

/**
 * Clase modelo que representa la entidad Evento en la base de datos.
 */
public class Evento {
    private int id;
    private String nombre;
    private int idOlimpiada;
    private int idDeporte;

    /**
     * Obtiene el ID del evento.
     *
     * @return El ID del evento.
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el ID del evento.
     *
     * @param id El ID del evento.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del evento.
     *
     * @return El nombre del evento.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del evento.
     *
     * @param nombre El nombre del evento.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID de la olimpiada asociada al evento.
     *
     * @return El ID de la olimpiada.
     */
    public int getIdOlimpiada() {
        return idOlimpiada;
    }

    /**
     * Establece el ID de la olimpiada asociada al evento.
     *
     * @param idOlimpiada El ID de la olimpiada.
     */
    public void setIdOlimpiada(int idOlimpiada) {
        this.idOlimpiada = idOlimpiada;
    }

    /**
     * Obtiene el ID del deporte asociado al evento.
     *
     * @return El ID del deporte.
     */
    public int getIdDeporte() {
        return idDeporte;
    }

    /**
     * Establece el ID del deporte asociado al evento.
     *
     * @param idDeporte El ID del deporte.
     */
    public void setIdDeporte(int idDeporte) {
        this.idDeporte = idDeporte;
    }
}
