package exercises;

import com.snippets.DividedByZeroException;
import com.snippets.Exercise01;
import com.snippets.GreaterThan10Exception;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

import static org.junit.Assert.*;

@DisplayName("Exercise 1")
public class Exercise01Spec {

    Exercise01 ex = new Exercise01();

    @Test
    @DisplayName("Transformation")
    void f() {

        List<String> opResult = ex.describeNumber(Flux.range(1, 10)).collectList().block();

        List<String> result = List.of("O", "E", "O", "E", "O", "E", "O", "E", "O", "E");

        assertEquals(opResult, result);

    }


    @Test
    @DisplayName("Error transformation")
    void f2() {

       Assertions.assertThrows(DividedByZeroException.class, () -> ex.exceptionMono().block());

    }


    @Test
    @DisplayName("Filter even")
    void f3() {

        List<Integer> opResult = ex.filtering(Flux.range(1, 10)).collectList().block();

        var result = List.of(2, 4, 6, 8, 10);

        assertEquals(opResult, result);

    }



    @Test
    @DisplayName("Product")
    void f5() {

        Integer opResult = ex.product(Flux.range(1, 5)).block();

        assertEquals(opResult, Integer.valueOf(120));

    }


    @Test
    @DisplayName("Expanded Product")
    void f6() {

        List<Integer> opResult = ex.productExpanded(Flux.range(1,5)).collectList().block();

        var result = List.of(1, 2, 6, 24, 120);

        assertEquals(opResult, result);

    }


    @Test
    @DisplayName("Number/Description")
    void f7() {

        List<Tuple2<Integer, String>> opResult = ex.numberDescription(Flux.range(1,5)).collectList().block();

        List<Tuple2<Integer, String>> result = List.of( Tuples.of(1,"O"), Tuples.of(2,"E"), Tuples.of(3, "O"), Tuples.of(4 , "E"), Tuples.of(5, "O"));

        assertEquals(opResult, result);

    }

    @Test
    @DisplayName("Description/Number")
    void f8() {

        List<Tuple2<String, Integer>> opResult = ex.descriptionNumber(Flux.range(1,5)).collectList().block();

        List<Tuple2<String, Integer>>result = List.of(Tuples.of("O", 1), Tuples.of("E", 2), Tuples.of( "O", 3), Tuples.of("E", 4), Tuples.of( "O", 5));

        assertEquals(opResult, result);

    }


    @Test
    @DisplayName("Greater than 10 exception")
    void f9() {

        Mono<Integer> valid = ex.greaterThan10(10);

        Mono<Integer> invalid = ex.greaterThan10(11);


        assertEquals(valid.block(), Integer.valueOf(10));
        Assertions.assertThrows(GreaterThan10Exception.class, () -> ex.exceptionMono().block());


    }

    @Test
    @DisplayName("Filtering with map")
    void f10() {

        List<Integer> opResult = ex.filteringByMap(Flux.range(1, 10)).collectList().block();

        var result = List.of(2, 4, 6, 8, 10);

        assertEquals(opResult, result);

    }


}
