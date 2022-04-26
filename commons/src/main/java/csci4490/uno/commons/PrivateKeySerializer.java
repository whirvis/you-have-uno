package csci4490.uno.commons;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

class PrivateKeySerializer
        implements JsonSerializer<PrivateKey>,
        JsonDeserializer<PrivateKey> {

    private static PrivateKey getPrivateKey(byte[] key) {
        try {
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(key));
        } catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull PrivateKey
            deserialize(@NotNull JsonElement json, @NotNull Type typeOfT,
                        @NotNull JsonDeserializationContext context)
                        throws JsonParseException {
        String str = json.getAsString();
        byte[] keyData = Base64.getDecoder().decode(str);
        return getPrivateKey(keyData);
    }

    @Override
    public @NotNull JsonElement
            serialize(@NotNull PrivateKey src, @NotNull Type typeOfSrc,
                      @NotNull JsonSerializationContext context) {
        byte[] keyData = src.getEncoded();
        String keyStr = Base64.getEncoder().encodeToString(keyData);
        return new JsonPrimitive(keyStr);
    }

}
