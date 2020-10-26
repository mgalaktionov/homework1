package ru.digitalhabbits.homework1.service;

import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.slf4j.LoggerFactory.getLogger;

public class PluginLoader {
    private static final Logger logger = getLogger(PluginLoader.class);
    private final int CLASS_EXT_LENGTH = ".class".length();

    //private static final String PLUGIN_EXT = ".jar";
    private static final String PACKAGE_TO_SCAN = "ru.digitalhabbits.homework1.plugin.";

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName){
        var currentDir = System.getProperty("user.dir");
        var searchDir = currentDir+"/"+pluginDirName+"/";
        var pluginDir = new File(searchDir);

        var classNames = new ArrayList<String>();
        for(String jar: pluginDir.list()){
            classNames.addAll(getClasseNames(searchDir+"/"+jar));
        }

        var urls = Arrays.stream(pluginDir.list())
                .map(fileName -> {
                    try {
                        return new URL("file:///" +searchDir+ fileName);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());


        var loader = new URLClassLoader(urls.toArray(new URL[urls.size()]));

        var classes = new ArrayList<Class<? extends PluginInterface>>();
        for(String className: classNames){
            try {
                classes.add((Class<? extends PluginInterface>) loader.loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return classes;
    }

    private List<String> getClasseNames(String jarName) {
        ArrayList classes = new ArrayList();

        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarName));
            JarEntry jarEntry;

            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if (jarEntry.getName().endsWith(".class")) {
                    var className = jarEntry.getName().replaceAll("/", "\\.");
                    classes.add(className.substring(0,className.length()-CLASS_EXT_LENGTH));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
