package loc;

import loc.filters.Filter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigFileReader {
	public static Filter[] readFilters(Path filePath) {
		List<Filter> filterList = new ArrayList<>();
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toFile()))) {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				filterList.add(FilterFactory.create(line));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filterList.toArray(new Filter[filterList.size()]);
	}
}
