package loc;

import loc.filter.Filter;
import loc.filter.FilterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConfigFileReader {
	public static Filter[] readFilters(Path filePath) throws IOException {
		HashSet<Filter> filters = new HashSet<>();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty()) continue;
			filters.add(FilterFactory.create(line));
		}
		return filters.toArray(new Filter[filters.size()]);
	}
}
