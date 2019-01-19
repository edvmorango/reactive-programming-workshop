package snippets;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

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




}
