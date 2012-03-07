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

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.tile.TileNamingConvention;
import edu.isi.misd.image.gateway.conversion.tile.ZoomLevelConfiguration;
import edu.isi.misd.image.gateway.conversion.tile.ZoomLevelNotFoundException;

public class ZoomifyTileNamingConvention implements TileNamingConvention {

    private final Logger LOG = Logger
            .getLogger(ZoomifyTileNamingConvention.class);
    private static final String TILE_GROUP_DIR = "TileGroup";

    @Override
    public String getTileFileName(final int level, final long row,
            final long column, final String fileExtension,
            final ZoomLevelConfiguration zoomLevelConfig)
                    throws ZoomLevelNotFoundException {
        if (fileExtension == null || fileExtension.length() == 0) {
            throw new IllegalArgumentException(
                    "File extension must be specified.");
        }
        if (zoomLevelConfig == null) {
            throw new IllegalArgumentException(
                    "Zoom Level Configuration must be specified.");
        }
        // find out how many tiles are behind this tile
        long previousTiles = 0;
        for (int i = level + 1; i < zoomLevelConfig.getNumberOfZoomLevels(); i++) {
            previousTiles += zoomLevelConfig.getNumberOfTiles(i);
        }
        previousTiles += row * zoomLevelConfig.getNumberOfColumns(level)
                + column;
        long tileGroupNumber = previousTiles / 256;
        return new StringBuffer(TILE_GROUP_DIR).append(tileGroupNumber)
                .append("/")
                .append(zoomLevelConfig.getNumberOfZoomLevels() - level - 1)
                .append("-").append(column)
                .append("-").append(row).append(".").append(fileExtension)
                .toString();
    }

}
