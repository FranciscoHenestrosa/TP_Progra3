package com.example.demo.algorithm;

import com.example.demo.graph.Graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BacktrackingDFS {

    /**
     * 
     *
     * @param graph grafo a recorrer.
     * @param start nodo inicial (actor).
     * @param end nodo final (actor).
     * @return lista de TODOS los caminos encontrados. Cada camino es una lista de nombres.
     */
    public static List<List<String>> findAllPaths(Graph graph, String start, String end) {
        List<List<String>> allPaths = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<String> currentPath = new ArrayList<>();

        // Inicia proceso recursivo de backtracking
        dfs(graph, start, end, visited, currentPath, allPaths);
        
        return allPaths;
    }

    
    private static void dfs(
            Graph graph,
            String current,
            String end,
            Set<String> visited,
            List<String> currentPath,
            List<List<String>> allPaths
    ) {
        // 1. MARCAR la visita (elegir)
        visited.add(current);
        currentPath.add(current);

        // 2. CASO BASE (Solución encontrada)
        if (current.equals(end)) {
            // Encontramos un camino. Añadimos una COPIA del camino actual a los resultados.
            allPaths.add(new ArrayList<>(currentPath));
        } 
        // 3. PASO RECURSIVO (Explorar)
        else {
            for (String neighbor : graph.getNeighbors(current).keySet()) {
                // Solo explorar si NO hemos visitado a este vecino EN ESTE CAMINO
                if (!visited.contains(neighbor)) {
                    dfs(graph, neighbor, end, visited, currentPath, allPaths);
                }
            }
        }

        // 4. BACKTRACK (Deshacer la elección)
        currentPath.remove(currentPath.size() - 1);
        visited.remove(current);
    }
}