package com.example.Examen1Back2.modelos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Curso {

    @Id  // Corregido: anotación completa para la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Corregido: anotación completa para autogenerar ID
    private Integer id;

    @Column(nullable = false)  // Opcional pero recomendable para campos que no pueden ser nulos
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "fk_docente", referencedColumnName = "id")
    @JsonBackReference(value = "docente-curso")
    private Docente docente;

    public Curso() {
    }

    public Curso(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y setters recomendados para los atributos

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}

