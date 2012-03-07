package edu.isi.misd.image.gateway.conversion.zoomify.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.tile.ZoomLevelConfigurationImplementation;
import edu.isi.misd.image.gateway.conversion.zoomify.ZoomifyTileNamingConvention;

public class ZoomifyTileNamingConventionTest {

    ZoomifyTileNamingConvention namingConvention;

    @Before
    public void setUp() {
        namingConvention = new ZoomifyTileNamingConvention();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTileFileName_nullExtension() throws Exception {
        namingConvention.getTileFileName(0, 0, 5, null,
                new ZoomLevelConfigurationImplementation(256, 256, 256, 2.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTileFileName_nullZoomLevel() throws Exception {
        namingConvention.getTileFileName(0, 0, 5, "jpg", null);
    }

    @Test
    public void testGetTileFileName() throws Exception {
        final int level = 1, row = 2, column = 3;
        final String extension = "jpg";
        final ZoomLevelConfigurationImplementation zoomLevelConfig = new ZoomLevelConfigurationImplementation(
                2560, 2560, 256, 2.0);
        final String tileFilename = new StringBuffer("TileGroup0/")
                .append(zoomLevelConfig.getNumberOfZoomLevels() - level - 1)
                .append("-").append(column).append("-").append(row).append(".")
                .append(extension).toString();
        final String conventionName = namingConvention.getTileFileName(level,
                row, column, extension, zoomLevelConfig);

        if (!conventionName.equals(tileFilename)) {
            fail("getTileFileName() returned " + conventionName + ", expected "
                    + tileFilename);
        }
    }
}
