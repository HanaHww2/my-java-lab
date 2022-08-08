package me.study.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log();
    }

    public Mono<String> namesMono() {
        return Mono.just("chloe")
                .log();
    }
    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService= new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is : " + name)); // 구독하여 컨슘(소비)

        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name -> System.out.println("Mono Name is : " + name));
    }
}
