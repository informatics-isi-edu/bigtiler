package edu.isi.misd.image.gateway.conversion.test;

import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;
import edu.isi.misd.image.gateway.conversion.UnsupportedConversionException;
import edu.isi.misd.image.gateway.conversion.loci.LociImageSource;

public class ImageSourceFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRegister_null1stArg() {
        ImageSourceFactory.register(null, LociImageSource.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegister_null2ndArg() {
        ImageSourceFactory.register("myfile.jpg", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetImageSource_nullFilename() throws Exception {
        ImageSourceFactory.getImageSource(null);
    }

    @Test(expected = UnsupportedConversionException.class)
    public void testGetImageSource_badFileExtension() throws Exception {
        ImageSourceFactory.getImageSource("myfile.badext");
    }
}
