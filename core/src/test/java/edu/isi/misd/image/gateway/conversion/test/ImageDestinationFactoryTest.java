package edu.isi.misd.image.gateway.conversion.test;

import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.ImageDestinationFactory;
import edu.isi.misd.image.gateway.conversion.UnsupportedConversionException;
import edu.isi.misd.image.gateway.conversion.loci.LociImageDestination;

public class ImageDestinationFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testRegister_null1stArg() {
        ImageDestinationFactory.register(null, LociImageDestination.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegister_null2ndArg() {
        ImageDestinationFactory.register("jpg", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetImageDestination_null1stArg() throws Exception {
        ImageDestinationFactory.getImageDestinationClass(null);
    }

    @Test(expected = UnsupportedConversionException.class)
    public void testGetImageDestination_badExt() throws Exception {
        ImageDestinationFactory.getImageDestinationClass("file.notext");
    }
}
