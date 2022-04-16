package csci4490.uno.dealer.endpoint;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Objects;

public final class Endpoints {

    private Endpoints() {
        /* static class */
    }

    /**
     * Registers all methods of an object annotated with {@link Endpoint}
     * as handlers to a Javalin webserver. If the specified object is a
     * {@link Class} (e.g., {@code AccountManager.class}), then its static
     * methods are registered. Otherwise, instance methods are registered.
     *
     * @param javalin the webserver to add handlers to.
     * @param obj     the object whose methods to register.
     * @throws NullPointerException     if {@code javalin} or {@code obj}
     *                                  are {@code null}.
     * @throws IllegalArgumentException if {@code obj} had no {@link Endpoint}
     *                                  annotated methods to register.
     * @throws EndpointException        if an {@link Endpoint} annotated method
     *                                  is {@code public}, does not return type
     *                                  {@code void}, does not take only one
     *                                  parameter, or the type of the first
     *                                  parameter is not {@link Context}.
     */
    public static void register(@NotNull Javalin javalin,
                                @NotNull Object obj) {
        Objects.requireNonNull(javalin, "javalin cannot be null");
        Objects.requireNonNull(obj, "obj cannot be null");

        boolean doStaticMethods = obj instanceof Class;

        int endpointsRegistered = 0;
        for (Method method : obj.getClass().getDeclaredMethods()) {
            Endpoint annotation = method.getAnnotation(Endpoint.class);
            if (annotation == null) {
                return; /* annotation not present */
            }

            int modifiers = method.getModifiers();

            /*
             * For this endpoint method to be invokable, it must be public.
             * There are ways to see if protected methods and whatnot are
             * executable by this class, but that's not worth the effort
             * here. Making sure it's public is the easiest way to ensure
             * no hiccups will occur when invoking the method.
             */
            if (!Modifier.isPublic(modifiers)) {
                String msg = "@" + Endpoint.class.getSimpleName();
                msg += " annotated methods must be public";
                throw new EndpointException(msg);
            }

            if (doStaticMethods && !Modifier.isStatic(modifiers)) {
                continue; /* non-static method for class */
            } else if (!doStaticMethods && Modifier.isStatic(modifiers)) {
                continue; /* static method for object instance */
            }

            /*
             * It makes no sense for this method to return anything. As such,
             * assume this was a mistake by the user and throw an exception.
             */
            if (method.getReturnType() != void.class) {
                String msg = "@" + Endpoint.class.getSimpleName();
                msg += " annotated methods must";
                msg += " return type " + void.class.getName();
                throw new EndpointException(msg);
            }

            Parameter[] params = method.getParameters();

            /*
             * For this method to be invokable, it must take in exactly one
             * parameter. Furthermore, that parameter must be a Context as
             * provided by the Javalin library.
             */
            if (params.length != 1) {
                String msg = "@" + Endpoint.class.getSimpleName();
                msg += " annotated methods must";
                msg += " have exactly one parameter";
                throw new EndpointException(msg);
            } else if (params[0].getType() != Context.class) {
                String msg = "@" + Endpoint.class.getSimpleName();
                msg += " annotated methods must";
                msg += " take in a " + Context.class.getName();
                msg += " as their first and only parameter";
                throw new EndpointException(msg);
            }

            HandlerType type = annotation.type();
            String path = annotation.path();
            javalin.addHandler(type, path, (ctx) -> method.invoke(obj, ctx));
            endpointsRegistered++;
        }

        /*
         * If no endpoints were registered as a result of calling this
         * method, this was like a mistake by the user. As such, throw
         * an exception to notify them something has gone wrong.
         */
        if (endpointsRegistered <= 0) {
            String msg = "obj contained no valid";
            msg += " @" + Endpoint.class.getSimpleName();
            msg += " annotated methods to register";
            throw new IllegalArgumentException(msg);
        }
    }

}
