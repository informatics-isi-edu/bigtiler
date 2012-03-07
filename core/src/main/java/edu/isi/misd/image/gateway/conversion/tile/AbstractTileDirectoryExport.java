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

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ImageSource;

/**
 * Abstract implementation class for tile directory export operations.
 * Subclasses decide the exported file type, file naming convention, and tile
 * metadata.
 * 
 * @author David Smith
 * 
 */
public abstract class AbstractTileDirectoryExport implements TileExport {

    protected static final Logger LOG = Logger.getLogger(TileExport.class);

    protected final ImageSource sourceImage;
    protected final ZoomLevelConfiguration zoomLevelConfiguration;
    protected final TileNamingConvention namingConvention;

    /**
     * Constructor
     * 
     * @param source
     *            the image source to read from
     * @param convention
     *            the tile naming convention to use
     */
    public AbstractTileDirectoryExport(final ImageSource source,
            final TileNamingConvention convention) {
        if (source == null) {
            throw new IllegalArgumentException("ImageSource must be specified.");
        }
        if (convention == null) {
            throw new IllegalArgumentException(
                    "TileNamingConvention must be specified.");
        }
        sourceImage = source;
        namingConvention = convention;

        zoomLevelConfiguration = new ZoomLevelConfigurationImplementation(
                sourceImage.getWidth(), sourceImage.getHeight(), getTileSize(),
                getZoomScale());
    }

    /**
     * Constructs a new instance of the appropriate tile writer type for this
     * operation
     * 
     * @param destination
     *            the directory to write the tiles
     * @param zoomLevelConfiguration2
     * @return the tile writer
     */
    abstract protected TileWriter getTileWriter(String destination);

    /**
     * Called after the tiles have all been created to perform any metadata
     * operations.
     * 
     * @param destination
     *            the directory that the tile files were saved to
     * @throws Exception
     */
    abstract protected void saveTileMetadata(String destination)
            throws Exception;

    @Override
    public void export(final String destination) throws Exception {
        if (destination == null || destination.length() == 0) {
            throw new IllegalArgumentException("Destination must be specified.");
        }
        exportFullResolutionTiles(destination);
        for (int i = 1; i < zoomLevelConfiguration.getNumberOfZoomLevels(); i++) {
            exportZoomLevelTiles(destination, i);
        }
        saveTileMetadata(destination);
    }

    private void exportFullResolutionTiles(final String destination)
            throws Exception {
        long n = 1;
        long col_num, row_num, cols;
        String tileFilename;
        int reader_x, reader_y;
        BufferedImage portion;

        long readWidth, readHeight;
        long remainingWidth, remainingHeight;

        final long tileSize = getTileSize();
        final long imageWidth = sourceImage.getWidth();
        final long imageHeight = sourceImage.getHeight();

        col_num = 0;
        row_num = 0;

        reader_x = 0;
        reader_y = 0;
        cols = zoomLevelConfiguration.getNumberOfColumns(0) - 1;

        final long numberOfTiles = zoomLevelConfiguration.getNumberOfTiles(0);

        final TileWriter tileWriter = getTileWriter(destination);

        for (long j = 0; j < numberOfTiles; j++) {
            readWidth = tileSize;

            readHeight = tileSize;
            remainingWidth = imageWidth - reader_x;
            remainingHeight = imageHeight - reader_y;

            if (remainingWidth < tileSize) {
                readWidth = (int) remainingWidth;
            }
            if (remainingHeight < tileSize) {
                readHeight = (int) remainingHeight;
            }

            if (readWidth > 0 && readHeight > 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Writing full resolution tile " + n + " of "
                            + numberOfTiles);
                }
                portion = sourceImage.readImage(reader_x, reader_y, readWidth,
                        readHeight);
                tileFilename = tileWriter.write(portion, 0, row_num,
                        col_num);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Tile " + tileFilename + " was saved successfully.");
                }
            } else {
                throw new ImageReadRangeException(
                        "Calculated invalid read range: " + readWidth + "x"
                                + readHeight + " for column " + col_num
                                + " and row " + row_num + " in level " + 0);
            }

            if (col_num >= cols) {
                col_num = 0;
                reader_x = 0;
                row_num++;
                reader_y += readHeight;
            } else {
                col_num++;
                reader_x += readWidth;
            }
            n++;
        }
    }

    private void exportZoomLevelTiles(final String destination, final int level)
            throws Exception {
        long col_num, row_num, cols;
        String tileFilename;

        col_num = 0;
        row_num = 0;

        if (LOG.isDebugEnabled()) {
            LOG.debug("Exporting tiles for zoom level " + level + "...");
        }
        cols = zoomLevelConfiguration.getNumberOfColumns(level) - 1;

        final long numberOfTiles = zoomLevelConfiguration
                .getNumberOfTiles(level);
        final TileWriter tileWriter = getTileWriter(destination);
        BufferedImage tileImage;
        for (long j = 0; j < numberOfTiles; j++) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Writing zoom level " + level + " tile " + (j + 1)
                        + " of "
                        + numberOfTiles);
            }
            tileImage = TileUtils.createLowerZoomLevelTile(tileWriter, level,
                    row_num, col_num);
            tileFilename = tileWriter.write(tileImage, level, row_num,
                    col_num);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Tile " + tileFilename + " was saved successfully.");
            }
            if (col_num >= cols) {
                col_num = 0;
                row_num++;
            } else {
                col_num++;
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished exporting tiles for zoom level " + level);
        }
    }
}
