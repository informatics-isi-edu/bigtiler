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

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * Implementation of ZoomLevelConfiguration
 * 
 * @author smithd
 * 
 */
public class ZoomLevelConfigurationImplementation implements
ZoomLevelConfiguration {

    private static final Logger LOG = Logger
            .getLogger(ZoomLevelConfiguration.class);
    private final ArrayList<ZoomLevel> zoomLevels;
    private long numberOfTiles;

    private final double scale;

    /**
     * Default constructor.
     * 
     * @param width
     *            original width of the image
     * @param height
     *            original height of the image
     * @param tile_size
     *            size of a single side of the square tile
     * @param scale
     *            the "zoom out" scale to apply. To shrink each level by half,
     *            set this to "2".
     */
    public ZoomLevelConfigurationImplementation(final long width,
            final long height, final long tile_size, final double scale) {
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be > 0.");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Height must be > 0.");
        }
        if (tile_size <= 0) {
            throw new IllegalArgumentException("Tile size must be > 0.");
        }
        if (scale <= 1) {
            throw new IllegalArgumentException("Scale must be > 1.");
        }
        if (tile_size > width || tile_size > height) {
            throw new IllegalArgumentException(
                    "Tile size must be less than the height and width of the original image.");
        }

        this.scale = scale;

        zoomLevels = new ArrayList<ZoomLevel>();

        int level = 0;
        double x = width;
        double y = height;
        double currentScale = 1;

        ZoomLevel zoomLevel;
        numberOfTiles = 0;
        while (x > tile_size || y > tile_size) {
            x = width;
            y = height;
            x = x * currentScale;
            y = y * currentScale;

            zoomLevel = new ZoomLevel(level, x, y, (long) Math.ceil(y
                    / tile_size), (long) Math.ceil(x / tile_size));
            numberOfTiles += zoomLevel.getNumberOfTiles();

            LOG.info(zoomLevel);
            zoomLevels.add(zoomLevel);

            if (x > tile_size || y > tile_size) {
                currentScale = currentScale / scale;
            }

            level++;
        }
    }

    @Override
    public int getNumberOfZoomLevels() {
        return zoomLevels.size();
    }

    @Override
    public long getNumberOfRows(final int level)
            throws ZoomLevelNotFoundException {
        checkZoomLevelExists(level);
        return zoomLevels.get(level).getNumberOfRows();
    }

    @Override
    public long getNumberOfColumns(final int level)
            throws ZoomLevelNotFoundException {
        checkZoomLevelExists(level);
        return zoomLevels.get(level).getNumberOfColumns();
    }

    @Override
    public long getNumberOfTiles(final int level)
            throws ZoomLevelNotFoundException {
        checkZoomLevelExists(level);
        return zoomLevels.get(level).getNumberOfTiles();
    }

    @Override
    public long getTotalNumberOfTiles() {
        return numberOfTiles;
    }

    private void checkZoomLevelExists(final int level)
            throws ZoomLevelNotFoundException {
        if (level >= zoomLevels.size()) {
            throw new ZoomLevelNotFoundException("Zoom level " + level
                    + " doesn't exist.");
        }
    }

    @Override
    public double getScale() {
        return scale;
    }
}
