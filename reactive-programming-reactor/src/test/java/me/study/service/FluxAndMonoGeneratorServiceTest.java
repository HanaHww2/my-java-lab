package me.study.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void namesFlux() {
        // given
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        // then
        StepVerifier.create(namesFlux) // create 가 subscribe() 를 내부적으로 실행하여 이벤트를 발생시킨다.
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void countNamesFlux() {
        // given
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        // then
        StepVerifier.create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void countNamesAfterOneFlux() {
        // given
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();
        // then
        StepVerifier.create(namesFlux)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void namesMono() {
    }

    @Test
    void main() {
    }

}