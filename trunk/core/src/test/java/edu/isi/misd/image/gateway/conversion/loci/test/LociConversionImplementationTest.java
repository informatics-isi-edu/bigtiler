package edu.isi.misd.image.gateway.conversion.loci.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.loci.LociConversionImplementation;
import edu.isi.misd.image.gateway.conversion.loci.test.LociImageSourceTest;

public class LociConversionImplementationTest {
    private static final String source = LociImageSourceTest.class.getResource(
            "/test.jpg").getPath();

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null1stArg() {
        new LociConversionImplementation(null, "myfile.tif");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null2ndArg() {
        new LociConversionImplementation(source, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_badSource() {
        new LociConversionImplementation("notafile.jpg", "myfile.tif");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximumImageSize_badArg() {
        final LociConversionImplementation convert = new LociConversionImplementation(
                source, "myfile.tif");
        convert.setMaximumImageSize(-100);
    }

    @Test
    public void testGetMaximumImageSize() {
        final LociConversionImplementation convert = new LociConversionImplementation(
                source, "myfile.tif");
        long current = convert.getMaximumImageSize();
        if (current != 0) {
            fail("getMaximumImageSize() returned " + current + ", expected 0.");
        }
        convert.setMaximumImageSize(1000);
        current = convert.getMaximumImageSize();
        if (current != 1000) {
            fail("getMaximumImageSize() returned " + current
                    + ", expected 1000.");
        }
    }
}
