package csci4490.uno.dealer.endpoint;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * When present, indicates that a method should be registered as an endpoint
 * handler for a Javalin webserver. This annotation may be used with either
 * instance or static methods. All methods with this annotation must return
 * a {@code void} type. Furthermore, they must take exactly one parameter of
 * type {@link io.javalin.http.Context}.
 *
 * @see Endpoints#register(Javalin, Object)
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {

    @NotNull HandlerType type();

    @NotNull String path();

}
