package edu.isi.misd.image.gateway.conversion.loci.test;

import static org.junit.Assert.fail;
import loci.common.services.ServiceFactory;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;

import org.junit.Before;
import org.junit.Test;

import edu.isi.misd.image.gateway.conversion.loci.LociImageDestination;

public class LociImageDestinationTest {

    private IMetadata omexml = null;
    private static final String source = LociImageSourceTest.class.getResource(
            "/test.jpg").getPath();

    @Before
    public void setUp() throws Exception {
        final ServiceFactory factory = new ServiceFactory();
        final OMEXMLService service = factory.getInstance(OMEXMLService.class);
        omexml = service.createOMEXMLMetadata();
        final IFormatReader reader = new ImageReader();
        reader.setMetadataStore(omexml);
        reader.setId(source);
        reader.setSeries(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null1stArg() throws Exception {
        new LociImageDestination(null, 10, 10, omexml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null2ndArg() throws Exception {
        new LociImageDestination("mydest.tif", 0, 10, omexml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null3rdArg() throws Exception {
        new LociImageDestination("mydest.tif", 10, 0, omexml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_null4thArg() throws Exception {
        new LociImageDestination("mydest.tif", 10, 10, null);
    }

    @Test
    public void testGetFilename() throws Exception {
        final LociImageDestination dest = new LociImageDestination(
                "myfile.tif", 10, 10, omexml);
        final String filename = dest.getFilename();
        if (!"myfile.tif".equals(filename)) {
            fail("getFilename() returned " + filename
                    + ", expected myfile.tif.");
        }
    }
}
