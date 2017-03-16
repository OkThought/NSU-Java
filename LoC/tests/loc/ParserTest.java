package loc;

import loc.filter.*;
import loc.filter.filters.Aggregate.And;
import loc.filter.filters.Aggregate.Or;
import loc.filter.filters.FileExtensionFilter;
import loc.filter.filters.TimeModified.ModifiedLater;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ParserTest {
	@Test (expected = Parser.ParseException.class)
	public void parseEmpty() throws Exception {
		new Or.Serializer().parse("");
	}
/* parenthesis are only for filter sequences now (used only in AggregateFilter)
	@Test (expected = FilterParser.ParseException.class)
	public void parseEmptyParenthesis() throws Exception {
		FilterParser.parseOr("()");
	}

	@Test (expected = FilterParser.ParseException.class)
	public void parseWrongParenthesisCount() throws Exception {
		FilterParser.parseAnd("((()");
	}

	@Test (expected = FilterParser.ParseException.class)
	public void parseAndEmpty() throws Exception {
		FilterParser.parseAnd("(() () ())");
	}

	@Test (expected = FilterParser.ParseException.class)
	public void parseEmptyCommaSeparatedSequence() throws Exception {
		FilterParser.parseAnd("(   )");
	}
*/

	@Test (expected = Parser.ParseException.class)
	public void parse10EmptyLines() throws Exception {
		ConfigFileReader.readFilters(Paths.get("tests/input/10lines"));
	}

	@Test
	public void parse6FiltersInputFile() throws Exception {
		Path config = Paths.get("tests/input/6filters.input");
		Filter[] actualFilters = ConfigFileReader.readFilters(config);
		assertEquals(6, actualFilters.length);
		Filter extensionFilter = new FileExtensionFilter(".input");
		Filter timeFilter = new ModifiedLater(1487311235);
		Filter[] expectedFilters = {
				extensionFilter,
				timeFilter,
				new And(new Filter[]{
						timeFilter
				}),
				new Or(new Filter[]{
						timeFilter, timeFilter
				}),
				new And(new Filter[]{
						new Or(new Filter[]{
								new And(new Filter[]{
										timeFilter
								})
						})
				}),
				new And(new Filter[]{
						extensionFilter,
						timeFilter,
						new Or(new Filter[]{
								timeFilter,
								extensionFilter
						})
				})
		};
		for (int i = 0; i < actualFilters.length; i++) {
			assertEquals(expectedFilters[i].toString(), actualFilters[i].toString());
		}
	}
}