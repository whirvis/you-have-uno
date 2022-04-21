package csci4490.uno.dealer.response;

import com.google.gson.JsonObject;
import csci4490.uno.commons.UnoJson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A wrapper for an HTTP response sent by the UNO dealer server.
 * This exists to aid the process of decoding responses.
 */
public abstract class UnoDealerResponse {

    /* @formatter:off */
    private static final @Nullable String
            MIME_TYPE_TEXT = ContentType.TEXT_PLAIN.getMimeType(),
            MIME_TYPE_JSON = ContentType.APPLICATION_JSON.getMimeType();
    /* @formatter:on */

    protected final @NotNull HttpResponse response;
    protected final @NotNull HttpEntity entity;
    protected final @NotNull StatusLine status;
    protected final @NotNull ContentType type;

    /**
     * The response text. This will be {@code null} if the type of
     * {@link #response} is not {@link ContentType#TEXT_PLAIN}.
     */
    protected final String plainText;

    /**
     * The response JSON. This will be {@code null} if the type of
     * {@link #response} is not {@link ContentType#APPLICATION_JSON}.
     */
    protected final JsonObject applicationJson;

    /**
     * This is set to {@code true} when an unknown content type is found
     * in the {@link #response}. Otherwise, this is {@code false}.
     */
    protected final boolean unknownContentType;

    /**
     * Constructs a new {@code UnoDealerResponse}. This sets a few internal
     * class fields, like {@link #entity}, {@link #status}, {@link #type},
     * etc. These are shorthands for accessing data contained within the
     * HTTP response.
     *
     * @param response the UNO dealer server's response.
     * @throws NullPointerException if {@code response} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public UnoDealerResponse(@NotNull HttpResponse response) throws IOException {
        Objects.requireNonNull(response, "response cannot be null");

        this.response = response;

        this.entity = response.getEntity();
        this.status = response.getStatusLine();
        this.type = ContentType.get(entity);

        InputStream content = entity.getContent();
        ContentType type = ContentType.get(entity);
        String mimeType = type.getMimeType();

        if (mimeType.equals(MIME_TYPE_TEXT)) {
            this.plainText = EntityUtils.toString(entity);
            this.applicationJson = null;
            this.unknownContentType = false;
        } else if (mimeType.equals(MIME_TYPE_JSON)) {
            this.plainText = null;
            this.applicationJson = UnoJson.fromJson(content);
            this.unknownContentType = false;
        } else {
            this.plainText = null;
            this.applicationJson = null;
            this.unknownContentType = true;
        }
    }

}
