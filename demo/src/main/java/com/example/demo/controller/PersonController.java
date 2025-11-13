package com.example.demo.controller;

import com.example.demo.algorithm.BFS;
import com.example.demo.algorithm.Dijkstra;
import com.example.demo.algorithm.QuickSort;
import com.example.demo.domain.ConnectionProjection; 
import com.example.demo.domain.PersonEntity;
import com.example.demo.graph.Graph;
import com.example.demo.repository.PersonRepository;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    private Mono<Graph> buildGraph() {
        Mono<List<PersonEntity>> peopleMono = repository.findAllPeople().collectList();
        Mono<List<ConnectionProjection>> edgesMono = repository.findAllConnections().collectList();

        return Mono.zip(peopleMono, edgesMono)
                .map(tuple -> {
                    List<PersonEntity> people = tuple.getT1();
                    List<ConnectionProjection> edges = tuple.getT2();

                    Graph g = new Graph();
                    for (PersonEntity p : people) {
                        g.addNode(p.getName());
                    }

                    for (ConnectionProjection edge : edges) {
                        String from = edge.from(); 
                        String to = edge.to();
                        Double weight = edge.weight();
                        
                        if (weight == null) weight = 1.0;
                        g.addEdge(from, to, weight);
                    }
                    return g;
                });
    }

    @GetMapping("/sorted")
    public Mono<List<String>> getSortedPeople() {
        return repository.findAllPeople()
                .collectList()
                .flatMap(people -> Mono.fromRunnable(() -> {
                            QuickSort.sort(people);
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                        .thenReturn(people) 
                )
                .map(sortedPeople -> sortedPeople.stream()
                                        .map(PersonEntity::getName)
                                        .collect(Collectors.toList())
                );
    }

    @GetMapping("/dijkstra")
    public Mono<Map<String, Object>> getShortestPath(
            @RequestParam String name1,
            @RequestParam String name2
    ) {
        return buildGraph()
                .flatMap(graph -> Mono.fromCallable(() -> {
                            // <-- ¡AQUÍ ESTÁ LA CORRECCIÓN!
                            // El nombre del método es findShortestPath
                            return Dijkstra.findShortestPath(graph, name1, name2);
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                );
    }

    @GetMapping("/bfs-degrees")
    public Mono<Map<String, Object>> getNeighborsWithinDegrees(
            @RequestParam String name,
            @RequestParam int degrees
    ) {
        return buildGraph()
                .flatMap(graph -> Mono.fromCallable(() -> {
                            Set<String> neighbors = BFS.findNeighborsWithinDegrees(graph, name, degrees);
                            
                            Map<String, Object> result = new HashMap<>();
                            result.put("actor", name);
                            result.put("degrees", degrees);
                            result.put("count", neighbors.size());
                            result.put("neighbors", neighbors);
                            return result;
                        })
                        .subscribeOn(Schedulers.boundedElastic())
                );
    }
}



