package csci4490.uno.commons.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * A configuration which can be loaded and saved.
 *
 * @see #load(InputStream)
 * @see #getProperty(String)
 */
public abstract class Config {

    private final String name;
    private boolean loaded;

    /**
     * @param name the config name. This is not indicative of where this
     *             config resides. It exists for the user to know which
     *             configuration is being referred to.
     * @throws NullPointerException if {@code name} is {@code null}.
     */
    public Config(@NotNull String name) {
        this.name = Objects.requireNonNull(name, "name cannot be null");
    }

    /**
     * The name of this configuration is <i>not</i> indicative of where it
     * was loaded. It exists only for the user to know which configuration
     * is being referred to.
     *
     * @return the name of this config.
     */
    public @NotNull String getName() {
        return this.name;
    }

    private void requireLoaded() {
        if (!loaded) {
            throw new IllegalStateException("config must be loaded");
        }
    }

    /**
     * The implementation for {@link #load(InputStream)}.
     *
     * @param in the {@code InputStream} to load the config from.
     *           This should <i>not</i> be closed by this method.
     * @throws IOException if an I/O error occurs.
     */
    protected abstract void loadImpl(@NotNull InputStream in) throws IOException;

    /**
     * Loads the configuration from an {@code InputStream}. If the config
     * has already been loaded, calling this method will have the effect of
     * refreshing its contents.
     *
     * @param in the {@code InputStream} to load the config from.
     *           This will <i>not</i> be automatically closed.
     * @return {@code in} as provided, allowing for a chained method call
     * to {@link InputStream#close()}.
     * @throws NullPointerException if {@code in} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public final @NotNull InputStream load(@NotNull InputStream in) throws IOException {
        Objects.requireNonNull(in, "in cannot be null");
        this.loadImpl(in);
        this.loaded = true;
        return in;
    }

    /**
     * Loads the configuration from the contents of a given URL. If the
     * config has already been loaded, calling this method will have the
     * effect of refreshing its contents.
     *
     * @param url the location of the config.
     * @throws NullPointerException if {@code url} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public final void loadURL(@NotNull URL url) throws IOException {
        Objects.requireNonNull(url, "url cannot be null");
        this.load(url.openStream()).close();
    }

    /**
     * Loads the configuration from a resource on the classpath. If the
     * config has already been loaded, calling this method will have the
     * effect of refreshing its contents.
     *
     * @param path the location of the config.
     * @throws NullPointerException if {@code path} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public final void loadResource(@NotNull String path) throws IOException {
        Objects.requireNonNull(path, "path cannot be null");

        URL configUrl = this.getClass().getResource(path);
        if (configUrl == null) {
            String msg = "no such resource \"" + path + "\"";
            throw new IOException(msg);
        }

        InputStream in = configUrl.openStream();
        this.load(in).close();
    }

    /**
     * Loads the configuration from a file on the file system. If the
     * config has already been loaded, calling this method will have the
     * effect of refreshing its contents.
     *
     * @param file the config file.
     * @throws NullPointerException if {@code file} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public final void loadFile(@NotNull File file) throws IOException {
        Objects.requireNonNull(file, "file cannot be null");
        FileInputStream in = new FileInputStream(file);
        this.load(in).close();
    }

    /**
     * Loads the configuration from a file on the file system. If the
     * config has already been loaded, calling this method will have the
     * effect of refreshing its contents.
     *
     * @param path the location of the config file.
     * @throws NullPointerException if {@code path} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     */
    public final void loadFile(@NotNull String path) throws IOException {
        Objects.requireNonNull(path, "path cannot be null");
        this.loadFile(new File(path));
    }

    /**
     * @param key the key of the property.
     * @return {@code true} if a property with the given key is present,
     * {@code false} otherwise.
     * @throws NullPointerException if {@code key} is {@code null}.
     */
    public final boolean hasProperty(@NotNull String key) {
        Objects.requireNonNull(key, "key cannot be null");
        return this.checkProperty(key);
    }

    /**
     * Checks if a property exists for {@link #hasProperty(String)}.
     *
     * @param key the key of the property to check.
     * @return {@code true} if a property with the given key is present,
     * {@code false} otherwise.
     */
    protected abstract boolean checkProperty(@NotNull String key);

    /**
     * @param key the key of the property to get.
     * @return the value of the property, may be {@code null} if that is the
     * value stored for it.
     * @throws NullPointerException  if {@code key} is {@code null}.
     * @throws IllegalStateException if this config has yet to be loaded;
     *                               if no such property {@code key} exists
     *                               in the configuration.
     */
    public final @Nullable String getProperty(@NotNull String key) {
        Objects.requireNonNull(key, "key cannot be null");
        this.requireLoaded();
        if (!this.hasProperty(key)) {
            String msg = "missing required config";
            msg += " \"" + key + "\"";
            throw new ConfigException(msg);
        }
        return this.fetchProperty(key);
    }

    /**
     * Fetches the property for {@link #getProperty(String)}.
     *
     * @param key the key of the property to fetch.
     * @return the value of the property.
     */
    protected abstract @Nullable String fetchProperty(@NotNull String key);

    /**
     * @param key      the key of the property to request.
     * @param fallback the value to return if no such property {@code key}
     *                 exists in the configuration.
     * @return the value of the property, may be {@code null} if that is the
     * value stored for it.
     * @throws NullPointerException  if {@code key} is {@code null}.
     * @throws IllegalStateException if this config has yet to be loaded.
     */
    public final @Nullable String getProperty(@NotNull String key,
                                              @Nullable String fallback) {
        Objects.requireNonNull(key, "key cannot be null");
        if (!this.hasProperty(key)) {
            return fallback;
        }
        return this.getProperty(key);
    }

}
