package bogush;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FilterParserTest {
    @Test (expected = FilterParseException.class)
    public void parseEmpty() throws Exception {
        FilterParser.parse("");
    }

    @Test (expected = FilterParseException.class)
    public void parseEmptyParenthesis() throws Exception {
        FilterParser.parse("()");
    }

    @Test (expected = FilterParseException.class)
    public void parseWrongParenthesisCount() throws Exception {
        FilterParser.parse("((()");
    }

    @Test (expected = FilterParseException.class)
    public void parseAndEmpty() throws Exception {
        FilterParser.parse("And((),(),())");
    }

    @Test (expected = FilterParseException.class)
    public void parseEmptyCommaSeparatedSequence() throws Exception {
        FilterParser.parse("(,,,)");
    }

    @Test (expected = FilterParseException.class)
    public void parse10EmptyLines() throws Exception {
        FilterParser.parse(Paths.get("tests/input/10lines"));
    }

    @Test
    public void parse6FiltersInputFile() throws Exception {
        Path config = Paths.get("tests/input/6filters.input");
        Filter[] actuals = FilterParser.parse(config);
        assertEquals(6, actuals.length);
        Filter extensionFilter = new FileExtensionFilter(".input");
        Filter timeFilter = new TimeFilterGreater(1487311235);
        Filter[] expecteds = {
                extensionFilter,
                timeFilter,
                new FilterAnd(new Filter[]{
                        timeFilter
                }),
                new FilterOr(new Filter[]{
                        timeFilter, timeFilter
                }),
                new FilterAnd(new Filter[]{
                        new FilterOr(new Filter[]{
                                new FilterAnd(new Filter[]{
                                        timeFilter
                                })
                        })
                }),
                new FilterAnd(new Filter[]{
                        extensionFilter,
                        timeFilter,
                        new FilterOr(new Filter[]{
                                timeFilter,
                                extensionFilter
                        })
                })
        };
        for (int i = 0; i < actuals.length; i++) {
            assertEquals(expecteds[i].toString(), actuals[i].toString());
        }
    }
}