package edu.isi.misd.image.gateway.conversion.loci.test;

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.loci.LociImageSource;

public class LociImageSourceTest {

    private static final String testFile = LociImageSourceTest.class
    .getResource("/test.jpg").getPath();

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1_null1stArg() throws Exception {
        new LociImageSource(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2_null1stArg() throws Exception {
        new LociImageSource(null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2_neg2ndArg() throws Exception {
        new LociImageSource(testFile, -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2_neg3rdArg() throws Exception {
        new LociImageSource(testFile, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1_invalidFile() throws Exception {
        new LociImageSource("/notafile.jpg");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2_invalidFile() throws Exception {
        new LociImageSource("/notafile.jpg", 0, 0);
    }

    @Test
    public void testGetFilename() throws Exception {
        final String filename = new LociImageSource(testFile).getFilename();
        if (filename == null) {
            fail("getFilename() should not be null.");
        }
        if (!filename.equals(testFile)) {
            fail("getFilename() returned " + filename + ", should be "
                    + testFile);
        }
    }

    @Test
    public void testGetSeriesNumber() throws Exception {
        final int series = new LociImageSource(testFile, 0, 0)
        .getSeriesNumber();
        if (series != 0) {
            fail("getSeriesNumber() returned " + series + ", should be 0");
        }
    }

    @Test
    public void testGetImageNumber() throws Exception {
        final int image = new LociImageSource(testFile, 0, 0).getImageNumber();
        if (image != 0) {
            fail("getSeriesNumber() returned " + image + ", should be 0");
        }
    }

    @Test
    public void testGetWidth() throws Exception {
        final BufferedImage image = ImageIO.read(new File(testFile));
        final LociImageSource source = new LociImageSource(testFile);
        if (source.getWidth() != image.getWidth()) {
            fail("getWidth() returned " + source.getWidth() + ", should be "
                    + image.getWidth());
        }
    }

    @Test
    public void testGetHeight() throws Exception {
        final BufferedImage image = ImageIO.read(new File(testFile));
        final LociImageSource source = new LociImageSource(testFile);
        if (source.getHeight() != image.getHeight()) {
            fail("getHeight() returned " + source.getHeight() + ", should be "
                    + image.getHeight());
        }
    }

    @Test
    public void testReadBytes() throws Exception {
        final LociImageSource source = new LociImageSource(testFile);
        final byte[] bytes = source.readBytes(0, 0, 20, 20);

        // w * h * 3 channels = 1200
        if (bytes.length != 1200) {
            fail("getBytes() returned " + bytes.length
                    + " bytes, expected 400.");
        }
        source.close();
    }

    @Test
    public void testReadImage() throws Exception {
        final LociImageSource source = new LociImageSource(testFile);
        final BufferedImage image = source.readImage(0, 0, 20, 30);
        if (image.getWidth() != 20 || image.getHeight() != 30) {
            fail("readImage() returned image of size " + image.getWidth() + "x"
                    + image.getHeight() + ", expected 20x30.");
        }
    }
}
