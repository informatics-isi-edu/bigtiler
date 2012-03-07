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
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import loci.formats.gui.AWTImageTools;

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.CompositeImage;

/**
 * Utility classes that involve tiling.
 * 
 * @author David Smith
 * 
 */
public final class TileUtils {

    private static final Logger LOG = Logger.getLogger(TileUtils.class);

    /**
     * Creates a new image for a lower zoom level by looking for higher
     * resolution images, combining them, then resizing them
     * 
     * @param tileWriter
     *            the tile writer that is being used
     * @param level
     *            the zoom level
     * @param row
     *            the row for the new tile
     * @param column
     *            the column for the new tile
     * @param scale
     *            the zoom scale that is being used
     * @return the image in its proper size and resolution
     * @throws IOException
     */
    public static BufferedImage createLowerZoomLevelTile(
            final TileWriter tileWriter, final int level, final long row,
            final long column) throws IOException {
        if (tileWriter == null) {
            throw new IllegalArgumentException("Tile writer cannot be null.");
        }

        if (level <= 0
                || level > tileWriter.getZoomLevelConfiguration()
                .getNumberOfZoomLevels() - 1) {
            throw new IllegalArgumentException(
                    "Requested zoom level must be between 0 and "
                            + tileWriter.getZoomLevelConfiguration()
                            .getNumberOfZoomLevels() + ", exclusive.");
        }

        int readWidth = 0;
        int readHeight = 0;

        // find the four corresponding tiles
        final File[] higherTiles = new File[4];
        try {
            higherTiles[0] = new File(
                    new StringBuffer(tileWriter.getDestinationDirectory())
                    .append(File.separator)
                    .append(tileWriter.getTileNamingConvention()
                            .getTileFileName(level - 1, row * 2,
                                    column * 2,
                                    tileWriter.getExtension(),
                                    tileWriter.getZoomLevelConfiguration()))
                                    .toString());
            higherTiles[1] = new File(new StringBuffer(
                    tileWriter.getDestinationDirectory())
            .append(File.separator)
            .append(tileWriter.getTileNamingConvention()
                    .getTileFileName(level - 1, row * 2,
                            column * 2 + 1, tileWriter.getExtension(),
                            tileWriter.getZoomLevelConfiguration()))
                            .toString());

            higherTiles[2] = new File(new StringBuffer(
                    tileWriter.getDestinationDirectory())
            .append(File.separator)
            .append(tileWriter.getTileNamingConvention()
                    .getTileFileName(level - 1, row * 2 + 1,
                            column * 2, tileWriter.getExtension(),
                            tileWriter.getZoomLevelConfiguration()))
                            .toString());
            higherTiles[3] = new File(new StringBuffer(
                    tileWriter.getDestinationDirectory())
            .append(File.separator)
            .append(tileWriter.getTileNamingConvention()
                    .getTileFileName(level - 1, row * 2 + 1,
                            column * 2 + 1, tileWriter.getExtension(),
                            tileWriter.getZoomLevelConfiguration()))
                            .toString());
        } catch (ZoomLevelNotFoundException e) {
            throw new IllegalArgumentException(
                    "Could not retrieve tile filenames of lower zoom level tiles.");
        }

        final List<List<BufferedImage>> imagesToAdd = new LinkedList<List<BufferedImage>>();
        BufferedImage bufferedImage = null;
        for (int r = 0; r < higherTiles.length; r += 2) {
            final List<BufferedImage> i_row = new LinkedList<BufferedImage>();
            int rowWidth = 0;
            int rowHeight = 0;
            for (int c = 0; c < 2; c++) {
                if (higherTiles[r + c].exists()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding higher resolution tile "
                                + higherTiles[r + c].getAbsolutePath());
                    }
                    bufferedImage = ImageIO.read(higherTiles[r + c]);
                    rowWidth += bufferedImage.getWidth();
                    if (bufferedImage.getHeight() > rowHeight) {
                        rowHeight = bufferedImage.getHeight();
                    }
                    i_row.add(bufferedImage);
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("Higher resolution tile "
                            + higherTiles[r + c].getAbsolutePath()
                            + " doesn't exist, not using.");
                }
            }
            if (rowWidth > readWidth) {
                readWidth = rowWidth;
            }

            readHeight += rowHeight;
            imagesToAdd.add(i_row);
        }

        final int widthToResize = (int) Math.ceil(readWidth
                / tileWriter.getZoomLevelConfiguration().getScale());
        final int heightToResize = (int) Math.ceil(readHeight
                / tileWriter.getZoomLevelConfiguration().getScale());

        return AWTImageTools.scale(glueImageMatrix(imagesToAdd).getImage(),
                widthToResize, heightToResize, false);
    }

    /**
     * Combines a matrix of images together into a single image.
     * 
     * @param imagesToAdd
     *            a List of Lists of buffered images that represents the grid of
     *            the layout
     * @return the combined images into a single image
     */
    public static CompositeImage glueImageMatrix(
            final List<List<BufferedImage>> imagesToAdd) {
        if (imagesToAdd == null || imagesToAdd.size() == 0) {
            throw new IllegalArgumentException(
                    "The images to add are not specified.");
        }
        final CompositeImage sourceImage = new CompositeImage();
        CompositeImage rowImage;
        for (final List<BufferedImage> r : imagesToAdd) {
            rowImage = new CompositeImage();

            for (final BufferedImage c : r) {
                rowImage.addImage(c, CompositeImage.HORIZONTAL);
            }
            if (rowImage.hasImage()) {
                sourceImage.addImage(rowImage.getImage(),
                        CompositeImage.VERTICAL);
            }
        }
        return sourceImage;
    }
}
