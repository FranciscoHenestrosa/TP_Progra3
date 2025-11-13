package com.example.demo.controller;

import com.example.demo.domain.MovieEntity;
import com.example.demo.repository.MovieRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Endpoint para CREAR o ACTUALIZAR una película
    @PutMapping
    public Mono<MovieEntity> createOrUpdateMovie(@RequestBody MovieEntity newMovie) {
        return movieRepository.save(newMovie);
    }

    // Endpoint para OBTENER TODAS las películas
    @GetMapping(value = { "/" }, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieEntity> getMovies() {
        return movieRepository.findAll();
    }
}
