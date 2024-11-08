package com.eiman.olimpiada.model;

/**
 * Clase modelo que representa la entidad Deportista en la base de datos.
 */
public class Deportista {
    private Integer id;
    private String nombre;
    private Sexo sexo;
    private int edad;
    private double peso;
    private double altura;
    private byte[] foto;

    /**
     * Obtiene el ID del deportista.
     *
     * @return El ID del deportista.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del deportista.
     *
     * @param id El ID del deportista.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del deportista.
     *
     * @return El nombre del deportista.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del deportista.
     *
     * @param nombre El nombre del deportista.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public enum Sexo {
        M, F;
    }

    /**
     * Obtiene el sexo del deportista.
     *
     * @return El sexo del deportista.
     */
    public Sexo getSexo() {
        return sexo;
    }

    /**
     * Establece el sexo del deportista.
     *
     * @param sexo El sexo del deportista.
     */
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    /**
     * Obtiene la edad del deportista.
     *
     * @return La edad del deportista.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad del deportista.
     *
     * @param edad La edad del deportista.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Obtiene el peso del deportista.
     *
     * @return El peso del deportista.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Establece el peso del deportista.
     *
     * @param peso El peso del deportista.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Obtiene la altura del deportista.
     *
     * @return La altura del deportista.
     */
    public double getAltura() {
        return altura;
    }

    /**
     * Establece la altura del deportista.
     *
     * @param altura La altura del deportista.
     */
    public void setAltura(double altura) {
        this.altura = altura;
    }

    /**
     * Obtiene la foto del deportista en formato de bytes.
     *
     * @return La foto del deportista.
     */
    public byte[] getFoto() {
        return foto;
    }

    /**
     * Establece la foto del deportista en formato de bytes.
     *
     * @param foto La foto del deportista.
     */
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}
