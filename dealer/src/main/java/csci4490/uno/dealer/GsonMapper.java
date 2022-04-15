package csci4490.uno.dealer;

import com.google.gson.Gson;
import io.javalin.plugin.json.JsonMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * A {@link JsonMapper} for Javalin which maps JSON using {@link Gson}.
 */
public class GsonMapper implements JsonMapper {

    private final Gson gson;

    /**
     * @param gson the GSON instance to use.
     * @throws NullPointerException if {@code gson} is {@code null}.
     */
    public GsonMapper(@NotNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson cannot be null");
    }

    @Override
    public @NotNull String toJsonString(@NotNull Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public @NotNull <T> T fromJsonString(@NotNull String json,
                                         @NotNull Class<T> targetClass) {
        return gson.fromJson(json, targetClass);
    }

    @Override
    public @NotNull <T> T fromJsonStream(@NotNull InputStream json,
                                         @NotNull Class<T> targetClass) {
        InputStreamReader jsonReader = new InputStreamReader(json);
        try {
            T obj = gson.fromJson(jsonReader, targetClass);
            jsonReader.close();
            return obj;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
