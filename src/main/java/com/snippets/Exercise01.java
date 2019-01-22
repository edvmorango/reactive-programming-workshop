package com.snippets;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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


    public Flux<Integer> filteringByMap(Flux<Integer> flux){

        return Flux.empty();

    }




}
