/*
 * Copyright 2011 University of Southern California
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.isi.misd.image.gateway.conversion.loci;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import loci.common.services.DependencyException;
import loci.common.services.ServiceException;
import loci.common.services.ServiceFactory;
import loci.formats.FormatException;
import loci.formats.IFormatReader;
import loci.formats.ImageReader;
import loci.formats.gui.BufferedImageReader;
import loci.formats.meta.IMetadata;
import loci.formats.services.OMEXMLService;

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ImageFormatException;
import edu.isi.misd.image.gateway.conversion.ImageInformation;
import edu.isi.misd.image.gateway.conversion.ImageInformationFactory;
import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * Wrapper for a LOCI image reader
 * 
 * @author David Smith
 * 
 */
public class LociImageSource implements ImageSource {

    private static final Logger LOG = Logger.getLogger(ImageSource.class);

    private final BufferedImageReader reader;
    private final IMetadata omexml;
    private final String imageFilename;
    private int seriesNumber;
    private final int imageNumber;

    private static final TreeSet<String> supportedTypes = new TreeSet<String>();

    private final ImageInformation imageInformation;

    public static void registerTypes() {
        final ImageReader baseReader = new ImageReader();
        final IFormatReader[] readers = baseReader.getReaders();
        for (int i = 0; i < readers.length; i++) {
            final String[] extensions = readers[i].getSuffixes();
            for (int j = 0; j < extensions.length; j++) {
                supportedTypes.add(extensions[j]);
                ImageSourceFactory.register(extensions[j],
                        LociImageSource.class);
            }
        }
    }

    public static boolean isSupportedType(final String extension) {
        return supportedTypes.contains(extension);
    }

    public static Set<String> getSupportedTypes() {
        return supportedTypes;
    }

    /**
     * Constructor that defaults to the largest image series in the set.
     * 
     * @param filename
     *            name of the source image
     * @throws DependencyException
     * @throws ServiceException
     * @throws IOException
     * @throws FormatException
     */
    public LociImageSource(final String filename) throws DependencyException,
    ServiceException, IOException, FormatException {
        this(filename, 0, 0);

        // find the largest image
        BigInteger maxPixels = BigInteger.ZERO;
        int series = 0;

        if (LOG.isDebugEnabled()) {
            LOG.debug("No image series specified - locating largest image as default.");
        }
        synchronized (reader) {
            for (int i = 0; i < reader.getSeriesCount(); i++) {
                reader.setSeries(i);
                BigInteger pixels = new BigInteger(Integer.toString(reader
                        .getSizeX())).multiply(new BigInteger(Integer
                                .toString(reader.getSizeY())));
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Series " + i + "=" + pixels + " pixels");
                }
                if (pixels.compareTo(maxPixels) > 0) {
                    series = i;
                    maxPixels = pixels;
                }
            }
            this.seriesNumber = series;
            reader.setSeries(this.seriesNumber);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Series " + this.seriesNumber
                    + " is the largest image and will be used as the default.");
        }
    }

    public LociImageSource(final BufferedImageReader imageReader,
            final IMetadata metadata) {
        this(imageReader, metadata, 0);
    }

    public LociImageSource(final BufferedImageReader imageReader,
            final IMetadata metadata, final int image) {
        reader = imageReader;
        imageNumber = image;

        omexml = metadata;

        imageFilename = reader.getCurrentFile();
        seriesNumber = reader.getSeries();

        imageInformation = ImageInformationFactory
                .getImageInformation(imageFilename);
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
    public LociImageSource(final String filename, final int series,
            final int image) throws DependencyException, ServiceException,
            IOException, FormatException {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null.");
        }
        if (series < 0) {
            throw new IllegalArgumentException("Series cannot be < 0");
        }
        if (image < 0) {
            throw new IllegalArgumentException("Image cannot be < 0");
        }

        final File f = new File(filename);
        if (!f.exists() || !f.canRead()) {
            throw new IllegalArgumentException("Source file " + filename
                    + " doesn't exist or is not readable.");
        }

        imageFilename = filename;
        seriesNumber = series;
        imageNumber = image;

        final ServiceFactory factory = new ServiceFactory();
        final OMEXMLService service = factory.getInstance(OMEXMLService.class);
        omexml = service.createOMEXMLMetadata();

        reader = new BufferedImageReader();
        reader.setMetadataStore(omexml);
        reader.setId(imageFilename);
        reader.setSeries(seriesNumber);

        imageInformation = ImageInformationFactory
                .getImageInformation(imageFilename);
    }

    @Override
    public void setSeriesNumber(int series) {
        seriesNumber = series;
        reader.setSeries(series);
    }

    @Override
    public int getSeriesCount() {
        return reader.getSeriesCount();
    }

    @Override
    public Map<String, Object> getGlobalMetadata() {
        Map<String, Object> metadata = reader.getGlobalMetadata();
        Map<String, Integer> additionalIFDs = imageInformation.getIFDMap();
        Set<String> keys = additionalIFDs.keySet();
        for (String key : keys) {
            try {
                metadata.put(key,
                        imageInformation.getIFDValue(additionalIFDs.get(key)));
            }
            catch(IOException e) {
                LOG.warn("Could not retrieve IFD entry for " + key);
            }
        }

        return metadata;
    }

    @Override
    public Object getMetadataValue(final String key) {
        Object value = reader.getMetadataValue(key);
        if (value == null) {
            // try one of the additional IFD's
            try {
                Integer offset = imageInformation.getIFDOffset(key);
                if (offset != null) {
                    value = imageInformation.getIFDValue(offset);
                }
            }
            catch(IOException e) {
                LOG.warn("Could not retrieve IFD for " + key);
            }
        }
        return value;
    }

    @Override
    public Map<String, Object> getSeriesMetadata() {
        return reader.getSeriesMetadata();
    }

    @Override
    public List<String> getRelevantFiles() {
        final String[] files = reader.getSeriesUsedFiles();
        final ArrayList<String> list = new ArrayList<String>(files.length);
        for (int i = 0; i < files.length; i++) {
            list.add(files[i]);
        }
        if (imageInformation.hasAnnotationFile()) {
            list.add(imageInformation.getAnnotationFilename());
        }
        return list;
    }

    @Override
    public Object getSeriesMetadataValue(final String key) {
        return reader.getSeriesMetadataValue(key);
    }

    @Override
    public byte[] readBytes(final long x, final long y, final long width,
            final long height) throws IOException {
        byte[] bytes = null;
        try {
            bytes = reader.openBytes(imageNumber, (int) x, (int) y,
                    (int) width, (int) height);
        } catch (final FormatException e) {
            throw new IOException(e.getMessage());
        }
        return bytes;
    }

    @Override
    public BufferedImage readImage(final long x, final long y,
            final long width, final long height) throws IOException {
        BufferedImage image = null;
        if (LOG.isDebugEnabled()) {
            LOG.debug("Reading bytes from offset " + (int) x + "x" + (int) y
                    + " of dimensions " + width + "x" + height);
        }
        try {
            image = reader.openImage(imageNumber, (int) x, (int) y,
                    (int) width,
                    (int) height);
        } catch (final FormatException e) {
            throw new IOException(e.getMessage());
        }
        return image;
    }

    @Override
    public long getWidth() {
        return reader.getSizeX();
    }

    @Override
    public long getHeight() {
        return reader.getSizeY();
    }

    @Override
    public String getFilename() {
        return imageFilename;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public int getSeriesNumber() {
        return seriesNumber;
    }

    @Override
    public int getImageNumber() {
        return imageNumber;
    }

    @Override
    public BufferedImage getThumbnailImage()
            throws ImageFormatException {
        return getThumbnailImage(true, Collections.<Integer> emptyList());
    }

    protected BufferedImage getThumbnailImage(boolean useThumbnailSeries,
            Collection<Integer> excludeSeries)
                    throws ImageFormatException {
        BufferedImage thumbnail = null;
        int thumbnailSeries = -1;
        int currentSeries = this.seriesNumber;
        int thumbWidth = -1;
        int thumbHeight = -1;
        int pixelThumbnailSeries = -1;

        // find the thumbnail series
        synchronized (reader) {
            for (int series = 0; series < reader.getSeriesCount(); series++) {
                if (!excludeSeries.contains(series)) {
                    reader.setSeries(series);
                    if (useThumbnailSeries && reader.isThumbnailSeries()) {
                        // found an actual series that is a thumbnail
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Found thumbnail series " + series);
                        }
                        thumbnailSeries = series;
                        break;
                    } else {
                        // in case there are no "thumbnail" series, figure out
                        // which one is the smallest
                        final int currentWidth = reader.getSizeX();
                        final int currentHeight = reader.getSizeY();

                        if ((thumbWidth < 0 || currentWidth < thumbWidth)
                                && (thumbHeight < 0 || currentHeight < thumbHeight)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Selecting series " + series
                                        + " as the lowest resolution.");
                            }
                            thumbWidth = currentWidth;
                            thumbHeight = currentHeight;
                            pixelThumbnailSeries = series;
                        }
                    }
                }
            }
            if ((useThumbnailSeries && thumbnailSeries >= 0)
                    || pixelThumbnailSeries >= 0) {
                try {
                    if (useThumbnailSeries && thumbnailSeries >= 0) {
                        reader.setSeries(thumbnailSeries);
                        thumbnail = reader.openThumbImage(0);
                    } else {
                        reader.setSeries(pixelThumbnailSeries);
                        thumbnail = reader.openThumbImage(0);
                    }
                } catch (Exception e) {
                    LOG.error("Error reading thumb image.", e);
                    throw new ImageFormatException(e.getMessage());
                } finally {
                    reader.setSeries(currentSeries);
                }
            } else {
                throw new ImageFormatException(
                        "Could not find an acceptable thumbnail from image "
                                + this.getFilename());
            }
        }
        return thumbnail;
    }


    public static void main(String[] args) {
        if (args.length < 2) {
            System.err
            .println("Usage: LociImageSource <image filename> <series> [<image>]");
            System.exit(1);
        }

        LociImageSource source = null;
        try {
            if (args.length < 3) {
                source = new LociImageSource(args[0],
                        Integer.parseInt(args[1]), 0);
            } else {
                source = new LociImageSource(args[0],
                        Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            }
            System.out.println("Image " + args[0] + ", series="
                    + source.getSeriesNumber() + ", image="
                    + source.getImageNumber() + " loaded successfully.");
        } catch (Exception e) {
            LOG.error("Error loading the image " + args[0], e);
            System.exit(1);
        }
    }
}
