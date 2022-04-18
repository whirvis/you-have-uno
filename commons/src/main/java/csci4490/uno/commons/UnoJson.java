package csci4490.uno.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnoJson {

    /**
     * The GSON instance used across all <i>You Have Uno!</i> modules. This
     * should be used to ensure the settings are across all parts of the
     * program are consistent.
     */
    public static final @NotNull Gson GSON =
            new GsonBuilder().serializeNulls().create();

    /**
     * Converts an {@code Object} to JSON.
     *
     * @param src the object to convert.
     * @return {@code src} converted to JSON.
     */
    public static JsonElement toJson(@Nullable Object src) {
        return GSON.toJsonTree(src);
    }

}
