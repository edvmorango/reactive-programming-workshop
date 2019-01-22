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




}
