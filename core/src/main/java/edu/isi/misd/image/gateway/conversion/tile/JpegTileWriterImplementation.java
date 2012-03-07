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

package edu.isi.misd.image.gateway.conversion.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.isi.misd.image.gateway.conversion.ConversionUtils;

/**
 * This class is responsible for writing a single JPEG tile, given the directory
 * to write the tile; the tile file naming convention that is necessary; and the
 * level, row, and column that this tile represents.
 * 
 * @author David Smith
 * 
 */
public class JpegTileWriterImplementation implements TileWriter {

    private final String tileDirectory;
    private final TileNamingConvention namingConvention;
    private final ZoomLevelConfiguration zoomLevelConfiguration;

    public static final String JPG = "jpg";

    /**
     * Constructor.
     * 
     * @param directory
     *            the directory to write the tile to
     * @param convention
     *            the naming convention to use for the tile filenames
     */
    public JpegTileWriterImplementation(final String directory,
            final TileNamingConvention convention,
            ZoomLevelConfiguration zoomLevelConfig) {
        if (directory == null || directory.length() == 0) {
            throw new IllegalArgumentException(
                    "Destination directory must be specified.");
        }
        if (convention == null) {
            throw new IllegalArgumentException(
                    "Tile naming convention must be specified.");
        }
        if (zoomLevelConfig == null) {
            throw new IllegalArgumentException(
                    "Zoom Level Config must be specified.");
        }

        final File d = new File(directory);
        if (!d.isDirectory() || !d.canWrite()) {
            throw new IllegalArgumentException("Directory " + directory
                    + " doesn't exist or is not writable.");
        }
        tileDirectory = directory;
        namingConvention = convention;
        zoomLevelConfiguration = zoomLevelConfig;
    }

    @Override
    public String write(final BufferedImage image, final int level,
            final long row, final long column) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null.");
        }

        String tileFilename = null;
        try {
            tileFilename = namingConvention.getTileFileName(level,
                    row, column, getExtension(), zoomLevelConfiguration);
        } catch (ZoomLevelNotFoundException e) {
            throw new IllegalArgumentException(
                    "Error retrieving the tile filename.", e);
        }

        if (!ConversionUtils.createDirectoryTree(tileDirectory, tileFilename)) {
            throw new IllegalArgumentException(
                    "Could not create directory tree for " + tileFilename);
        }
        final File tileFile = new File(new StringBuffer(tileDirectory)
        .append(File.separator).append(tileFilename).toString());
        ImageIO.write(image, JPG, tileFile);
        return tileFile.getAbsolutePath();
    }

    @Override
    public String getExtension() {
        return JPG;
    }

    @Override
    public TileNamingConvention getTileNamingConvention() {
        return namingConvention;
    }

    @Override
    public String getDestinationDirectory() {
        return tileDirectory;
    }

    @Override
    public ZoomLevelConfiguration getZoomLevelConfiguration() {
        return zoomLevelConfiguration;
    }
}
