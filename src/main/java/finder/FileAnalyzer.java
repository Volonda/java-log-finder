package finder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.nio.file.Path;

public class FileAnalyzer {

    public List<SearchEntry> tree(String path, String extension, String search) throws Exception
    {
        ArrayList<SearchEntry> collection = new ArrayList<>();

        Stream files = Files.walk(Paths.get(path))
            .filter(Files::isRegularFile)
            .filter(f -> f.toString().endsWith("." +  extension))
        ;

        for (Path file :  (Iterable<Path>)files::iterator) {

            addSearchEntry(
                new File(file.toString()),
                search,
                collection
            );
        }

        return collection;
    }


    private void addSearchEntry(File file, String search, List<SearchEntry> collection) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(file);
        Pattern p = Pattern.compile(".*" + search + ".*");

        int lineNum = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lineNum++;
            Matcher m = p.matcher(line);
            if (m.matches()) {
                SearchEntry entry = new SearchEntry(file, lineNum, line);
                collection.add(entry);
            }
        }
    }
}
