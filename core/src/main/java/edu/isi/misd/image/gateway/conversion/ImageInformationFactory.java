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

import java.util.Map;
import java.util.TreeMap;

import edu.isi.misd.image.gateway.conversion.ndpi.NdpiImageInformation;
import edu.isi.misd.image.gateway.conversion.svs.SvsImageInformation;

/**
 * Factory class for retrieving ImageInformation
 * 
 * @author David Smith
 * 
 */
public class ImageInformationFactory {

    /**
     * 
     * @param filename
     *            name of the file to retrieve information for
     * @return the ImageInformation for this file
     */
    public static final ImageInformation getImageInformation(String filename) {
        if (filename == null || filename.length() == 0) {
            throw new IllegalArgumentException("Filename must be specified.");
        }
        final String extension = ConversionUtils.getExtension(filename);
        ImageInformation info = null;
        if (Constants.NDPI_FILE_EXTENSION.equals(extension)) {
            info = new NdpiImageInformation(filename);
        } else if (Constants.SVS_FILE_EXTENSION.equals(extension)) {
            info = new SvsImageInformation(filename);
        } else {
            info = new EmptyImageInformation();
        }
        return info;
    }

    /**
     * Default ImageInformation for classes without an ImageInformation
     * implementation
     * 
     * @author David Smith
     * 
     */
    private static final class EmptyImageInformation extends
    AbstractImageInformation {

        @Override
        public String getAnnotationFilename() {
            return null;
        }

        @Override
        public Map<String, Integer> getIFDMap() {
            return new TreeMap<String, Integer>();
        }

        @Override
        public Integer getIFDOffset(String key) {
            return null;
        }

        @Override
        public Object getIFDValue(int offset) {
            return null;
        }

    }
}
