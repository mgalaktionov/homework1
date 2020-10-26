package ru.digitalhabbits.homework1.service;

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

public class PluginLoader {
    private static final int CLASS_EXT_LENGTH = ".class".length();

    @Nonnull
    public List<Class<? extends PluginInterface>> loadPlugins(@Nonnull String pluginDirName){
        var currentDir = System.getProperty("user.dir");
        var searchDir = currentDir+"/"+pluginDirName+"/";
        var pluginDir = new File(searchDir);

        var classNames = new ArrayList<String>();
        for(String jar: pluginDir.list()){
            classNames.addAll(getClassesNames(searchDir+"/"+jar));
        }


        var loader = new URLClassLoader(Arrays.stream(pluginDir.list())
                .map(fileName -> {
                    try {
                        return new URL("file:///" + searchDir + fileName);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).toArray(URL[]::new));

        var classes = new ArrayList<Class<? extends PluginInterface>>();
        for(String className: classNames){
            try {
                classes.add((Class<? extends PluginInterface>) loader.loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            loader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private List<String> getClassesNames(String jarName) {
        ArrayList<String> classes = new ArrayList<>();
        try (
                var fis = new FileInputStream(jarName);
                var jarFile = new JarInputStream(fis)
        ){

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
