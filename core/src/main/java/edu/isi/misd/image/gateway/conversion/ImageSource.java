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

package edu.isi.misd.image.gateway.conversion;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Common interface for any implementation that can be considered a source for
 * reading an image.
 * 
 * @author David Smith
 * 
 */
public interface ImageSource {

    /**
     * Reads a specific section of bytes from an image
     * 
     * @param x
     *            the x-coordinate to start
     * @param y
     *            the y-coordinate to start
     * @param width
     *            the width of the read
     * @param height
     *            the height of the read
     * @return the bytes from the image
     */
    public byte[] readBytes(long x, long y, long width, long height)
    throws IOException;

    /**
     * Reads a specific section of bytes from an image and converts it to a
     * buffered image
     * 
     * @param x
     *            the x-coordinate to start
     * @param y
     *            the y-coordinate to start
     * @param width
     *            the width of the read
     * @param height
     *            the height of the read
     * @return a buffed image of the bytes read
     * @throws IOException
     */
    public BufferedImage readImage(long x, long y, long width, long height)
    throws IOException;

    /**
     * 
     * @return the width of the image in pixels
     */
    public long getWidth();

    /**
     * 
     * @return the height of the image in pixels
     */
    public long getHeight();

    /**
     * 
     * @return the filename of the image
     */
    public String getFilename();

    /**
     * 
     * @return the series number of the image to read
     */
    public int getSeriesNumber();

    /**
     * 
     * @return the image number of the image to read
     */
    public int getImageNumber();

    /**
     * Closes the source.
     */
    public void close() throws IOException;

    /**
     * 
     * @return a map of all the global metadata of the image source
     */
    public Map<String, Object> getGlobalMetadata();

    /**
     * 
     * @return a map of all the series metadata of the image source
     */
    public Map<String, Object> getSeriesMetadata();

    /**
     * 
     * @param key
     *            the metadata entry to lookup
     * @return the value of the metadata
     */
    public Object getMetadataValue(String key);

    /**
     * 
     * @param key
     *            the metadata entry to lookup
     * @return the value of the metadata
     */
    public Object getSeriesMetadataValue(String key);

    /**
     * Sets the current series of the image to use
     * 
     * @param seriesNumber
     *            the series number
     */
    public void setSeriesNumber(int seriesNumber);

    /**
     * 
     * @return the total number of series in the image
     */
    public int getSeriesCount();

    /**
     * 
     * @return a list of all of the relevant files for this image
     */
    public List<String> getRelevantFiles();

    /**
     * Retrieves a thumbnail image
     * 
     * @return the thumbnail image
     * @throws ImageFormatException
     *             if the image thumbnail cannot be retrieved
     */
    public BufferedImage getThumbnailImage()
    throws ImageFormatException;
}
