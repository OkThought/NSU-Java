package loc.statistics;

import loc.IFilterSerializer;
import loc.filters.*;

public interface IStatisticsSerializer {
	String serialize() throws IFilterSerializer.FilterSerializerException;
}
