package exercises;

import com.snippets.DividedByZeroException;
import com.snippets.Exercise01;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    @DisplayName("Filter")
    void f3() {

        List<Integer> opResult = ex.filtering(Flux.range(1, 10)).collectList().block();

        var result = List.of(2, 4, 6, 8, 10);

        assertEquals(opResult, result);

    }






    @Test
    @DisplayName("Filter mapping")
    void f10() {

        List<Integer> opResult = ex.filteringByMap(Flux.range(1, 10)).collectList().block();

        var result = List.of(2, 4, 6, 8, 10);

        assertEquals(opResult, result);

    }


}
