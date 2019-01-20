package snippets;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@DisplayName("Misc")
public class Snippet04 {


    @Test
    @DisplayName("Cancelling")
    void f() throws InterruptedException {

        Flux<Integer> integerFlux = Flux.range(1, 10).delayElements(Duration.ofMillis(250));

        Disposable disposable = integerFlux
                .doOnCancel(() -> System.out.println("Cancelled"))
                .subscribe(e -> System.out.println("Element: "+ e));

        Thread.sleep(1000);

        disposable.dispose();

        Thread.sleep(1000);

    }


    @Test
    @DisplayName("Strict evaluation")
    void f2() {

        Mono.just(1/0)
                .doOnSubscribe(e -> System.out.println("Subscribed"))
                .doOnError(e -> System.out.println("Exception: "+e.getMessage()))
                .block();

    }

    @Test
    @DisplayName("Lazy evaluation")
    void f3() {

        Mono.fromCallable(() -> 1/0 )
                .doOnSubscribe(e -> System.out.println("Subscribed"))
                .doOnError(e -> System.out.println("Exception: "+e.getMessage()))
                .block();

    }






}
