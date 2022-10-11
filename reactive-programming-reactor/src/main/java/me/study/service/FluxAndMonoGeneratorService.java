package me.study.service;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Mono<String> namesMono() {
        return Mono.just("chloe")
                .log(); // 실습이므로 디비나 데이터소스와 연동한 실제 작업 대신 로그를 찍어본다.
    }

    public Flux<String> namesFlux() {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .log();
    }

    public Mono<List<String>> namesMonoFlatMap(int filterLength) {
        // 퍼블리셔, 데이터소스
        return Mono.just("alex")
                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > filterLength)
                .flatMap(this::splitStringMono)
                .log();// 파이프라이닝, 함수형 프로그래밍
    }

    public Flux<String> namesMonoFlatMapMany(int filterLength) {
        // 퍼블리셔, 데이터소스
        return Mono.just("alex")
                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > filterLength)
                .flatMapMany(this::splitString) // Mono -> Flux
                .log();// 파이프라이닝, 함수형 프로그래밍
    }

    private Mono<List<String>> splitStringMono(String s) {
        var charArray = s.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesFluxMap() {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                //.map(String::toUpperCase)
                .map(s -> s.toUpperCase()) // 1 to 1 mapping
                .log();
    }

    public Flux<String> namesFluxImmutability() {
        Flux<String> namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        namesFlux.map(s -> s.toUpperCase());
        return namesFlux;
    }

    public Flux<String> namesFluxFilter(int filterLength) {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > filterLength)
                .map(s -> s.length() + "-" + s) // 파이프라이닝, 함수형 프로그래밍
                .log();
    }

    public Flux<String> namesFluxFlatMap(int filterLength) {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > filterLength)
                // 1 to N 매핑, 플럭스 혹은 모노를 구독하여, 리턴된 결과들을 펼처서 하나로 이어준다.
                // 비동기적으로 동작
                .flatMap(s -> splitString(s))
                .log();
    }

    public Flux<String> namesFluxFlatMapAsync(int filterLength) {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > filterLength)
                // 랜덤하게 들어오는 순서대로 처리된다.
                .flatMap(s -> splitStringWithDelay(s))
                .log();
    }

    // 유사한 기능을 제공하고 더 성능면에서 개선된 flatMap Sequential 이 존재한다고 한다.
    public Flux<String> namesFluxConcatMapAsync(int filterLength) {
        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(s -> s.toUpperCase())
                .filter(s -> s.length() > filterLength)
                // ""순서""를 기다리면서 처리한다.
                // flatMap에 비해 처리에 걸리는 시간이 길어진다. trade off
                .concatMap(s -> splitStringWithDelay(s))
                .log();
    }

    public Flux<String> namesFluxTransform(int filterLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > filterLength);

        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                // Function 객체를 파라미터로 전달받아 수행
                .transform(filterMap)
                .flatMap(s -> splitString(s))
                .defaultIfEmpty("default") // 반환값이 없는 경우 디폴트 값 설정
                .log();
    }
    public Flux<String> namesFluxTransform_switchIfEmpty(int filterLength) {

        Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > filterLength)
                .flatMap(s -> splitString(s));

        var defaultFlux = Flux.just("default")
                .transform(filterMap);

        // 퍼블리셔, 데이터소스
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                // Function 객체를 파라미터로 전달받아 수행
                .transform(filterMap)
                .switchIfEmpty(defaultFlux) // 반환값이 없는 경우 디폴트 퍼블리셔 설정
                .log();
    }

    public Flux<String> exploreConcat() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux) // 순차적으로 진행됨
                .log();
    }

    public Flux<String> exploreConcatWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");

        return abcFlux.concatWith(defFlux) // 순차적으로 진행됨
                .log();
    }

    public Flux<String> exploreMonoConcatWith() {
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.concatWith(bMono)
                .log();
    }

    public Flux<String> exploreMerge() {
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.merge(abcFlux, defFlux) // 순차적으로 진행됨
                .log();
    }

    public Flux<String> exploreMergeWith() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return abcFlux.mergeWith(defFlux) // 순차적으로 진행됨
                .log();
    }

    public Flux<String> exploreMonoMergeWith() {

        var aMono = Mono.just("A");
        var bMono = Mono.just("B");

        return aMono.mergeWith(bMono)
                .log();
    }

    public Flux<String> exploreMergeSequential() {

        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));

        return Flux.mergeSequential(abcFlux, defFlux) // 순차적으로 진행됨
                .log();
    }

    public Flux<String> splitString(String name) {
        var charArray = name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String> splitStringWithDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);
        //var delay = 1000; 소요시간 테스트해볼 때
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay)); // 랜덤하게 emit 될 예정
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService= new FluxAndMonoGeneratorService();

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> System.out.println("Name is : " + name)); // 구독하여 컨슘(소비)

        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name -> System.out.println("Mono Name is : " + name));
    }
}
