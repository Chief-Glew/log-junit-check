package org.glew.logging.check;

import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.fail;

public class TestShouldFailExtension implements InvocationInterceptor {


    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        var annotation = invocationContext.getExecutable().getAnnotation(ExpectFailure.class);
        if (annotation == null){
            invocation.proceed();
            return;
        }
        try {
            invocation.proceed();
        } catch (AssertionError e){
            return;
        }
        fail("Expecting test to fail but it passed!");
    }
}
