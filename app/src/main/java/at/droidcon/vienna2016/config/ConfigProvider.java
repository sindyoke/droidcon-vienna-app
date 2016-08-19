package at.droidcon.vienna2016.config;

/**
 * Created by helmuth on 19/08/16.
 *
 * This interface defines getting configuration values,
 * e.g. which drawer menu is the default or whether there
 * shall be a home entry.
 */

public interface ConfigProvider {
    public void refresh();
    public boolean getBoolean(String key);
    public long getLong(String key);
    public double getDouble(String key);
    public String getString(String key);
}
