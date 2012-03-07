package edu.isi.misd.image.gateway.tile.test;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.tile.ZoomLevelConfigurationImplementation;
import edu.isi.misd.image.gateway.conversion.tile.ZoomLevelNotFoundException;

public class ZoomLevelConfigurationImplementationTest {

    ZoomLevelConfigurationImplementation config;

    @Before
    public void setUp() {
        config = new ZoomLevelConfigurationImplementation(1000, 500, 10, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_bad1stArg() {
        new ZoomLevelConfigurationImplementation(0, 100, 10, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_bad2ndArg() {
        new ZoomLevelConfigurationImplementation(100, 0, 10, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_bad3rdArg() {
        new ZoomLevelConfigurationImplementation(100, 100, 0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_badTileSize() {
        new ZoomLevelConfigurationImplementation(100, 20, 25, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_bad4thArg() {
        new ZoomLevelConfigurationImplementation(100, 100, 10, 1.0);
    }

    // 0: 100 cols 50 rows, 5000 total
    // 1: 50 cols, 25 rows, 1250 total
    // 2: 25 cols, 13 rows, 325 total
    // 3: 13 cols, 7 rows, 91 total
    // 4: 7 cols, 4 rows, 28 total
    // 5: 4 cols, 2 rows, 8 total
    // 6: 2 cols, 1 rows, 2 total
    // 7: 1 cols, 1 row, 1 total
    @Test
    public void testGetNumberOfLevels() {
        if (config.getNumberOfZoomLevels() != 8) {
            fail("getNumberOfZoomLevels() returned "
                    + config.getNumberOfZoomLevels() + " levels, expected 8.");
        }
    }

    @Test
    public void testGetNumberOfRows() throws Exception {
        final int[] expected = { 50, 25, 13, 7, 4, 2, 1, 1 };
        for (int i = 0; i < config.getNumberOfZoomLevels(); i++) {
            if (config.getNumberOfRows(i) != expected[i]) {
                fail("Level " + i + " returned " + config.getNumberOfRows(i)
                        + " rows, expected " + expected[i]);
            }
        }
    }

    @Test
    public void testGetNumberOfColumns() throws Exception {
        final int[] expected = { 100, 50, 25, 13, 7, 4, 2, 1 };
        for (int i = 0; i < config.getNumberOfZoomLevels(); i++) {
            if(config.getNumberOfColumns(i) != expected[i]) {
                fail("Level " + i + " returned " + config.getNumberOfColumns(i) + " columns, expected " + expected[i]);
            }
        }
    }

    @Test
    public void testGetNumberOfTiles() throws Exception {
        final int[] expected = { 5000, 1250, 325, 91, 28, 8, 2, 1 };
        for (int i = 0; i < config.getNumberOfZoomLevels(); i++) {
            if (config.getNumberOfTiles(i) != expected[i]) {
                fail("Level " + i + " returned " + config.getNumberOfTiles(i)
                        + " tiles, expected " + expected[i]);
            }
        }
    }

    @Test(expected = ZoomLevelNotFoundException.class)
    public void testBadZoomLevel() throws Exception {
        config.getNumberOfTiles(20);
    }
}
