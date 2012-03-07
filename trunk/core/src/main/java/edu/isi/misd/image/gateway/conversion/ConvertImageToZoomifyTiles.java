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

package edu.isi.misd.image.gateway.conversion;

import java.io.File;

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.tile.TileExport;
import edu.isi.misd.image.gateway.conversion.zoomify.ZoomifyTileDirectoryExport;

/**
 * Class that converts a source image into a directory structures of tiles
 * compatible with Zoomify image viewers.
 * 
 * @author David Smith
 * 
 */
public class ConvertImageToZoomifyTiles implements ImageConversion {

    private static final Logger LOG = Logger
            .getLogger(ConvertImageToZoomifyTiles.class);

    private static final String USAGE = "Usage: "
            + ConvertImageToZoomifyTiles.class.getName()
            + " <source image> <zoomify tile directory>";
    private final ImageSource sourceImage;

    private final String destinationDirectory;

    public static void main(final String[] args) {
        if (args.length < 2) {
            System.err.println(USAGE);
            System.exit(1);
        }
        try {
            final ConvertImageToZoomifyTiles convert = new ConvertImageToZoomifyTiles(
                    args[0], args[1]);
            convert.run();
        } catch (final Exception e) {
            LOG.error("Error converting the image to Zoomify tiles.", e);
            System.exit(1);
        }
    }

    /**
     * Constructor.
     * 
     * @param source
     *            the source filename
     * @param destination
     *            the destination directory of the tiles
     * @throws UnsupportedConversionException
     *             if the source filetype cannot be read.
     */
    public ConvertImageToZoomifyTiles(final String source,
            final String destination) throws UnsupportedConversionException {
        if (source == null || source.length() == 0 || destination == null
                || destination.length() == 0) {
            throw new IllegalArgumentException(
                    "Source and destination must be specified.");
        }
        final File f = new File(source);
        if (!f.exists() || !f.canRead()) {
            throw new IllegalArgumentException("Source file " + source
                    + " doesn't exist or isn't readable.");
        }

        sourceImage = ImageSourceFactory.getImageSource(source);

        final File dir = new File(destination);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IllegalArgumentException(
                        "Could not create destination directory " + destination);
            } else if (LOG.isInfoEnabled()) {
                LOG.info("Created destination directory " + destination);

            }
        } else {
            // make sure we can write to this directory
            if (!dir.canWrite()) {
                throw new IllegalArgumentException("Destination directory "
                        + destination + " is not writable");
            }
        }
        destinationDirectory = destination;
    }

    /**
     * Default constructor.
     * 
     * @param source
     *            the image source
     * @param destination
     *            the destination directory
     */
    public ConvertImageToZoomifyTiles(final ImageSource source,
            final String destination) throws UnsupportedConversionException {
        sourceImage = source;
        final File dir = new File(destination);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                throw new IllegalArgumentException(
                        "Could not create destination directory " + destination);
            } else {
                LOG.info("Created destination directory " + destination);
            }
        }
        else {
            // make sure we can write to this directory
            if(!dir.canWrite()) {
                throw new IllegalArgumentException("Destination directory "
                        + destination + " is not writable");
            }
        }
        destinationDirectory = destination;
    }

    /**
     * Runs the conversion.
     * 
     * @throws Exception
     *             if the conversion failed.
     */
    @Override
    public void run() throws Exception {

        final TileExport export = new ZoomifyTileDirectoryExport(sourceImage);

        export.export(destinationDirectory);

        sourceImage.close();
    }

    /**
     * 
     * @return the source image filename
     */
    @Override
    public String getSourceFilename() {
        return sourceImage.getFilename();
    }

    /**
     * 
     * @return the destination directory name
     */
    @Override
    public String getDestinationFilename() {
        return destinationDirectory;
    }

    @Override
    public void setMaximumImageSize(final long size) {
        // TODO: Auto-generated method stub

    }

    @Override
    public long getMaximumImageSize() {
        // TODO Auto-generated method stub
        return 0;
    }
}
