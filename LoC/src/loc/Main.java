package loc;

import loc.filter.Filter;
import loc.statistics.Statistics;
import loc.statistics.StatisticsCollector;
import loc.statistics.StatisticsSerializer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) {
		try {
			if (args.length < 2) {
				System.err.println("At least two arguments required: config file path and dir path");
				return;
			}
			String configFileName = args[0];
            String dir = args[1];

            Path configFilePath = Paths.get(configFileName);
			Path dirToWalk = Paths.get(dir);

			Statistics stats = new Statistics();
			Filter[] filters = ConfigFileReader.readFilters(configFilePath);
			StatisticsCollector collector = new StatisticsCollector(stats, filters);
//            collector.printVisited = true;

            Files.walkFileTree(dirToWalk, collector);
			StatisticsSerializer serializer = new StatisticsSerializer(stats);
			System.out.println(serializer.serialize());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
