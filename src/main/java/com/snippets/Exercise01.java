package com.snippets;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

public class Exercise01 {


    public Flux<String> describeNumber(Flux<Integer> integerFlux) {

        return Flux.empty();

    }

    public Mono<Integer> exceptionMono() {

        return Mono.fromCallable(() ->  1 / 0);

    }

    public Flux<Integer> filtering(Flux<Integer> flux){

        return Flux.empty();

    }


    public Mono<Integer> product(Flux<Integer> flux) {

        return Mono.empty();

    }

    public Flux<Integer> productExpanded(Flux<Integer> flux){

            return Flux.empty();

    }

    public Flux<Tuple2<Integer, String>> numberDescription(Flux<Integer> flux){

        return Flux.empty();

    }

    public Flux<Tuple2<String, Integer>> descriptionNumber(Flux<Integer> flux){

        return Flux.empty();

    }

    public Mono<Integer> greaterThan10(Integer flux){

        return Mono.empty();

    }
    public Flux<Integer> filteringByMap(Flux<Integer> flux){

        return Flux.empty();

    }




}
