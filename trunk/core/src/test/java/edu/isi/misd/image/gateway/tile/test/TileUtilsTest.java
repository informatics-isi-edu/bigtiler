package edu.isi.misd.image.gateway.tile.test;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.CompositeImage;
import edu.isi.misd.image.gateway.conversion.tile.TileUtils;

public class TileUtilsTest {

    private String testDir;
    private static final String testFile = JpegTileWriterImplementationTest.class
            .getResource("/big.jpg").getPath();

    @Before
    public void setUp() throws Exception {
        final File d = new File("test_tile_utils_dir");
        if (!d.mkdir()) {
            throw new Exception("Could not create directory "
                    + d.getAbsolutePath());
        }
        testDir = d.getAbsolutePath();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateLowerZoomLevelTile_null1stArg() throws Exception {
        TileUtils.createLowerZoomLevelTile(null, 0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGlueImageMatrix_null1stArg() throws Exception {
        TileUtils.glueImageMatrix(null);
    }

    @Test
    public void testGlueImageMatrix() throws Exception {
        final File imageFile = new File(testFile);
        final BufferedImage singleImage = ImageIO.read(imageFile);
        final List<List<BufferedImage>> imagesToAdd = new ArrayList<List<BufferedImage>>();
        final int numRows = 3, numCols = 4;
        for (int r = 0; r < numRows; r++) {
            final List<BufferedImage> row = new ArrayList<BufferedImage>();
            for (int c = 0; c < numCols; c++) {
                row.add(ImageIO.read(imageFile));
            }
            imagesToAdd.add(row);
        }
        final CompositeImage result = TileUtils.glueImageMatrix(imagesToAdd);
        if (result.getImage().getWidth() != singleImage.getWidth() * numCols
                || result.getImage().getHeight() != singleImage.getHeight()
                * numRows) {
            fail("glued image is " + result.getImage().getWidth() + "x"
                    + result.getImage().getHeight() + ", expected "
                    + singleImage.getWidth() * numCols + "x"
                    + singleImage.getHeight() * numRows);
        }
    }

    /*
     * @Test public void testCreateLowerZoomLevelTile() throws Exception { final
     * BufferedImage orig = ImageIO.read(new File(testFile)); final
     * ZoomifyTileNamingConvention namingConvention = new
     * ZoomifyTileNamingConvention(); final JpegTileWriterImplementation writer
     * = new JpegTileWriterImplementation( testDir, namingConvention, new
     * ZoomLevelConfigurationImplementation(orig.getWidth(), orig.getHeight(),
     * 256, 2.0)); final String[] tileFilenames = new String[4]; int numFiles =
     * 0; for (int r = 0; r < 2; r++) { for (int c = 0; c < 2; c++) {
     * tileFilenames[numFiles++] = writer.write(orig, 3, r, c); } }
     * 
     * final BufferedImage result = TileUtils.createLowerZoomLevelTile(writer,
     * 3, 0, 0); if (result.getWidth() != orig.getWidth() || result.getHeight()
     * != orig.getHeight()) { fail("lower zoom level image is " +
     * result.getWidth() + "x" + result.getHeight() + ", expected " +
     * orig.getWidth() + "x" + orig.getHeight()); } for (int i = 0; i <
     * tileFilenames.length; i++) { new File(tileFilenames[i]).delete(); } }
     */

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
