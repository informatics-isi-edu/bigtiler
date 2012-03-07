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
 * Interface for exporting an image into a series of tiles
 * 
 * @author David Smith
 * 
 */
public interface TileExport {

    /**
     * Exports the tiles to a given destination
     * 
     * @param destination
     *            the destination to write the tiles to
     * @throws Exception
     *             if the tiles could not be written
     */
    public void export(String destination) throws Exception;

    /**
     * 
     * @return the size of the tile to export
     */
    public long getTileSize();

    /**
     * The scale of the zoom levels of the tile
     * 
     * @return the zoom scale to use between zoom levels
     */
    public double getZoomScale();

    /**
     * @param destination
     *            the destination directory that the export was written to
     * @return the exported file that can be used as a thumbnail
     */
    public String getThumbnailFilename(String destination);
}
