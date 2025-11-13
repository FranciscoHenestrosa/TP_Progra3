package com.example.demo.repository;

import com.example.demo.domain.ConnectionProjection; 
import com.example.demo.domain.PersonEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface PersonRepository extends ReactiveNeo4jRepository<PersonEntity, String> {

    Mono<PersonEntity> findByName(String name);

    // Consulta carga las relaciones
    @Query("""
        MATCH (p:Person)
        OPTIONAL MATCH (p)-[:ACTED_IN]->(m:Movie)
        OPTIONAL MATCH (p)-[:CO_ACTED_WITH]->(p2:Person)
        RETURN p, collect(distinct m) AS movies, collect(distinct p2) AS coActors
    """)
    Flux<PersonEntity> findAllPeople();

    @Query("""
        MATCH (p1:Person)-[r:CO_ACTED_WITH]->(p2:Person)
        RETURN p1.name AS from, p2.name AS to, r.weight AS weight
        """)
    Flux<ConnectionProjection> findAllConnections(); 
}



























