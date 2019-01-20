package snippets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;


@DisplayName("Flux/Processors")
public class Snippet03 {


    @Nested
    @DisplayName("Flux")
    class PeekObservable{

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


    @Nested
    @DisplayName("Processors/Subjects")
    class Processors{


        @Test
        @DisplayName("Simple Processor/Subject")
        void f() throws InterruptedException {

            UnicastProcessor<Integer> processor = UnicastProcessor.create();

            processor
                    .doOnNext(e -> System.out.println("Element: "+e))
                    .doOnComplete(() -> System.out.println("FINISHED"))
                    .subscribe();

            processor.onNext(1);

            Thread.sleep(1000);

            processor.onNext(2);

            Thread.sleep(1000);

            processor.onNext(3);

            Thread.sleep(1000);

            processor.onNext(4);

            Thread.sleep(1000);

            processor.onComplete();

            Thread.sleep(1000);

        }

        @Test
        @DisplayName("Sink (recommended for multithreading)")
        void f1() throws InterruptedException{

            var flux = Flux.just(1,2,3,4,5).delayElements(Duration.ofMillis(250));
            var flux2 = Flux.just(6,7,8,9,10);

            Flux<String> stringFlux = Flux.create(sink -> {

                var inFlux = flux
                        .delayElements(Duration.ofMillis(500))
                        .doOnNext(e -> sink.next(Thread.currentThread().getName() + " -> " + e));


                var inFlux2 = flux2.doOnNext(e -> sink.next(Thread.currentThread().getName() + " -> " + e));


                Flux.merge(inFlux, inFlux2).doOnComplete(sink::complete).subscribe();

            });

            stringFlux
                    .doOnNext(System.out::println)
                    .subscribe();

            Thread.sleep(5000);

        }



        public Runnable webServiceCall(Function<String, Boolean> callback) {

            return new Runnable() {
                @Override
                public void run() {
                    System.out.println("Calling Webservice");
                    try {
                        Thread.sleep(2000);
                        callback.apply("Request result");
                    }catch (Exception e) {}
                }};

        }



        @Test
        @DisplayName("Wrap asynchronous operation into Mono")
        void f2() throws InterruptedException {

           Mono<String> result = Mono.create(sink -> {

                webServiceCall(new Function<>() {
                    @Override
                    public Boolean apply(String s) {
                        sink.success(s);
                        return true;
                    }
                }).run();


            });

           result.doOnSuccess(r -> System.out.println("Result: " + r)).block();

        }

    }

}
