package edu.isi.misd.image.gateway.conversion.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.ConvertImageToZoomifyTiles;

public class ConvertImageToZoomifyTilesTest {

    private String testDir;
    private final String testFile = ConvertImageToZoomifyTilesTest.class
    .getResource("/test.jpg").getPath();

    @Before
    public void setUp() {
        final File d = new File("test_dir");
        d.mkdir();
        testDir = d.getAbsolutePath();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null1stArg() throws Exception {
        new ConvertImageToZoomifyTiles((String) null, testDir);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null2ndArg() throws Exception {
        new ConvertImageToZoomifyTiles(testFile, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_nonExistantSource() throws Exception {
        new ConvertImageToZoomifyTiles("/notafile.jpg", testDir);
    }

    @Test
    public void testGetSourceImage() throws Exception {
        if (new ConvertImageToZoomifyTiles(testFile, testDir)
                .getSourceFilename() == null) {
            fail("getSourceImage() should not return null.");
        }
    }

    @Test
    public void testGetDestintationFilename() throws Exception {
        if (new ConvertImageToZoomifyTiles(testFile, testDir)
                .getDestinationFilename() == null) {
            fail("getDestinationFilename() should not return null.");
        }
    }

    @After
    public void tearDown() {
        removeDir(new File(testDir));
    }

    private void removeDir(final File dir) {
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isDirectory()) {
                    removeDir(f);
                } else {
                    f.delete();
                }
            }
        }
        dir.delete();
    }
}
