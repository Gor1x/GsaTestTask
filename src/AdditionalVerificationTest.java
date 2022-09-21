// Do not modify or delete this file. It is used to verify your solution before submission.
// (c) GSA Capital

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdditionalVerificationTest {

    @ParameterizedTest
    @MethodSource("verificationTestsProvider")
    public void runTest(String testId, String input, String expectedOutput) throws IOException {

        String stdOut = getOrderBookOutput(input);
        System.out.println(stdOut);

        assertEquals(expectedOutput, stdOut);
    }

    private static String getOrderBookOutput(String input) throws IOException {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        try (ByteArrayInputStream newIn = new ByteArrayInputStream(input.getBytes()); ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos, true)) {
            System.setIn(newIn);
            System.setOut(ps);

            SETSOrderBookExercise.main(new String[0]);

            return baos.toString(StandardCharsets.UTF_8).trim().replaceAll("\\r\\n?", "\n");

        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }

    static Stream<Arguments> verificationTestsProvider() {
        return Stream.of(Arguments.of("TwoSells",
                        ""
                                .concat("S,3,2,4\n")
                                .concat("S,2,1,5"),
                        ""
                                .concat("+-----------------------------------------------------------------+\n")
                                .concat("| BUY                            | SELL                           |\n")
                                .concat("| Id       | Volume      | Price | Price | Volume      | Id       |\n")
                                .concat("+----------+-------------+-------+-------+-------------+----------+\n")
                                .concat("|          |             |       |      2|            4|         3|\n")
                                .concat("+-----------------------------------------------------------------+\n")
                                .concat("+-----------------------------------------------------------------+\n")
                                .concat("| BUY                            | SELL                           |\n")
                                .concat("| Id       | Volume      | Price | Price | Volume      | Id       |\n")
                                .concat("+----------+-------------+-------+-------+-------------+----------+\n")
                                .concat("|          |             |       |      1|            5|         2|\n")
                                .concat("|          |             |       |      2|            4|         3|\n")
                                .concat("+-----------------------------------------------------------------+")
                ),
                Arguments.of("TwoBuys",
                        ""
                                .concat("B,1,1,1\n")
                                .concat("B,2,2,1"),
                        ""
                                .concat("+-----------------------------------------------------------------+\n")
                                .concat("| BUY                            | SELL                           |\n")
                                .concat("| Id       | Volume      | Price | Price | Volume      | Id       |\n")
                                .concat("+----------+-------------+-------+-------+-------------+----------+\n")
                                .concat("|         1|            1|      1|       |             |          |\n")
                                .concat("+-----------------------------------------------------------------+\n")
                                .concat("+-----------------------------------------------------------------+\n")
                                .concat("| BUY                            | SELL                           |\n")
                                .concat("| Id       | Volume      | Price | Price | Volume      | Id       |\n")
                                .concat("+----------+-------------+-------+-------+-------------+----------+\n")
                                .concat("|         2|            1|      2|       |             |          |\n")
                                .concat("|         1|            1|      1|       |             |          |\n")
                                .concat("+-----------------------------------------------------------------+")
                )
        );
    }
}