package loc;

import loc.filter.*;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FilterParserTest {
	@Test (expected = FilterParser.FilterParseException.class)
	public void parseEmpty() throws Exception {
		new FilterParser().parseOr("");
	}
/* parenthesis are only for filter sequences now (used only in AggregateFilter)
	@Test (expected = FilterParser.FilterParseException.class)
	public void parseEmptyParenthesis() throws Exception {
		FilterParser.parseOr("()");
	}

	@Test (expected = FilterParser.FilterParseException.class)
	public void parseWrongParenthesisCount() throws Exception {
		FilterParser.parseAnd("((()");
	}

	@Test (expected = FilterParser.FilterParseException.class)
	public void parseAndEmpty() throws Exception {
		FilterParser.parseAnd("(() () ())");
	}

	@Test (expected = FilterParser.FilterParseException.class)
	public void parseEmptyCommaSeparatedSequence() throws Exception {
		FilterParser.parseAnd("(   )");
	}
*/

	@Test (expected = FilterParser.FilterParseException.class)
	public void parse10EmptyLines() throws Exception {
		ConfigFileReader.readFilters(Paths.get("tests/input/10lines"));
	}

	@Test
	public void parse6FiltersInputFile() throws Exception {
		Path config = Paths.get("tests/input/6filters.input");
		IFilter[] actualFilters = ConfigFileReader.readFilters(config);
		assertEquals(6, actualFilters.length);
		IFilter extensionFilter = new FileExtensionFilter(".input");
		IFilter timeFilter = new ModifiedLaterFilter(1487311235);
		IFilter[] expectedFilters = {
				extensionFilter,
				timeFilter,
				new FilterAnd(new IFilter[]{
						timeFilter
				}),
				new FilterOr(new IFilter[]{
						timeFilter, timeFilter
				}),
				new FilterAnd(new IFilter[]{
						new FilterOr(new IFilter[]{
								new FilterAnd(new IFilter[]{
										timeFilter
								})
						})
				}),
				new FilterAnd(new IFilter[]{
						extensionFilter,
						timeFilter,
						new FilterOr(new IFilter[]{
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