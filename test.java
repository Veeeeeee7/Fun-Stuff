import java.lang.reflect.Method;
import java.util.HashSet;

public class test {
    public static void main(String[] args) {
        ExampleClass e = new ExampleClass();

        HashSet<Method> testMethods = ExampleClass.getTestMethods(Tester.class);

        for (Method method : testMethods) {
            // System.out.println(method.toGenericString());
            ExampleClass.getEnteringArguments(method, Tester.class);
        }
    }
}
