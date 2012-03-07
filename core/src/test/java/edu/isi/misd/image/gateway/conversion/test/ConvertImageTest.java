package edu.isi.misd.image.gateway.conversion.test;

import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.ConvertImage;

public class ConvertImageTest {

    private final String source = ConvertImageTest.class.getResource(
            "/test.jpg").getPath();
    private final String destination = "mydestination.tiff";

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_null1stArg() throws Exception {
        ConvertImage.convert(null, destination);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_null2ndArg() throws Exception {
        ConvertImage.convert(source, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nonExistantSource() throws Exception {
        ConvertImage.convert("notafile.jpg", destination);
    }
}
