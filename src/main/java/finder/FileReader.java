package finder;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileReader
{
    private static int linesInPage = 500;

    public static List<String> readPage(File file, int page) throws Exception {

        if (page < 0) {

           throw new Exception("page must be >= 0");
        }

        List<String> lines = new ArrayList<>();

        BufferedReader in = new BufferedReader(new java.io.FileReader(file));
        String line = in.readLine();

        int lineNum = 0;
        int lineNumFrom = page * linesInPage;
        int lineNumTill = ((page + 1) * linesInPage);

        while (line != null && lineNum < lineNumTill) {

            if (lineNum >= lineNumFrom) {
                lines.add(line);
            }

            lineNum++;
            line = in.readLine();
        }

        return lines;
    }
}
