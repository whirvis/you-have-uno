package csci4490.uno.commons;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

class InetSocketAddressSerializer
        implements JsonSerializer<InetSocketAddress>,
        JsonDeserializer<InetSocketAddress> {

    @Override
    public @NotNull InetSocketAddress
            deserialize(@NotNull JsonElement json, @NotNull Type typeOfT,
                        @NotNull JsonDeserializationContext context)
                        throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String host = obj.get("host").getAsString();
        int port = obj.get("port").getAsInt();
        return new InetSocketAddress(host, port);
    }

    @Override
    public @NotNull JsonElement
            serialize(@NotNull InetSocketAddress src, @NotNull Type typeOfSrc,
                      @NotNull JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("host", src.getHostString());
        obj.addProperty("port", src.getPort());
        return obj;
    }

}
