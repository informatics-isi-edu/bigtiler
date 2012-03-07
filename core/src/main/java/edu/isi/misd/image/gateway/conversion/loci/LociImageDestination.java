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

import java.io.IOException;
import java.util.Set;
import java.util.TreeMap;

import loci.formats.FormatException;
import loci.formats.IFormatWriter;
import loci.formats.ImageWriter;
import loci.formats.meta.IMetadata;
import ome.xml.model.primitives.PositiveInteger;
import edu.isi.misd.image.gateway.conversion.ImageDestination;
import edu.isi.misd.image.gateway.conversion.ImageDestinationFactory;

/**
 * Convenience class for converting one image format to another using
 * Bio-Formats
 * 
 * @author David Smith
 * 
 */
public class LociImageDestination implements ImageDestination {

    private final IMetadata omexml;
    private final String destinationFile;
    private final IFormatWriter writer;

    private final int seriesNumber;
    private final int imageNumber;

    private static final TreeMap<String, String> supportedTypes = new TreeMap<String, String>();

    /**
     * Registers the types that LOCI Bio-Formats can write to
     */
    public static void registerTypes() {
        final ImageWriter baseWriter = new ImageWriter();
        final IFormatWriter[] writers = baseWriter.getWriters();
        for (int i = 0; i < writers.length; i++) {
            final String[] extensions = writers[i].getSuffixes();
            for (int j = 0; j < extensions.length; j++) {
                supportedTypes.put(extensions[j], null);
                ImageDestinationFactory.register(extensions[j],
                        LociImageDestination.class);
            }
        }
    }

    /**
     * 
     * @param extension
     *            the file extension
     * @return true if LOCI can be used to export this file type
     */
    public static boolean isSupportedType(final String extension) {
        return supportedTypes.containsKey(extension);
    }

    /**
     * 
     * @return a set of file types that can be exported
     */
    public static Set<String> getSupportedTypes() {
        return supportedTypes.keySet();
    }

    /**
     * Constructor
     * 
     * @param destinationFilename
     * @param width
     * @param height
     * @param metadata
     * @throws IOException
     * @throws FormatException
     */
    public LociImageDestination(final String destinationFilename,
            final int width, final int height, final IMetadata metadata)
    throws IOException, FormatException {
        if (destinationFilename == null || destinationFilename.length() == 0) {
            throw new IllegalArgumentException(
            "Destination filename must be specified.");
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
            "Width and height must be greater than 0.");
        }

        if (metadata == null) {
            throw new IllegalArgumentException("Metadata must be specified.");
        }

        omexml = metadata;
        destinationFile = destinationFilename;

        seriesNumber = 0;
        imageNumber = 0;

        omexml.setPixelsSizeX(new PositiveInteger(width), imageNumber);
        omexml.setPixelsSizeY(new PositiveInteger(height), imageNumber);

        writer = new ImageWriter();

        writer.setMetadataRetrieve(omexml);
        writer.setId(destinationFile);
        writer.setSeries(seriesNumber);
    }

    @Override
    public void writeBytes(final byte[] bytes) throws Exception {
        writer.saveBytes(imageNumber, bytes);
    }

    @Override
    public void writeBytes(final byte[] bytes, final long x, final long y,
            final long width, final long height) throws Exception {
        writer.saveBytes(imageNumber, bytes, (int) x, (int) y, (int) width,
                (int) height);
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }

    @Override
    public String getFilename() {
        return destinationFile;
    }
}
