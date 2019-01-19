package com.snippets;

import reactor.core.publisher.Mono;

public class Snippet01 {

    public static void main(String[] args){

        Mono<Integer> singleElement = Mono.just(10);

        System.out.println("singleElement String -> " + singleElement);
        System.out.println("singleElement Value -> " + singleElement.block());


        Mono<Integer> singleElementP10 =  singleElement.map(e -> e * 10);

        System.out.println("singleElementP10 String -> " + singleElementP10);
        System.out.println("singleElementP10 Value -> " + singleElementP10.block());


        var singleElementP100 =  singleElement.map(e -> e * 10).map(e -> e * 10);

        System.out.println("singleElementP10 Hashcode -> " + singleElementP100);
        System.out.println("singleElementP10 Value -> " + singleElementP100.block());

    }


}
