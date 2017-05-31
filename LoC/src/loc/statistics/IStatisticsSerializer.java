package loc.statistics;

import loc.SerializeException;

public interface IStatisticsSerializer {
	String serialize() throws SerializeException;
}
