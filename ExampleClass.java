import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.agent.builder.*;

public class ExampleClass {
    public static void main(String[] args) {
        inspectTestMethods(Tester.class);
    }

    public static void inspectTestMethods(Class<?> testClass) {
        Method[] methods = testClass.getDeclaredMethods();

        for (Method method : methods) {
            Test testAnnotation = method.getAnnotation(Test.class);

            if (testAnnotation != null) {
                try {
                    // Create an instance of the test class and invoke the test method
                    Object testInstance = testClass.getDeclaredConstructor().newInstance();
                    checkMethod(method);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void checkMethod(Method method) {
        System.out.println("Checking method: " + method.toGenericString());

        new ByteBuddy()
                .redefine(method.getDeclaringClass())
                .visit(Advice.to(MethodEntryExitInterceptor.class).on(ElementMatchers.named(method.getName())))
                .make()
                .load(method.getDeclaringClass().getClassLoader(), )
                .getLoaded();
    };
}

public static class MethodEntryExitInterceptor {
    @Advice.OnMethodEnter
    public static void onMethodEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
        System.out.println("Entering method: " + method.toGenericString());
        System.out.println("Arguments: " + argsToString(args));
    }

    @Advice.OnMethodExit
    public static void onMethodExit(@Advice.Origin Method method, @Advice.Return Object returnValue) {
        System.out.println("Exiting method: " + method.toGenericString());
        System.out.println("Return value: " + returnValue);
    }

    private static String argsToString(Object[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < args.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    }

    public static String returnMessage() {
        return "99";
    }

}
