package com.example.demo.algorithm;

import com.example.demo.domain.PersonEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicProgramming {

 
    @param people //lista de todos los actores 
    @param maxCost //El presupuesto máximo (la "capacidad" de la mochila).
    @return //Un Map con el valor total y la lista de actores seleccionados.

    public static Map<String, Object> solveKnapsack(List<PersonEntity> people, int maxCost) {
        
        int n = people.size();
        
        // Arrays para guardar los valores y costos de cada actor
        int[] values = new int[n];
        int[] costs = new int[n];
        String[] names = new String[n];

        // Extrae los datos de las entidades
        for (int i = 0; i < n; i++) {
            PersonEntity p = people.get(i);
            names[i] = p.getName();
            
            // Valor = numero de Películas + numero de Co-Actores 
            values[i] = p.getMovies().size() + p.getCoActors().size();
            
            // Costo = año de nacimiento, por ej: 1964 -> 64
            costs[i] = (p.getBorn() != null ? p.getBorn() % 100 : 50); // Usamos 50 como default
        }

        // Inicio del Algoritmo de Programación Dinámica 

        // dp[i][c] = máximo valor que podemos obtener usando los primeros 'i' ítems
        // con un costo (presupuesto) máximo de 'c'.
        int[][] dp = new int[n + 1][maxCost + 1];

        // Llenamos la tabla DP
        for (int i = 1; i <= n; i++) {
            int currentItemIndex = i - 1;
            int value = values[currentItemIndex];
            int cost = costs[currentItemIndex];

            for (int c = 0; c <= maxCost; c++) {
                // Caso 1: NO incluimos el ítem actual
                // El valor es el mismo que teníamos sin él
                dp[i][c] = dp[i - 1][c]; 

                // Caso 2: SÍ incluimos el ítem actual (si podemos pagarlo)
                if (c >= cost) {
                    // Comparamos: ¿Qué es mejor?
                    // 1. No incluirlo (valor de arriba)
                    // 2. Incluirlo (valor del ítem + valor restante que podíamos pagar)
                    dp[i][c] = Math.max(
                        dp[i - 1][c], 
                        value + dp[i - 1][c - cost]
                    );
                }
            }
        }

        // termina el Algoritmo 

        // El valor máximo está en la última celda de la tabla
        int maxValue = dp[n][maxCost];

        // Backtracking para encontrar qué actores seleccionamos
        List<String> selectedActors = new ArrayList<>();
        int c = maxCost;
        for (int i = n; i > 0 && maxValue > 0; i--) {
            // Si el valor es diferente al de la fila anterior, significa que si incluimos este ítem.
            if (dp[i][c] != dp[i - 1][c]) {
                int currentItemIndex = i - 1;
                selectedActors.add(names[currentItemIndex]);
                
                // Restamos su costo y valor para seguir buscando
                maxValue -= values[currentItemIndex];
                c -= costs[currentItemIndex];
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("maxCost", maxCost);
        result.put("totalValue", dp[n][maxCost]); // El valor total óptimo
        result.put("selectedActors", selectedActors);
        
        return result;
    }
}