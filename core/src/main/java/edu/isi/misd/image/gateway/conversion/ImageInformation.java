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

import java.io.IOException;
import java.util.Map;

/**
 * Interface for retrieving information about the image outside of the image
 * source's scope
 * 
 * @author David Smith
 * 
 */
public interface ImageInformation {

    /**
     * 
     * @return the name of the annotation file for this image, or null if
     *         annotation files aren't stored for this image type
     */
    public String getAnnotationFilename();

    /**
     * 
     * @return true if an annotation file exists for this image
     */
    public boolean hasAnnotationFile();

    /**
     * 
     * @return the map of key-names to IFD offsets that extend the image source
     */
    public Map<String, Integer> getIFDMap();

    /**
     * 
     * @param key
     *            metadata key
     * @return the offset of the IFD represented by this key, or null if none
     *         exists
     */
    public Integer getIFDOffset(String key);

    /**
     * 
     * @param offset
     *            the offset of the IFD
     * @return the value of the IFD in this offset
     * @throws IOException
     *             if the IFD cannot be read
     */
    public Object getIFDValue(int offset) throws IOException;
}
