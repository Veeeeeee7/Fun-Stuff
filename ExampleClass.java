import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashSet;
import java.util.jar.JarFile;

import org.junit.jupiter.api.Test;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;

public class ExampleClass {
    public static void main(String[] args) {
        injectAgent("lib/byte-buddy-agent-1.14.12.jar");
        ByteBuddyAgent.install();

        new AgentBuilder.Default()
                .type(ElementMatchers
                        .any())
                .transform(new AgentBuilder.Transformer() {
                    @Override
                    public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,
                            ClassLoader classLoader, JavaModule module, ProtectionDomain protectionDomain) {
                        return builder.method(ElementMatchers.isAnnotatedWith(Test.class))
                                .intercept(Advice.to(ExampleClass.class));
                    }
                })
                .installOnByteBuddyAgent();

        HashSet<Method> testMethods = getTestMethods(Tester.class);

        for (Method method : testMethods) {
            // System.out.println(method.toGenericString());
            getEnteringArguments(method, Tester.class);
        }
    }

    @Advice.OnMethodEnter
    private static void enter(@Advice.AllArguments Object[] args, @Advice.Origin Method method) {
        System.out.println("Method " + method.getName() + " called with args: " + Arrays.toString(args));
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    private static void exit(@Advice.Origin Method method, @Advice.Thrown Throwable throwable) {
        if (throwable != null) {
            System.out.println("Method " + method.getName() + " threw an exception: " + throwable.getMessage());
        } else {
            System.out.println("Exiting " + method.getName());
        }
    }

    private static HashSet<Method> getTestMethods(Class<?> testClass) {
        HashSet<Method> list = new HashSet<>();
        for (Method method : testClass.getDeclaredMethods()) {
            Test testAnnotation = method.getAnnotation(Test.class);
            list.add(method);
            if (testAnnotation != null) {
                list.add(method);
            }
        }
        return list;
    }

    private static void injectAgent(String path) {
        try {
            // Check if the agent jar file exists
            File agentJar = new File(path);
            if (!agentJar.exists()) {
                throw new IllegalArgumentException("Agent jar file does not exist: " + path);
            }

            // Obtain the instrumentation instance using Byte Buddy
            Instrumentation instrumentation = ByteBuddyAgent.install();

            // Add the agent jar file to the JVM's system classloader
            instrumentation.appendToSystemClassLoaderSearch(new JarFile(agentJar));

            System.out.println("Successfully injected agent: " + path);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to inject agent: " + e.getMessage());
        }
    }

    private static void getEnteringArguments(Method method, Class<?> targetClass) {
        try {
            Object instance = targetClass.newInstance();
            method.invoke(instance);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static String returnMessage() {
        return "NOT ANSWER";
    }

}
