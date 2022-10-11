package me.study.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

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
        // create 메소드가 subscribe() 를 내부적으로 실행하여 퍼블리셔가 데이터를 보내는 이벤트를 발생시킨다.
        StepVerifier.create(namesFlux)
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
    void namesFluxMap() {
        // given
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxMap();
        // then
        StepVerifier.create(namesFlux)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        // given
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxImmutability();
        // then
        StepVerifier.create(namesFlux)
                // .expectNext("ALEX", "BEN", "CHLOE")
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void namesFluxFilter() {
        // given
        int filterLength = 3;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFilter(filterLength);
        // then
        StepVerifier.create(namesFlux)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        // given
        int filterLength = 3;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMap(filterLength);
        // then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        // given
        int filterLength = 3;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync(filterLength);
        // then
        StepVerifier.create(namesFlux)
                //.expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                // 랜덤하게 들어오는 순서대로 처리된다.
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxConcatMapAsync() {
        // given
        int filterLength = 3;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMapAsync(filterLength);
        // then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        // given
        int filterLength = 3;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(filterLength);
        // then
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFluxTransformWithEmpty() {
        // given
        int filterLength = 6;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(filterLength);
        // then
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFluxTransform_switchIfEmpty() {
        // given
        int filterLength = 6;
        // when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform_switchIfEmpty(filterLength);
        // then
        StepVerifier.create(namesFlux)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        // given
        int filterLength = 3;
        // when
        var value = fluxAndMonoGeneratorService.namesMonoFlatMap(filterLength);
        // then
        StepVerifier.create(value)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void namesMonoFlatMapMany() {
        // given
        int filterLength = 3;
        // when
        var value = fluxAndMonoGeneratorService.namesMonoFlatMapMany(filterLength);
        // then
        StepVerifier.create(value)
                .expectNext("A", "L", "E", "X")
                .verifyComplete();
    }

    @Test
    void exploreConcat() {
        // given
        // when
        var concatFlux = fluxAndMonoGeneratorService.exploreConcat();
        // then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }
    @Test
    void exploreMerge() {
        // given
        // when
        var value = fluxAndMonoGeneratorService.exploreMerge();
        // then
        StepVerifier.create(value)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void exploreMergeSequential() {
        // given
        // when
        var value = fluxAndMonoGeneratorService.exploreMergeSequential();
        // then
        StepVerifier.create(value)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void namesMono() {
    }


    @Test
    void splitString() {
    }

    @Test
    void splitStringWithDelay() {
    }

    @Test
    void main() {
    }


}