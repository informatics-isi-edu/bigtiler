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
package edu.isi.misd.image.gateway.conversion.ndpi;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import loci.formats.tiff.TiffParser;
import edu.isi.misd.image.gateway.conversion.AbstractImageInformation;

/**
 * Responsible for retrieving information for an NDPI file.
 * 
 * @author David Smith
 * 
 */
public class NdpiImageInformation extends AbstractImageInformation {

    private final String imageFilename;
    private TiffParser parser = null;

    public static int IFD_PHYSICAL_X_CENTER = 65422;
    public static int IFD_PHYSICAL_Y_CENTER = 65423;

    private static final TreeMap<String, Integer> ifdMap = new TreeMap<String, Integer>();
    static {
        ifdMap.put("PhysicalXCenter", IFD_PHYSICAL_X_CENTER);
        ifdMap.put("PhysicalYCenter", IFD_PHYSICAL_Y_CENTER);
    }

    /**
     * Default constructor
     * 
     * @param file
     *            name of the file to retrieve information for
     */
    public NdpiImageInformation(String file) {
        if (file == null || file.length() == 0) {
            throw new IllegalArgumentException("File must be specified.");
        }
        imageFilename = file;
    }

    @Override
    public Map<String, Integer> getIFDMap() {
        return ifdMap;
    }

    @Override
    public String getAnnotationFilename() {
        return imageFilename + ".ndpa";
    }

    @Override
    public Object getIFDValue(int offset) throws IOException {
        if (parser == null) {
            parser = new TiffParser(imageFilename);
        }

        return parser.getFirstIFD().getIFDValue(offset);
    }

    @Override
    public Integer getIFDOffset(String key) {
        return ifdMap.get(key);
    }
}
