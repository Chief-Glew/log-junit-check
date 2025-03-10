package org.glew.logging.check.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

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
