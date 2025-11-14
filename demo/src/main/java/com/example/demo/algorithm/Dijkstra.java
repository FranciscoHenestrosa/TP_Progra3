package com.example.demo.algorithm;

import com.example.demo.graph.Graph;

import java.util.*;

public class Dijkstra {

    public static Map<String, Object> findShortestPath(Graph graph, String start, String end) {
        Map<String, Double> distances = new HashMap<>();    //almacena distancia minima
        Map<String, String> previous = new HashMap<>();     //migas de pan para reconstruir el camino
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));  //Para decidir la prioridad de un actor, busca su distancia actual en el mapa distances

        for (String node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
        }

        //si el actor de inicio no existe en el grafo, retornamos resultado con distancia infinita y camino vacío
        if (graph.getNodes().contains(start)) {
            distances.put(start, 0.0);
            queue.add(start);
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("distance", Double.POSITIVE_INFINITY);
            result.put("path", Collections.emptyList());
            return result;
        }

        // Algoritmo de Dijkstra
        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(end)) break;

            if (distances.get(current) == Double.POSITIVE_INFINITY) { //Comprueba si la distancia del nodo que sacamos sigue siendo "Infinita"
                break;
            }

            // bucle que recorre cada co-actor conectado al actor
            for (Map.Entry<String, Double> neighbor : graph.getNeighbors(current).entrySet()) {
                double alt = distances.get(current) + neighbor.getValue();
                
                if (alt < distances.get(neighbor.getKey())) {   //Compara el costo del nuevo camino con el mejor costo conocido para llegar a ese neighbor
                    distances.put(neighbor.getKey(), alt);
                    previous.put(neighbor.getKey(), current);
                    queue.add(neighbor.getKey()); 
                }
            }
        }

        LinkedList<String> path = new LinkedList<>(); // guarda el camino  
        String at = end;
        while (at != null) {
            path.addFirst(at);
            at = previous.get(at);
        }
        
        Map<String, Object> result = new HashMap<>();   //Crea el Map que se convertirá en el JSON de respuesta
        

        //verificación de seguridad para el caso en que el nodo final no sea alcanzable desde el nodo inicial.
        if (path.isEmpty() || !path.getFirst().equals(start)) {
             result.put("distance", Double.POSITIVE_INFINITY);
             result.put("path", Collections.emptyList());
        } else {    //se obtiene el costo final
             result.put("distance", distances.get(end));
             result.put("path", path);
        }
       
        return result;
    }
}



