package com.example.demo.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Person")
public class PersonEntity {

    @Id
    private String name; // <-- Quitamos final

    private Integer born; // <-- Quitamos final

    @Relationship(type = "ACTED_IN")
    private Set<MovieEntity> movies = new HashSet<>();

    @Relationship(type = "CO_ACTED_WITH")
    private Set<PersonEntity> coActors = new HashSet<>();

    /**
     * Constructor vacío que Spring Data necesita para instanciar la clase
     * antes de "llenar" los campos.
     */
    public PersonEntity() {
    }

    /**
     * Dejamos el constructor que usas para crear instancias
     */
    public PersonEntity(String name, Integer born) {
        this.name = name;
        this.born = born;
    }
    
    // --- Getters ---

    public String getName() {
        return name;
    }

    public Integer getBorn() {
        return born;
    }

    public Set<MovieEntity> getMovies() {
        return movies;
    }

    public Set<PersonEntity> getCoActors() {
        return coActors;
    }

    // --- Setters (Para que Spring Data pueda "llenar" la entidad) ---

    public void setName(String name) {
        this.name = name;
    }

    public void setBorn(Integer born) {
        this.born = born;
    }

    public void setMovies(Set<MovieEntity> movies) {
        this.movies = movies;
    }

    public void setCoActors(Set<PersonEntity> coActors) {
        this.coActors = coActors;
    }
}








