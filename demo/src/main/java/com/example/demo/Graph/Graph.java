package com.example.demo.graph;

import java.util.*;

public class Graph {
    private final Map<String, Map<String, Double>> adj = new HashMap<>();

    public void addNode(String node) {
        adj.computeIfAbsent(node, k -> new HashMap<>());
    }

    public void addEdge(String from, String to, double weight) {
        adj.computeIfAbsent(from, k -> new HashMap<>());
        adj.computeIfAbsent(to, k -> new HashMap<>());

        adj.get(from).put(to, weight);
        adj.get(to).put(from, weight); // grafo no dirigido
    }

    public Map<String, Double> getNeighbors(String node) {
        return adj.getOrDefault(node, Collections.emptyMap());
    }

    public Set<String> getNodes() {
        return adj.keySet();
    }
}
