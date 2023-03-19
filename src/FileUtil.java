import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class FileUtil {

    private FileUtil() {
    }

    public static String readFirstLineFromFile(String filePath) {
        try (BufferedReader reader = getBufferedReader(filePath)) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Could not read line from file");
        }

        return null;
    }

    private static BufferedReader getBufferedReader(String filePath) {
        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(ResourceBase.class.getResourceAsStream(filePath)),
                        StandardCharsets.UTF_8));
    }
}
