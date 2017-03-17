package loc;

import loc.filter.Filter;
import loc.filter.FilterFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigFileReader {
	public static Filter[] readFilters(Path filePath) throws IOException {
		List<Filter> filterList = new ArrayList<>();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			filterList.add(FilterFactory.create(line));
		}
		return filterList.toArray(new Filter[filterList.size()]);
	}
}
