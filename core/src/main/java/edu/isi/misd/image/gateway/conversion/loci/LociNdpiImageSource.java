package edu.isi.misd.image.gateway.conversion.loci;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.formats.FormatException;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;
import edu.isi.misd.image.gateway.conversion.ImageFormatException;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Class for handling NDPI files using the LOCI libraries.
 * 
 * @author David Smith
 * 
 */
public class LociNdpiImageSource extends LociImageSource {

    private static final TreeSet<String> supportedTypes = new TreeSet<String>();

    public static void registerTypes() {
        supportedTypes.add("ndpi");
        ImageSourceFactory.register("ndpi", LociNdpiImageSource.class);
    }

    public static boolean isSupportedType(final String extension) {
        return supportedTypes.contains(extension);
    }

    public static Set<String> getSupportedTypes() {
        return supportedTypes;
    }

    /**
     * Constructor.
     * 
     * @param filename
     *            name of the source image
     * @throws DependencyException
     * @throws ServiceException
     * @throws IOException
     * @throws FormatException
     */
    public LociNdpiImageSource(final String filename)
            throws DependencyException, ServiceException, IOException,
            FormatException {
        super(filename);
    }

    public LociNdpiImageSource(final BufferedImageReader imageReader,
            final IMetadata metadata) {
        super(imageReader, metadata);
    }

    public LociNdpiImageSource(final BufferedImageReader imageReader,
            final IMetadata metadata, final int image) {
        super(imageReader, metadata, image);
    }

    /**
     * Constructor.
     * 
     * @param filename
     *            name of the source image
     * @param series
     *            the series number of the image to read
     * @param image
     *            the image number of the image to read
     * @throws DependencyException
     * @throws ServiceException
     * @throws IOException
     * @throws FormatException
     */
    public LociNdpiImageSource(final String filename, final int series,
            final int image) throws DependencyException, ServiceException,
            IOException, FormatException {
        super(filename, series, image);
    }

    @Override
    public BufferedImage getThumbnailImage() throws ImageFormatException {
        // don't use thumbnail bytes and exclude the last two series (whole
        // slide and label)
        return getThumbnailImage(false,
                Arrays.asList(getSeriesCount() - 1, getSeriesCount() - 2));
    }

}
