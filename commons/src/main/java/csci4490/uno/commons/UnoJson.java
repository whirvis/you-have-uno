package csci4490.uno.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.Objects;

public class UnoJson {

    /**
     * The GSON instance used across all <i>You Have Uno!</i> modules. This
     * should be used to ensure the settings are across all parts of the
     * program are consistent.
     */
    public static final @NotNull Gson GSON = new GsonBuilder()
            .setPrettyPrinting().serializeNulls()
            .registerTypeAdapter(InetSocketAddress.class,
                    new InetSocketAddressSerializer())
            .create();

    /**
     * Converts an {@code Object} to JSON.
     *
     * @param src the object to convert.
     * @return {@code src} converted to JSON.
     */
    public static JsonElement toJson(@Nullable Object src) {
        return GSON.toJsonTree(src);
    }

    /**
     * Reads a {@link JsonObject} from an {@link InputStream}.
     *
     * @param in the input stream to ready from.
     * @return the parsed JSON object.
     * @throws NullPointerException if {@code in} is {@code null}.
     */
    public static JsonObject fromJson(@NotNull InputStream in) {
        Objects.requireNonNull(in, "in cannot be null");
        InputStreamReader reader = new InputStreamReader(in);
        return GSON.fromJson(reader, JsonObject.class);
    }

    public static <T> T fromJson(@NotNull JsonElement json,
                                 @NotNull Class<T> clazz) {
        Objects.requireNonNull(json, "json cannot be null");
        Objects.requireNonNull(clazz, "clazz cannot be null");
        return GSON.fromJson(json, clazz);
    }

}
