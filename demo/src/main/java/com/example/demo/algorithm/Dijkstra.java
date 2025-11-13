package com.example.demo.algorithm;

import com.example.demo.graph.Graph;

import java.util.*;

public class Dijkstra {

    public static Map<String, Object> findShortestPath(Graph graph, String start, String end) {
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));

        for (String node : graph.getNodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            previous.put(node, null);
        }

        if (graph.getNodes().contains(start)) {
            distances.put(start, 0.0);
            queue.add(start);
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("distance", Double.POSITIVE_INFINITY);
            result.put("path", Collections.emptyList());
            return result;
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(end)) break;

            if (distances.get(current) == Double.POSITIVE_INFINITY) {
                break;
            }

            for (Map.Entry<String, Double> neighbor : graph.getNeighbors(current).entrySet()) {
                double alt = distances.get(current) + neighbor.getValue();
                
                if (alt < distances.get(neighbor.getKey())) {
                    distances.put(neighbor.getKey(), alt);
                    previous.put(neighbor.getKey(), current);
                    queue.add(neighbor.getKey()); 
                }
            }
        }

        LinkedList<String> path = new LinkedList<>();
        String at = end;
        while (at != null) {
            path.addFirst(at);
            at = previous.get(at);
        }
        
        Map<String, Object> result = new HashMap<>();
        
        if (path.isEmpty() || !path.getFirst().equals(start)) {
             result.put("distance", Double.POSITIVE_INFINITY);
             result.put("path", Collections.emptyList());
        } else {
             result.put("distance", distances.get(end));
             result.put("path", path);
        }
       
        return result;
    }
}



