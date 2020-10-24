package ru.digitalhabbits.homework1.service;

import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);

    //private static final String PLUGIN_EXT = ".jar";
    //private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin";

    private static final String COUNTER_PLUGIN = "plugins/counter-plugin.jar";
    private static final String FREQ_DICT_PLUGIN = "plugins/frequency-dictionary-plugin.jar";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName)
            throws MalformedURLException {
        // TODO: NotImplemented
        String counterPluginURL = "file://" + System.getProperty("user.dir") + COUNTER_PLUGIN;
        String freqDictPluginURL = "file://" + System.getProperty("user.dir") + FREQ_DICT_PLUGIN;
        URL[] urls = {new URL(counterPluginURL), new URL(freqDictPluginURL)};
        URLClassLoader classLoader = new URLClassLoader(urls);
        List<Class<? extends PluginInterface>> classes = new ArrayList<>();

        return classes;
    }
}
