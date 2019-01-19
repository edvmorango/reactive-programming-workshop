package snippets;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@DisplayName("Mono")
public class Snippet01 {


    @Nested
    @DisplayName("Mono")
    class JustMono{


             @Test
        @DisplayName("Simple Mono")
        void f() {

            Mono<Integer> singleElement = Mono.just(10);

            System.out.println("singleElement String -> " + singleElement);

            System.out.println("singleElement Value -> " + singleElement.block());



        }


        @Test
        @DisplayName("Mono simple transformation")
        void f1() {

            Mono<Integer> singleElement = Mono.just(10);

            Mono<Integer> singleElementP10 =  singleElement.map(e -> e * 10);

            System.out.println("singleElement String -> " + singleElement);
            System.out.println("singleElementP10 String -> " + singleElementP10);
            System.out.println("singleElementP10 Value -> " + singleElementP10.block());



        }

        @Test
        @DisplayName("Simple simple transformations")
        void f2() {

            Mono<Integer> singleElement = Mono.just(10);

            Mono<Integer> singleElementP100 = singleElement
                    .map(e -> e * 10)
                    .map(e -> e * 10);

            System.out.println("singleElementP10 Hashcode -> " + singleElementP100);
            System.out.println("singleElementP10 Value -> " + singleElementP100.block());


        }



    }




}
