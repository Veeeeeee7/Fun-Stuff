import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Tester {
    private String test1Answer = "answer1";
    private String test2Answer = "answer2";
    private String test3Answer = "answer3";
    private String answer = "ANSWER";

    @Test
    void test1() {
        ExampleClass e = new ExampleClass();

        assertEquals(answer, ExampleClass.returnMessage());
    }

    @Test
    void test2() {
        ExampleClass e = new ExampleClass();

        assertEquals(answer, ExampleClass.returnMessage());
    }

    @Test
    void test3() {
        ExampleClass e = new ExampleClass();

        assertEquals(answer, ExampleClass.returnMessage());
    }
    // public static int add(int a, int b) {
    // int c = a + b;
    // return c;
    // }
}
