package com.eiman.olimpiada.model;

/**
 * Clase modelo que representa la entidad Participacion en la base de datos.
 */
public class Participacion {
    private int idDeportista;
    private int idEvento;
    private int idEquipo;
    private String medalla;

    /**
     * Obtiene el ID del deportista en la participación.
     *
     * @return El ID del deportista.
     */
    public int getIdDeportista() {
        return idDeportista;
    }

    /**
     * Establece el ID del deportista en la participación.
     *
     * @param idDeportista El ID del deportista.
     */
    public void setIdDeportista(int idDeportista) {
        this.idDeportista = idDeportista;
    }

    /**
     * Obtiene el ID del evento en la participación.
     *
     * @return El ID del evento.
     */
    public int getIdEvento() {
        return idEvento;
    }

    /**
     * Establece el ID del evento en la participación.
     *
     * @param idEvento El ID del evento.
     */
    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * Obtiene el ID del equipo al que pertenece el deportista en la participación.
     *
     * @return El ID del equipo.
     */
    public int getIdEquipo() {
        return idEquipo;
    }

    /**
     * Establece el ID del equipo al que pertenece el deportista en la participación.
     *
     * @param idEquipo El ID del equipo.
     */
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    /**
     * Obtiene la medalla obtenida en la participación (oro, plata, bronce o null).
     *
     * @return La medalla obtenida.
     */
    public String getMedalla() {
        return medalla;
    }

    /**
     * Establece la medalla obtenida en la participación (oro, plata, bronce o null).
     *
     * @param medalla La medalla obtenida.
     */
    public void setMedalla(String medalla) {
        this.medalla = medalla;
    }
}
