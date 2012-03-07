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

/**
 * Interface for image conversion operations.
 * 
 * @author David Smith
 * 
 */
public interface ImageConversion {

    /**
     * Runs the conversion task.
     * 
     * @throws Exception
     *             if an error occurred during conversion
     */
    public void run() throws Exception;

    /**
     * Sets the maximum image size (in bytes) to support in conversion. Default
     * is no limit.
     * 
     * @param size
     *            size in bytes
     */
    public void setMaximumImageSize(long size);

    /**
     * 
     * @return the maximum image size allowed (in bytes), or 0 if there is no
     *         size limit.
     */
    public long getMaximumImageSize();

    /**
     * 
     * @return the filename of the image to convert
     */
    public String getSourceFilename();

    /**
     * 
     * @return the filename of the image that was converted.
     */
    public String getDestinationFilename();
}
