package csci4490.uno.commons.config;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig extends Config {

    private final Properties config;

    public PropertiesConfig(@NotNull String name) {
        super(name);
        this.config = new Properties();
    }

    @Override
    public void loadImpl(@NotNull InputStream in) throws IOException {
        config.clear();
        config.load(in);
    }

    @Override
    public boolean checkProperty(@NotNull String key) {
        return config.containsKey(key);
    }

    @Override
    public String fetchProperty(@NotNull String key) {
        return config.getProperty(key);
    }

}
