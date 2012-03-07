package edu.isi.misd.image.gateway.conversion.test;

import static org.junit.Assert.fail;

import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.ConversionUtils;

public class ConversionUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetExtension_nullArg() {
        ConversionUtils.getExtension(null);
    }

    @Test
    public void testGetExtension() {
        String filename = "2010-02-05 12.13.43.jpg";
        String extension = ConversionUtils.getExtension(filename);
        if (!extension.equals("jpg")) {
            fail("getExtension() returned " + extension + ", expected jpg.");
        }

        filename = "filenoextension";
        extension = ConversionUtils.getExtension(filename);
        if (extension.length() != 0) {
            fail("getExtension() returned " + extension + ", expected nothing.");
        }
    }
}
