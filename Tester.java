import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class Tester {
    private String test1Answer = "answer1";
    private String test2Answer = "answer2";
    private String test3Answer = "answer3";

    @Test
    void test1() {
        assertEquals(test1Answer, ExampleClass.returnMessage());
    }

    @Test
    void test2() {
        assertEquals(test2Answer, ExampleClass.returnMessage());
    }

    @Test
    void test3() {
        assertEquals(test3Answer, ExampleClass.returnMessage());
    }
    // public static int add(int a, int b) {
    // int c = a + b;
    // return c;
    // }
}
