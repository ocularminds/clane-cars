
package clane.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Festus B Jejelowo
 * @author festus.jejelowo@ocularminds.com
 */
public class FileParser {

    public static List<String[]> parse(String path) throws Exception {
        List<String[]> records;
        File file = new File(path);
        InputStream inputFS = new FileInputStream(file);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputFS))) {
            // skip the header of the csv
            records = br.lines().skip(1).map((line) -> line.split(",")).collect(Collectors.toList());
        }
        return records;
    }
}
