package attendance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvFileReader {

    private static final String FILE_PREFIX = "src/main/resources/";
    private static final String FILE_SUFFIX = ".csv";

    public List<String> read(String fileName) {
        Path path = Paths.get(FILE_PREFIX + fileName + FILE_SUFFIX);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽는 중 에러가 발생했습니다.");
        }
    }
}
