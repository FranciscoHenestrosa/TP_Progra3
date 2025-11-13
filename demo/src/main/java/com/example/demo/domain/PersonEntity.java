package com.example.demo.domain;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("Person")
public class PersonEntity {

    @Id
    private final String name;

    private final Integer born;

    @Relationship(type = "ACTED_IN")
    private Set<MovieEntity> movies = new HashSet<>();

    @Relationship(type = "CO_ACTED_WITH")
    private Set<PersonEntity> coActors = new HashSet<>();

    public PersonEntity(String name, Integer born) {
        this.name = name;
        this.born = born;
    }

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
}








