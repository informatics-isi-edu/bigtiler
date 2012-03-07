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
 * Interface that defines the zoom level configuration of a tiling task. This
 * class knows the number of zoom levels to create, as well as the number of
 * tiles, rows, and columns for each level.
 * 
 * @author David Smith
 * 
 */
public interface ZoomLevelConfiguration {

    /**
     * 
     * @return the total number of zoom levels to create, including the
     *         full-resolution level
     */
    public int getNumberOfZoomLevels();

    /**
     * 
     * @param level
     *            the specified zoom level
     * @return the number of tile rows at this zoom level
     * @throws ZoomLevelNotFoundException
     *             if the zoom level doesn't exist
     */
    public long getNumberOfRows(int level) throws ZoomLevelNotFoundException;

    /**
     * 
     * @param level
     *            the specified zoom level
     * @return the number of tile columns at this zoom level
     * @throws ZoomLevelNotFoundException
     *             if the zoom level doesn't exist
     */
    public long getNumberOfColumns(int level) throws ZoomLevelNotFoundException;

    /**
     * 
     * @param level
     *            the specified zoom level
     * @return the number of tiles at this zoom level
     * @throws ZoomLevelNotFoundException
     *             if the zoom level doesn't exist
     */
    public long getNumberOfTiles(int level) throws ZoomLevelNotFoundException;

    /**
     * 
     * @return the total number of tiles, summed across all zoom levels
     */
    public long getTotalNumberOfTiles();

    /**
     * 
     * @return the scale used between resolutions
     */
    public double getScale();
}
