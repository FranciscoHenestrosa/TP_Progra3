package com.example.demo.algorithm;

import com.example.demo.domain.PersonEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class BranchAndBound {

    /**
     * Clase interna para representar un "Ítem" (Actor) en el problema.
     * Incluye el ratio valor/costo para ordenar.
     */
    private static class Item {
        int value;
        int cost;
        String name;
        double ratio; // value / cost

        Item(int value, int cost, String name) {
            this.value = value;
            this.cost = cost;
            this.name = name;
            this.ratio = (cost == 0) ? Double.POSITIVE_INFINITY : (double) value / cost;
        }
    }

    /**
     * Clase interna para representar un "Nodo" en el árbol de decisión.
     */
    private static class Node {
        int level; // Nivel del ítem que estamos considerando
        int currentValue; // Valor acumulado hasta ahora
        int currentCost; // Costo acumulado hasta ahora
        double upperBound; // Estimación (límite superior) del valor máximo
        List<String> selected; // Actores seleccionados en este camino

        Node(int level, int currentValue, int currentCost, List<String> selected) {
            this.level = level;
            this.currentValue = currentValue;
            this.currentCost = currentCost;
            this.selected = new ArrayList<>(selected);
        }
    }

    /**
     * Función principal que resuelve el problema de la mochila con B&B.
     */
    public static Map<String, Object> solveKnapsack(List<PersonEntity> people, int maxCost) {
        
        List<Item> items = new ArrayList<>();
        // 1. Convertir PersonEntity a Items y calcular valor/costo
        for (PersonEntity p : people) {
            int value = p.getMovies().size() + p.getCoActors().size();
            int cost = (p.getBorn() != null ? p.getBorn() % 100 : 50);
            
            // Ignorar ítems que no tienen valor o que cuestan 0
            if (value > 0 && cost > 0 && cost <= maxCost) {
                items.add(new Item(value, cost, p.getName()));
            }
        }

        // 2. Ordenar ítems por ratio (valor/costo) descendente.
        // Esto es clave para que el "upper bound" sea lo más preciso posible.
        items.sort((a, b) -> Double.compare(b.ratio, a.ratio));

        int n = items.size();
        
        // 3. Inicializar la cola de prioridad (Max-Heap)
        // Ordena los nodos a explorar por su 'upperBound' (el más prometedor primero)
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> -node.upperBound));

        // 4. Crear el nodo raíz (nivel -1, sin ítems, costo 0)
        Node root = new Node(-1, 0, 0, new ArrayList<>());
        root.upperBound = calculateUpperBound(root, items, maxCost);
        queue.add(root);

        int maxTotalValue = 0;
        List<String> bestSelection = new ArrayList<>();

        // 5. Iniciar la exploración (B&B)
        while (!queue.isEmpty()) {
            Node v = queue.poll(); // Saca el nodo más prometedor

            // Si el nodo actual (v) ya no puede mejorar la mejor solución, se poda.
            if (v.upperBound < maxTotalValue) {
                continue; // Podar esta rama
            }

            // Mover al siguiente nivel (siguiente ítem)
            int nextLevel = v.level + 1;
            if (nextLevel >= n) {
                continue; // Llegamos al final de esta rama
            }

            Item item = items.get(nextLevel);

            // --- CASO 1: "SÍ tomar el ítem" ---
            // Solo si podemos pagarlo
            if (v.currentCost + item.cost <= maxCost) {
                List<String> selectedWith = new ArrayList<>(v.selected);
                selectedWith.add(item.name);
                
                Node withItem = new Node(
                    nextLevel,
                    v.currentValue + item.value,
                    v.currentCost + item.cost,
                    selectedWith
                );

                // Si este camino es mejor que el máximo actual, actualizamos
                if (withItem.currentValue > maxTotalValue) {
                    maxTotalValue = withItem.currentValue;
                    bestSelection = withItem.selected;
                }

                withItem.upperBound = calculateUpperBound(withItem, items, maxCost);

                // Si la estimación de esta rama sigue siendo buena, la exploramos
                if (withItem.upperBound > maxTotalValue) {
                    queue.add(withItem);
                }
            }

            // --- CASO 2: "NO tomar el ítem" ---
            Node withoutItem = new Node(
                nextLevel,
                v.currentValue, // El valor no cambia
                v.currentCost,  // El costo no cambia
                v.selected      // La lista de seleccionados no cambia
            );
            
            withoutItem.upperBound = calculateUpperBound(withoutItem, items, maxCost);

            // Si la estimación de esta rama (sin el ítem) es buena, la exploramos
            if (withoutItem.upperBound > maxTotalValue) {
                queue.add(withoutItem);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("maxCost", maxCost);
        result.put("totalValue", maxTotalValue);
        result.put("selectedActors", bestSelection);
        return result;
    }

    /**
     * Calcula el "Upper Bound" (límite superior) para un nodo.
     * Esta es la estimación optimista.
     */
    private static double calculateUpperBound(Node node, List<Item> items, int maxCost) {
        int n = items.size();
        int remainingCost = maxCost - node.currentCost;
        double bound = node.currentValue;
        int level = node.level + 1;

        // Llenar la capacidad restante con los siguientes ítems
        while (level < n && items.get(level).cost <= remainingCost) {
            remainingCost -= items.get(level).cost;
            bound += items.get(level).value;
            level++;
        }

        // Si aún queda espacio, añadir una FRACCIÓN del siguiente ítem
        if (level < n && remainingCost > 0) {
            bound += (items.get(level).ratio * remainingCost);
        }

        return bound;
    }
}