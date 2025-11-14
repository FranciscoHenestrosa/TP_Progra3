package com.example.demo.algorithm;

import com.example.demo.graph.Graph;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BacktrackingDFS {

    @param graph //grafo a recorrer
    @param start //nodo inicial 
    @param end //nodo final
    @return //lista de TODOS los caminos encontrados. Cada camino es una lista de nombres
     
    public static List<List<String>> findAllPaths(Graph graph, String start, String end) {
        List<List<String>> allPaths = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        List<String> currentPath = new ArrayList<>();

        // Inicia backtracking
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
        
        visited.add(current);
        currentPath.add(current);

        // 2. caso base,solucion encontrada
        if (current.equals(end)) {
            // se encuentra un camino, añade una copia del camino actual a los resultados
            allPaths.add(new ArrayList<>(currentPath));
        } 
        // 3. paso recursivo, explorar
        else {
            for (String neighbor : graph.getNeighbors(current).keySet()) {
                // Solo explorar si no hemos visitado a este vecino en este camino
                if (!visited.contains(neighbor)) {
                    dfs(graph, neighbor, end, visited, currentPath, allPaths);
                }
            }
        }

        // 4. backtrack, deshace la eleccion
        currentPath.remove(currentPath.size() - 1);
        visited.remove(current);
    }
}