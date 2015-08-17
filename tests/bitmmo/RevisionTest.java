package bitmmo;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.junit.Test;

import me.themallard.bitmmo.api.Revision;

public class RevisionTest {

	@Test
	public void testCreate() throws URISyntaxException {
		String name = "1232";
		String path = "jars\\HTMudWeb_" + name + ".jar";

		Revision r = Revision.create(name);

		assertEquals(name, r.getName());
		assertEquals(true, r.getFile().getPath().contains(path));
	}

	// TODO: Test parse and parseResources
}
