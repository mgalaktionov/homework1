package ru.digitalhabbits.homework1.service;

import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import static java.util.Arrays.stream;
import static org.slf4j.LoggerFactory.getLogger;

public class FileEngine {
    private final Logger logger = getLogger(FileEngine.class);
    private static final String RESULT_FILE_PATTERN = "results-%s.txt";
    private static final String RESULT_DIR = "results";
    private static final String RESULT_EXT = "txt";

    public boolean writeToFile(@Nonnull String text, @Nonnull String pluginName) {
        // TODO: NotImplemented
        String resultDirPath = System.getProperty("user.dir") + RESULT_DIR;
        String fileName = resultDirPath + "\\" + String.format(RESULT_FILE_PATTERN, pluginName);
        logger.info("'{}' results are in '{}'", pluginName, fileName);
        try {
            RandomAccessFile stream = new RandomAccessFile(fileName, "rw");
            FileChannel channel = stream.getChannel();

            FileLock lock = null;
            try {
                lock = channel.tryLock();
            } catch (final OverlappingFileLockException e) {
                stream.close();
                channel.close();
            }
            stream.writeChars(text);
            lock.release();

            stream.close();
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void cleanResultDir() {
        final String currentDir = System.getProperty("user.dir");
        final File resultDir = new File(currentDir + "/" + RESULT_DIR);
        logger.info("Path to result directory is '{}'",resultDir.getName());
        if (resultDir.list() != null) {
            stream(resultDir.list((dir, name) -> name.endsWith(RESULT_EXT)))
                    .forEach(fileName -> new File(resultDir + "/" + fileName).delete());
        }
    }
}
