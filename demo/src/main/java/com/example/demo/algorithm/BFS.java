package com.example.demo.algorithm;

import com.example.demo.graph.Graph;
import java.util.*;

public class BFS {

    /**
     * Encuentra todos los nodos (personas) alcanzables desde un nodo de inicio
     * dentro de un número específico de "grados" o "saltos".
     */
    public static Set<String> findNeighborsWithinDegrees(Graph graph, String startNode, int degrees) {
        Set<String> neighborsFound = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        if (!graph.getNodes().contains(startNode)) {
            return neighborsFound; // Nodo inicial no existe en el grafo
        }

        queue.add(startNode);
        distance.put(startNode, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDistance = distance.get(current);

            if (currentDistance >= degrees) {
                continue; // No explorar más allá de los grados pedidos
            }

            for (String neighbor : graph.getNeighbors(current).keySet()) {
                if (!distance.containsKey(neighbor)) { // Si no lo he visitado
                    distance.put(neighbor, currentDistance + 1);
                    queue.add(neighbor);
                    neighborsFound.add(neighbor); // Añadir a los resultados
                }
            }
        }

        return neighborsFound;
    }
}