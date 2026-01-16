import org.junit.jupiter.api.Test;
import org.zadanko.exception.DataIntegrityException;
import org.zadanko.persistence.PortfolioFileRepository;
import org.zadanko.persistence.TextPortfolioFileRepository;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersistenceTest {

    @Test
    void invalidLotQuantityCausesDataIntegrityException() throws Exception {
        Path tempFile = Files.createTempFile("portfolio", ".txt");

        Files.writeString(tempFile,
                "HEADER|CASH|1000.0\n" +
                        "ASSET|SHARE|AAPL\n" +
                        "LOT|2023-05-10|10|150.0\n" +
                        "LOT|2023-06-12|5|155.0\n" +
                        "LOT|BROKEN|LINE\n"
        );

        PortfolioFileRepository repo =
                new TextPortfolioFileRepository();

        assertThrows(
                DataIntegrityException.class,
                () -> repo.load(tempFile)
        );
    }
}