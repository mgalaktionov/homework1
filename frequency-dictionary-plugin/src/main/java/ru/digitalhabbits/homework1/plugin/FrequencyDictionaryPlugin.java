package ru.digitalhabbits.homework1.plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class FrequencyDictionaryPlugin
        implements PluginInterface {

    @Nullable
    @Override
    public String apply(@Nonnull String text) {
        // TODO: NotImplemented
        FrequencyDictionary dictionary = new FrequencyDictionary(text.toLowerCase());
        System.out.println(dictionary.toString());
        return dictionary.toString();
    }


}
