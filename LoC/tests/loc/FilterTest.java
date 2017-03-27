package loc;

import loc.filter.*;
import loc.filter.filters.aggregate.And;
import loc.filter.filters.aggregate.Not;
import loc.filter.filters.aggregate.Or;
import loc.filter.filters.FileExtensionFilter;
import loc.filter.filters.time.ModifiedEarlier;
import loc.filter.filters.time.ModifiedLater;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilterTest {
	Filter extensionFilter0 = new FileExtensionFilter("java");
	Filter extensionFilter1 = new FileExtensionFilter("java");
	Filter extensionFilter2 = new FileExtensionFilter("cpp");
	Filter timeFilter0 = new ModifiedLater(1);
	Filter timeFilter1 = new ModifiedLater(1);
	Filter timeFilter2 = new ModifiedLater(2);
	Filter timeFilter3 = new ModifiedEarlier(1);
	Filter timeFilter4 = new ModifiedEarlier(2);
	Filter andFilter0 = new And(new Filter[]{extensionFilter1, extensionFilter2});
	Filter andFilter1 = new And(new Filter[]{extensionFilter1, extensionFilter2});
	Filter andFilter2 = new And(new Filter[]{extensionFilter1, timeFilter1});
	Filter orFilter0 = new Or(new Filter[]{extensionFilter1, extensionFilter2});
	Filter orFilter1 = new Or(new Filter[]{extensionFilter1, extensionFilter2});
	Filter orFilter2 = new Or(new Filter[]{extensionFilter1, timeFilter1});
	Filter notFilter0 = new Not(extensionFilter1);
	Filter notFilter1 = new Not(extensionFilter1);
	Filter notFilter2 = new Not(timeFilter1);

	@Test
	public void differentToString() {
		assertNotEquals(extensionFilter1.toString(), extensionFilter2.toString());
		assertNotEquals(extensionFilter1.toString(), timeFilter1.toString());

		assertNotEquals(timeFilter1.toString(), timeFilter2.toString()); // >1 ≠ >2
		assertNotEquals(timeFilter2.toString(), timeFilter3.toString()); // >2 ≠ <1
		assertNotEquals(timeFilter2.toString(), timeFilter4.toString()); // >2 ≠ <2

		assertNotEquals(andFilter1.toString(), extensionFilter1.toString());
		assertNotEquals(andFilter1.toString(), timeFilter1.toString());
		assertNotEquals(andFilter1.toString(), andFilter2.toString());

		assertNotEquals(orFilter1.toString(), extensionFilter1.toString()); // or ≠ .java
		assertNotEquals(orFilter1.toString(), timeFilter1.toString()); // or ≠ time
		assertNotEquals(orFilter1.toString(), andFilter1.toString()); // or ≠ and
		assertNotEquals(orFilter1.toString(), orFilter2.toString()); // or1 ≠ or2

		assertNotEquals(notFilter1.toString(), extensionFilter1.toString()); // not ≠ .java
		assertNotEquals(notFilter1.toString(), timeFilter1.toString()); // not ≠ time
		assertNotEquals(notFilter1.toString(), andFilter1.toString()); // not ≠ and
		assertNotEquals(notFilter1.toString(), orFilter1.toString()); // not ≠ or
		assertNotEquals(notFilter1.toString(), notFilter2.toString()); // not1 ≠ not2
	}

	@Test
	public void similarToString() {
		assertEquals(extensionFilter0.toString(), extensionFilter1.toString());
		assertEquals(timeFilter0.toString(), timeFilter1.toString());
		assertEquals(andFilter0.toString(), andFilter1.toString());
		assertEquals(orFilter0.toString(), orFilter1.toString());
		assertEquals(notFilter0.toString(), notFilter1.toString());

	}
}