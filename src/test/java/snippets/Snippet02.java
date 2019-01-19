package snippets;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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



}
