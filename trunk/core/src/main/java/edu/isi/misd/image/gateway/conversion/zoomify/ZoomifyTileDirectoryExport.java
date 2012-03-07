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

package edu.isi.misd.image.gateway.conversion.zoomify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.tile.AbstractTileDirectoryExport;
import edu.isi.misd.image.gateway.conversion.tile.JpegTileWriterImplementation;
import edu.isi.misd.image.gateway.conversion.tile.TileWriter;
import edu.isi.misd.image.gateway.conversion.tile.ZoomLevelNotFoundException;

/**
 * Exports tiles and a directory format compatible with Zoomify viewers.
 * 
 * @author David Smith
 * 
 */
public class ZoomifyTileDirectoryExport extends AbstractTileDirectoryExport {

    private static final long DEFAULT_TILE_SIZE = 256;
    private static final double DEFAULT_ZOOM_SCALE = 2;

    private static final String TILE_METADATA_FILENAME = "ImageProperties.xml";
    private static final String VERSION = "1.8";

    /**
     * Default constructor.
     * 
     * @param source
     *            the source image to read
     */
    public ZoomifyTileDirectoryExport(final ImageSource source) {
        super(source, new ZoomifyTileNamingConvention());
    }

    @Override
    public long getTileSize() {
        return DEFAULT_TILE_SIZE;
    }

    @Override
    public double getZoomScale() {
        return DEFAULT_ZOOM_SCALE;
    }

    @Override
    protected TileWriter getTileWriter(final String destination) {
        if (destination == null || destination.length() == 0) {
            throw new IllegalArgumentException("Destination must be specified.");
        }
        return new JpegTileWriterImplementation(destination, namingConvention,
                zoomLevelConfiguration);
    }

    @Override
    protected void saveTileMetadata(final String destination) throws Exception {
        if (destination == null || destination.length() == 0) {
            throw new IllegalArgumentException("Destination must be specified.");
        }
        createTileMetaFile(destination);
    }

    private void createTileMetaFile(final String destination)
            throws IOException {
        final File metafile = new File(new StringBuffer(destination).append(
                File.separator + TILE_METADATA_FILENAME).toString());
        if (LOG.isInfoEnabled()) {
            LOG.info("Writing tile metafile " + metafile.getAbsolutePath());
        }
        final String contents = new StringBuffer("<IMAGE_PROPERTIES WIDTH=\"")
        .append(sourceImage.getWidth())
        .append("\" HEIGHT=\"")
        .append(sourceImage.getHeight())
        .append("\" NUMTILES=\"")
        .append(zoomLevelConfiguration.getTotalNumberOfTiles())
        .append("\" NUMIMAGES=\"1\" VERSION=\"" + VERSION
                + "\" TILESIZE=\"").append(getTileSize())
                .append("\" />").toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Contents: " + contents);
        }
        final FileWriter writer = new FileWriter(metafile);
        writer.write(contents);
        writer.close();
        if (LOG.isInfoEnabled()) {
            LOG.info("Metafile saved successfully.");
        }
    }

    @Override
    public String getThumbnailFilename(final String destination) {
        if (destination == null || destination.length() == 0) {
            throw new IllegalArgumentException(
                    "Destination directory must be specified.");
        }
        if (!new File(destination).isDirectory()) {
            throw new IllegalArgumentException("Destination " + destination
                    + " doesn't exist.");

        }
        String thumbFilename = null;
        try {
            thumbFilename = namingConvention.getTileFileName(0, 0, 0,
                    getTileWriter(destination).getExtension(),
                    zoomLevelConfiguration);
        } catch (ZoomLevelNotFoundException e) {
            LOG.error("Error retrieving the thumb filename.", e);
        }
        return thumbFilename;
    }

}
