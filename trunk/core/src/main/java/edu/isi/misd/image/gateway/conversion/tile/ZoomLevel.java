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

/**
 * Container class for each zoom configuration
 * 
 * @author David Smith
 * 
 */
public class ZoomLevel {

    private final double image_width;
    private final double image_height;
    private final int zoom_level;
    private final long number_of_rows;
    private final long number_of_cols;
    private final long number_of_tiles;

    /**
     * Default constructor
     * 
     * @param level
     *            the zoom level number
     * @param w
     *            width of the image to convert (pixels)
     * @param h
     *            height of the image to convert (pixels)
     * @param rows
     *            number of rows of tiles
     * @param cols
     *            number of columns of tiles
     */
    public ZoomLevel(final int level, final double w, final double h,
            final long rows, final long cols) {
        zoom_level = level;
        image_width = w;
        image_height = h;
        number_of_rows = rows;
        number_of_cols = cols;
        number_of_tiles = number_of_rows * number_of_cols;
    }

    /**
     * 
     * @return the number of tiles to include in this zoom level
     */
    public long getNumberOfTiles() {
        return number_of_tiles;
    }

    /**
     * 
     * @return the number of tile rows
     */
    public long getNumberOfRows() {
        return number_of_rows;
    }

    /**
     * 
     * @return the number of tile columns
     */
    public long getNumberOfColumns() {
        return number_of_cols;
    }

    @Override
    public String toString() {
        return new StringBuffer("Level ").append(zoom_level)
                .append(": rows=").append(number_of_rows).append(" cols=")
                .append(number_of_cols).append(" total=")
                .append(getNumberOfTiles()).append(" width=")
                .append(image_width).append(" height=")
                .append(image_height).toString();
    }
}
