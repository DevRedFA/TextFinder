import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ivan on 30.07.2017.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class); //LoggerOne

    public static List<File> getFiles(String addr, String fileFormat) {
        List<File> list = new ArrayList<>();
        try {
            logger.info("Trying open addr: {}", addr);
            list = Files.walk(Paths.get(addr))
                    .filter(Files::isRegularFile)
                    .filter(s -> s.toString().endsWith(fileFormat))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            logger.info("list with files correctly created");
        } catch (IOException e) {
            logger.error("wrong addr: {}, \n {}", addr, e);
        }
        return list;
    }


    public static boolean isFileContains(String s, File f) throws IOException {
        logger.info("searching: \"{}\" in file {}", s, f);

//        looks cool, bad working with big files:
//        long count = Files.lines(f.toPath())
//                .filter(q -> q.contains(s))
//                .count();

        long count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ((line.contains(s))) {
                    count++;
                }
            }
        } catch (IOException e) {
            logger.error("Problems while opening: {}", f, e);
        }
        logger.info("finded {} entry", count);
        if (count > 0) return true;
        return false;
    }

}
