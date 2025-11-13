package com.example.demo.domain;

/**
 * Un DTO (Data Transfer Object) para mapear los resultados de la consulta de conexiones.
 * Los nombres de los campos (from, to, weight) deben coincidir EXACTAMENTE
 * con los alias (AS from, AS to, AS weight) de la consulta Cypher.
 */
public record ConnectionProjection(String from, String to, Double weight) {
}