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
import java.io.IOException;

/**
 * Interface that defines the operation of writing an image tile.
 * 
 * @author David Smith
 * 
 */
public interface TileWriter {

    /**
     * Writes a buffered image to a tile image
     * 
     * @param image
     *            the image to write
     * @param level
     *            the current zoom level
     * @param row
     *            the current row
     * @param column
     *            the current column
     * @return the name of the tile that was written
     */
    public String write(BufferedImage image, int level, long row, long column)
            throws IOException;

    /**
     * 
     * @return the file extension of the tiles to write
     */
    public String getExtension();

    /**
     * 
     * @return the tile naming convention used by this tile writer
     */
    public TileNamingConvention getTileNamingConvention();

    /**
     * 
     * @return the destination base directory of the tile files
     */
    public String getDestinationDirectory();

    /**
     * 
     * @return the zoom level configuration used by this tile writer
     */
    public ZoomLevelConfiguration getZoomLevelConfiguration();
}
