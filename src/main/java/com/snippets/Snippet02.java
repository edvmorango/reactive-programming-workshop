package com.snippets;

import reactor.core.publisher.Mono;

import java.util.Optional;


// Empty Mono
public class Snippet02 {

    public static void main(String[] args) {

        var optionalValue = Optional.of(10);

        Mono<Optional<Integer>> redundantMono = Mono.just(optionalValue);

        System.out.println("redundantMono String -> " + redundantMono);
        System.out.println("redundantMono Value -> " + redundantMono.block());


        Mono<Integer> singleOptionalElement = Mono.justOrEmpty(optionalValue);

        System.out.println("singleOptionalElement String -> " + singleOptionalElement);
        System.out.println("singleOptionalElement Value -> " + singleOptionalElement.block());


        Mono<Integer> singleOptionalElementP10 = singleOptionalElement.map(e -> e * 10);

        System.out.println("singleOptionalElementP10 String -> " + singleOptionalElement);
        System.out.println("singleOptionalElementP10 Value -> " + singleOptionalElement.block());




    }

}
