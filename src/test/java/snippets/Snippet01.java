package snippets;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

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


    @Nested
    @DisplayName("Empty Mono")
    class EmptyMono{


        @Test
        @DisplayName("Mono from optional nested")
        void f() {

            var optionalValue = Optional.of(10);

            Mono<Optional<Integer>> nestedMono = Mono.just(optionalValue);

            System.out.println("nestedMono String -> " + nestedMono);
            System.out.println("nestedMono Value -> " + nestedMono.block());



        }

        @Test
        @DisplayName("Mono from optional flatten")
        void f2() {

            var optionalValue = Optional.of(10);

            Mono<Integer> singleOptionalElement = Mono.justOrEmpty(optionalValue);

            System.out.println("singleOptionalElement String -> " + singleOptionalElement);
            System.out.println("singleOptionalElement Value -> " + singleOptionalElement.block());


        }


        @Test
        @DisplayName("Mono from optional successful transformation")
        void f3() {

            var optionalValue = Optional.of(10);

            Mono<Integer> singleOptionalElement = Mono.justOrEmpty(optionalValue);


            Mono<Integer> singleP100 = singleOptionalElement
                    .map(e -> e * 10)
                    .map(e -> e * 10);


            System.out.println("singleP100 String -> " + singleP100);
            System.out.println("singleP100 Value -> " + singleP100.block());

        }

        @Test
        @DisplayName("Mono from optional empty")
        void f4() {

            Mono<Integer> emptyMono = Mono.justOrEmpty(Optional.empty());

            System.out.println("emptyMono String -> " + emptyMono);
            System.out.println("emptyMono Value -> " + emptyMono.block());

        }

        @Test
        @DisplayName("Mono empty")
        void f5() {

            // Infers to Object
            var emptyMonoObject = Mono.empty();

            Mono<Integer> emptyMono = Mono.empty();

            System.out.println("emptyMono String -> " + emptyMono);
            System.out.println("emptyMono Value -> " + emptyMono.block());

        }


        @Test
        @DisplayName("Mono empty, transformations")
        void f6() {

            Mono<Integer> emptyMono = Mono.empty();

            Mono<Integer> singleP100 = emptyMono
                    .map(e -> e * 10)
                    .map(e -> e * 10);

            System.out.println("singleP100 String -> " + singleP100);
            System.out.println("singleP100 Value -> " + singleP100.block());

        }

    }

    @Nested
    @DisplayName("Failed Mono")
    class  FailedMono{


        @Test
        @DisplayName("Exception Mono")
        void f() {

            Mono<Integer> singleElement = Mono.error(new RuntimeException("Some exception"));

            System.out.println("singleElement String -> " + singleElement);

            System.out.println("singleElement Value -> " + singleElement.block());

        }

        @Test
        @DisplayName("Exception transformation")
        void f2() {

            Mono<Integer> singleElement = Mono.error(new RuntimeException("Some exception"));

            Mono<Integer> singleElementP100 = singleElement.map(e -> e * 100);


            System.out.println("singleElement String -> " + singleElementP100);

            System.out.println("singleElement Value -> " + singleElementP100.block());

        }


    }

    @Nested
    @DisplayName("Pipes")
    class Structure{


        @Test
        @DisplayName("Transformations as Pipe")
        void f() throws InterruptedException {

            var someMono =
                    Mono.just(10)
                        .map(a -> a * 10)
                        .map(a -> a.toString())
                        .map(a -> Long.valueOf(a))
                        .map(a -> {
                            System.out.println("Reached the last transformation");
                            return a * 10L;
                        });


            System.out.println("Some action");

            System.out.println("Another action");

            Thread.sleep(5000);

        }

        @Test
        @DisplayName("Subscribing a pipe ")
        void f2() throws InterruptedException {

            var someMono =
                    Mono.just(10)
                            .map(a -> a * 10)
                            .map(a -> a.toString())
                            .map(a -> Long.valueOf(a))
                            .map(a -> {
                                System.out.println("Reached the last transformation");
                                return a * 10L;
                            });


            someMono.subscribe();

            System.out.println("Some action");

            System.out.println("Another action");


            Thread.sleep(2000);

        }

        @Test
        @DisplayName("Subscribing a heavyweight pipe ")
        void f3() throws InterruptedException {

            var someMono =
                    Mono.just(10)
                            .map(a -> a * 10)
                            .map(a -> a.toString())
                            .map(a -> Long.valueOf(a))
                            .delayElement(Duration.ofSeconds(1))
                            .map(a -> {
                                System.out.println("Reached the last transformation");
                                return a * 10L;
                            });


            someMono.subscribe();

            System.out.println("Some action");

            System.out.println("Another action");


            Thread.sleep(2000);

        }



    }


}
