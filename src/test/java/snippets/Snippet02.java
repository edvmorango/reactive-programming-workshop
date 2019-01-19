package snippets;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DisplayName("High-order functions*")
public class Snippet02 {


    @DisplayName("Map")
    @Nested
    class MapSnippet {

        @Test
        @DisplayName("Simple map")
        void f() {

            var result = Mono.just("10")
                    .map(Long::valueOf)
                    .map(e -> e * 10);


            System.out.println("Map \"10\" to 100: " + result.block());

        }

        @Test
        @DisplayName("Map exception")
        void f1() {

            var result = Mono.just("zas")
                    .map(Long::valueOf)
                    .map(e -> e * 10);


            System.out.println("Map \"zas\" to 100: " + result.block());

        }

        @Test
        @DisplayName("Map fusion")
        void f2() {


            var result = Mono.just("10")
                    .map(this::fromStringAndMultiply)
                    .map(e -> e * 10);

            System.out.println("Map \"10\" to 100 fused: " + result.block());


        }

        Long fromStringAndMultiply(String value) {

            return Long.valueOf(value) * 10L;
        }

    }


    @DisplayName("Filter")
    @Nested
    class FilterSnippet{

        @Test
        @DisplayName("Filter simple")
        void f() {

            var result = Mono.just(100).filter(e -> e > 100);

            var result2 = Mono.just(100).filter(e -> e > 50);

            var fluxResult = Flux.just(100, 50, 200).filter(e -> e >= 100);

            System.out.println("Filter result: "+ result.block());

            System.out.println("Filter2 result: "+ result2.block());

            System.out.println("Flux result: "+ fluxResult.collectList().block());

        }

    }


    @DisplayName("Reduce")
    @Nested
    class ReduceSnippet{

        @Test
        @DisplayName("Reduce simple")
        void f() {

            Mono<Integer> reduceSum =  Flux.just(100, 50, 200).reduce((acc, e)-> acc + e);

            System.out.println("Flux reduceSum result: "+ reduceSum.block());


        }

        @Test
        @DisplayName("Reduce empty")
        void f2() {

            Flux<Integer> emptyFlux = Flux.empty();
            Mono<Integer> reduceSum = emptyFlux.reduce((acc, e) -> acc + e);

            System.out.println("Flux reduceSum result: "+ reduceSum.block());


        }

        @Test
        @DisplayName("Fold simple")
        void f3() {
            // fold
            Mono<Integer> foldSum = Flux.just(100, 50, 200).reduce(0, (acc, e) -> acc + e);

            System.out.println("Flux fold sum result: "+ foldSum.block());

        }

        @Test
        @DisplayName("Fold empty")
        void f4() {

            Flux<Integer> emptyFlux = Flux.empty();

            // fold
            Mono<Integer> foldSum = emptyFlux.reduce(0, (acc, e) -> acc + e);

            System.out.println("Flux fold sum result: "+ foldSum.block());

        }

        @Test
        @DisplayName("Map + filter fusion in terms of Folding")
        void f5() {

            Flux<String> flux = Flux.just("50", "100", "300");

            var naturalApproach = flux.map(Integer::valueOf).filter(e -> e > 100).collectList();

            System.out.println("Map + Filter natural approach: "+ naturalApproach.block());


            ArrayList<Integer> emptyList = new ArrayList<>();

            var fusionOptimization = flux.reduce(emptyList, (acc, e) -> {
                var intElement = Integer.valueOf(e);

                if( intElement > 100){

                    acc.add(acc.size(), intElement);
                    return acc;
                } else {
                    return acc;
                }

            });


            System.out.println("Map + Filter fusion optimization approach: "+ fusionOptimization.block());


        }

    }

    @DisplayName("Zip")
    @Nested
    class ZipSnippet {

        @Test
        @DisplayName("Zip simple")
        void f() {

            Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5);
            Flux<String> charFlux = Flux.just("a", "b", "c",  "d", "e");


            Flux<Tuple2<Integer, String>> zippedFlux = integerFlux.zipWith(charFlux);

            System.out.println("Zipped flux result:" + zippedFlux.collectList().block());

        }


        @Test
        @DisplayName("Zip different number of elements")
        void f2() {

            Flux<Integer> integerFlux = Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            Flux<String> charFlux = Flux.just("a", "b", "c",  "d");


            Flux<Tuple2<Integer, String>> zippedFlux = integerFlux.zipWith(charFlux);

            System.out.println("Zipped different flux result:" + zippedFlux.collectList().block());


        }


        @Test
        @DisplayName("Zip independent pipes in the same level (parallelism strategy)")
        void f3() {

            Mono<Integer> delayedElement = Mono.just("123").delayElement(Duration.ofSeconds(3)).map(Integer::valueOf);
            Mono<Integer> map = Mono.just(5);

            var merged = map.zipWith(delayedElement).map(a -> a.getT1() + a.getT2());

            System.out.println("Zipped in flux result:" + merged.block());

        }

        @Test
        @DisplayName("Zip a valid pipe with a empty pipe (avoid resource leak)")
        void f4() {

            Mono<Integer> delayedElement = Mono.empty();
            Mono<Integer> map = Mono.just(2).delay(Duration.ofSeconds(5)).map(e -> 10);

            var merged = map.zipWith(delayedElement).map(a -> a.getT1() + a.getT2());

            System.out.println("Zipped in flux result:" + merged.block());

        }

        @Test
        @DisplayName("Zip a valid action/pipe with a failed pipe (avoid resource leak)")
        void f5() {

            Mono<Integer> delayedElement = Mono.error(new RuntimeException("Exception"));
            Mono<Integer> map = Mono.just(2).delay(Duration.ofSeconds(5)).map(e -> 10);

            var merged = map.zipWith(delayedElement).map(a -> a.getT1() + a.getT2());

            System.out.println("Zipped in flux result:" + merged.block());

        }




    }


    @DisplayName("FlatMap")
    @Nested
    class FlatMapSnippet{

        class CustomException extends RuntimeException {

            CustomException() {
                super("Custom Exception");
            }

        }



        @Test
        @DisplayName("A not idiomatic way to fail")
        void f() {

            var someMono = Mono.just(10).map(a -> {
                if(a > 5)
                    throw new CustomException();
                else
                    return a;
            });

            System.out.println("Result: "+ someMono.block());

        }

        @Test
        @DisplayName("A not idiomatic way to sequence wrapped actions")
        void f2(){

            Mono<Mono<Integer>> asyncOps = Mono.just("param").map(this::asyncOp);


            System.out.println("Async operation result: "+ asyncOps.block().block());



        }

        Mono<Integer> asyncOp(String param) {
            return Mono.just(123);
        }

        @Test
        @DisplayName("A not idiomatic way to retain values")
        void f3() {

            var someMono = authorizeAsync("user", "123").map(token -> {

                Mono<Mono<Mono<String>>> resultOfSequenceOfOperations = opWhoNeedsTokenAsync(token)
                        .map(op1Result -> {
                            return opWhoNeedsTokenAndParamAsync(op1Result, token)
                                    .map(op2Result -> opWhoNeedsTokenAnd2ParamsAsync(op1Result, op2Result, token));
                        });

                return resultOfSequenceOfOperations;

            });

            Mono<Mono<Mono<Mono<String>>>> upperCaseResult = someMono.map(l1 -> l1.map(l2 -> l2.map(l3 -> l3.map(e -> e.toUpperCase()))));

            System.out.println("Result: " +upperCaseResult.block().block().block().block());
        }


        Mono<String> authorizeAsync(String login, String password) {
            return Mono.just("authtoken");
        }

        Mono<String> opWhoNeedsTokenAsync(String token) {

            return Mono.just("new param");

        }

        Mono<String> opWhoNeedsTokenAndParamAsync(String op1Result, String token) {

            return Mono.just("new param");

        }

        Mono<String> opWhoNeedsTokenAnd2ParamsAsync(String op1Result, String op2Result, String token) {

            return Mono.just("finalResult");

        }



        @Test
        @DisplayName("Failing correctly")
        void f4(){

            var someMono = Mono.just(10).flatMap(a -> {
                if(a > 5)
                    return Mono.error(new CustomException());
                else
                    return Mono.just(a);
            });

            System.out.println("Failing correclty: "+ someMono.block());

        }


        @Test
        @DisplayName("Sequencing async actions correctly")
        void f5(){


            Mono<Integer> asyncOps = Mono.just("param").flatMap(this::asyncOp);


            System.out.println("Async operation result correctly: "+ asyncOps.block());


        }


        @Test
        @DisplayName("Sequencing async actions correctly")
        void f6(){

            var result = authorizeAsync("user", "123")
                    .flatMap(token ->
                            opWhoNeedsTokenAsync(token)
                                 .flatMap(op1Result ->
                                         opWhoNeedsTokenAndParamAsync(op1Result, token)
                                                 .flatMap(op2Result -> opWhoNeedsTokenAnd2ParamsAsync(op1Result, op2Result, token))))
                    .map(String::toUpperCase);


            System.out.println("Result: " +result.block());
        }


        @Test
        @DisplayName("Empty circuit-breaker")
        void f7() {

            var result = Mono.just(10).flatMap($ -> Mono.empty());

            System.out.println("Result: "+ result.block());
        }



    }





}
