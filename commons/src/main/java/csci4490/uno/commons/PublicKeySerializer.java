package csci4490.uno.commons;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

class PublicKeySerializer
        implements JsonSerializer<PublicKey>,
        JsonDeserializer<PublicKey> {

    private static PublicKey getPublicKey(byte[] key) {
        try {
            return KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(key));
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull PublicKey
            deserialize(@NotNull JsonElement json, @NotNull Type typeOfT,
                        @NotNull JsonDeserializationContext context)
                        throws JsonParseException {
        String str = json.getAsString();
        byte[] keyData = Base64.getDecoder().decode(str);
        return getPublicKey(keyData);
    }

    @Override
    public @NotNull JsonElement
            serialize(@NotNull PublicKey src, @NotNull Type typeOfSrc,
                      @NotNull JsonSerializationContext context) {
        byte[] keyData = src.getEncoded();
        String keyStr = Base64.getEncoder().encodeToString(keyData);
        return new JsonPrimitive(keyStr);
    }

}
