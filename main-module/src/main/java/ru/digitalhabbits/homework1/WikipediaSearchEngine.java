package ru.digitalhabbits.homework1;

import org.slf4j.Logger;
import ru.digitalhabbits.homework1.plugin.PluginInterface;
import ru.digitalhabbits.homework1.service.FileEngine;
import ru.digitalhabbits.homework1.service.PluginEngine;
import ru.digitalhabbits.homework1.service.PluginLoader;
import ru.digitalhabbits.homework1.service.WikipediaClient;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.*;

import static org.slf4j.LoggerFactory.getLogger;

public class WikipediaSearchEngine {
    private static final Logger logger = getLogger(WikipediaSearchEngine.class);
    private static final String PLUGIN_POSTFIX = "Plugin";
    private static final String PLUGIN_DIR = "plugins";

    public void search(@Nonnull String searchString) {
        var startTime = System.currentTimeMillis();
        logger.info("Searching '{}' on wikipedia", searchString);

        // 1. сделать запрос в wikipedia, получить результат в формате json.
        final WikipediaClient client = new WikipediaClient();
        var pool = Executors.newFixedThreadPool(3);
        Callable<String> callable = new WikiCallable(searchString,client);
        Future<String> futureText = pool.submit(callable);
        //final String text = client.search(searchString);

        //logger.info("FORMATTED TEXT \n {}", text);

        // 2. очистить папку с результатами
        final FileEngine fileEngine = new FileEngine();
        fileEngine.cleanResultDir();

        // 3. найти и загрузить все плагины
        final PluginLoader pluginLoader = new PluginLoader();
        final List<Class<? extends PluginInterface>> plugins =
                pluginLoader.loadPlugins(PLUGIN_DIR);

        // 4. выполнить действия над результатом и записать в файл
        final PluginEngine pluginEngine = new PluginEngine();
        String text = "";
        try {
            text = futureText.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            pool.shutdown();
        } finally {
            pool.shutdown();
        }
        if(!text.isEmpty()) {
            for (Class<? extends PluginInterface> plugin : plugins) {
                if (plugin.getSimpleName().contains(PLUGIN_POSTFIX)) {
                    final String result = pluginEngine.applyPlugin(plugin, text);
                    final String pluginName = plugin.getSimpleName();
                    fileEngine.writeToFile(result, pluginName);
                }
            }
        }
        logger.info("DURATION OF PROCESSING '{}", System.currentTimeMillis() - startTime);
    }

    private static class WikiCallable implements Callable{

        private final String searchString;
        private final WikipediaClient client;

        private WikiCallable(String searchString, WikipediaClient client) {
            this.searchString = searchString;
            this.client = client;
        }

        @Override
        public String call() throws Exception {
            logger.info("STARTED WIKISEARCH THREAD");
            return client.search(searchString);
        }
    }
}
