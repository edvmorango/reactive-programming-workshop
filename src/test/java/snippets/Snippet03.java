package snippets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;


@DisplayName("Flux, ,Processors, Evaluation")
public class Snippet03 {


    @DisplayName("Flux")
    @Nested
    public class PeekObservable{

        @Test
        @DisplayName("Peek elements of Flux/Mono ignoring ")
        void f(){


            Flux<Integer> integerFlux = Flux.fromStream(List.of(1, 2, 3, 4, 5).stream());

            integerFlux.doOnNext(e -> System.out.println("Processing element: " + e))
                       .doOnComplete(() -> System.out.println("Stream completed"));

            integerFlux.blockLast(Duration.ofSeconds(5));

        }

        @Test
        @DisplayName("Peek elements of Flux/Mono successfully")
        void f2(){


            Flux<Integer> integerFlux = Flux.fromStream(List.of(1, 2, 3, 4, 5).stream());

            integerFlux.doOnNext(e -> System.out.println("Processing element: " + e))
                    .doOnComplete(() -> System.out.println("Stream completed"))
                    .blockLast(Duration.ofSeconds(5));

        }

        @Test
        @DisplayName("Peeking failures")
        void f3(){

            Flux<Integer> integerFlux = Flux
                    .fromStream(List.of(1, 2, 3 , 4, 5).stream())
                    .map(e -> e == 3 ? e/0 : e);

            integerFlux.doOnNext(e -> System.out.println("Processing element: " + e))
                       .doOnComplete(() -> System.out.println("Stream completed"))
                       .doOnError(e -> System.out.println("Catching Error: " + e.getMessage()))
                       .blockLast(Duration.ofSeconds(5));

        }

        @Test
        @DisplayName("Peeking many times and drop elements")
        void f4(){

            Flux<Integer> integerFlux = Flux.fromStream(List.of(1, 2, 3 , 4, 5).stream());


            integerFlux
                    .doOnNext(e -> System.out.println("Processing element: " + e))
                    .flatMap(e -> {
                        if(e == 3)
                            return Mono.empty();
                        else
                            return Mono.just(e * 2).delayElement(Duration.ofSeconds(e)) ;
                    })
                    .doOnNext(e -> System.out.println("Processing element after transformation: " + e))
                    .blockLast(Duration.ofSeconds(5));

        }


        @Test
        @DisplayName("Parallel Flux process")
        void f5() throws InterruptedException {

            Flux<Integer> integerFlux = Flux.fromStream(List.of(1, 2, 3 , 4, 5, 6).stream());

            var integerPipe = integerFlux
                    .doOnNext(e -> System.out.println("Processing element: " + e))
                    .flatMap(e -> {
                        if(e % 2 == 0)
                            return Mono.just(e);
                        else
                            return Mono.just(e).delayElement(Duration.ofSeconds(2));
                    })
                    .doOnNext(e -> System.out.println("Processing element after transformation at : " + e + " Thread:"+ Thread.currentThread().getName()));


            integerPipe
                    .parallel(2)
                    .runOn(Schedulers.parallel())
                    .subscribe();


            Thread.sleep(5000);


        }
        @Test
        @DisplayName("Merging Monos/Flux (unordered operation)")
        void f6() {

            Flux<Integer> flux = Flux.merge(Mono.just(1).delayElement(Duration.ofSeconds(2)), Mono.just(2));


            flux.doOnNext(e -> System.out.println("Processing element: " + e))
                .blockLast(Duration.ofSeconds(5));


        }


        @Test
        @DisplayName("Merging Monos (avoid resource leak)")
        void f7() {

            Flux<Integer> flux = Flux.merge(Mono.just(1).delayElement(Duration.ofSeconds(2)), Mono.error(RuntimeException::new));


            flux.doOnNext(e -> System.out.println("Processing element: " + e))
                    .blockLast(Duration.ofSeconds(5));


        }


        @Test
        @DisplayName("Merging empty Monos)")
        void f8() {

            Flux<Integer> flux = Flux.merge(Mono.just(1), Mono.empty());


            flux.doOnNext(e -> System.out.println("Processing element: " + e))
                    .blockLast(Duration.ofSeconds(5));


        }

        @Test
        @DisplayName("Mono generating Flux")
        void f9() {

            Mono<String> mono = Mono.just("this is a string");

            Flux<String> flux = mono.flatMapMany(e -> {
                var list = e.split(" ");
                return Flux.fromArray(list);
            });

            flux.doOnNext(e -> System.out.println("Element: "+ e)).blockLast(Duration.ofSeconds(3));

        }



    }



}