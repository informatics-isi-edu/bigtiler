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
package edu.isi.misd.image.gateway.conversion.svs;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import loci.formats.tiff.TiffParser;
import edu.isi.misd.image.gateway.conversion.AbstractImageInformation;
import edu.isi.misd.image.gateway.conversion.ConversionUtils;

/**
 * Responsible for retrieving image information for an SVS file
 * 
 * @author David Smith
 * 
 */
public class SvsImageInformation extends AbstractImageInformation {

    private final String imageFilename;
    private TiffParser parser;
    private static final TreeMap<String, Integer> ifdMap = new TreeMap<String, Integer>();

    /**
     * Default constructor
     * 
     * @param filename
     *            the file to retrieve information for
     */
    public SvsImageInformation(String filename) {
        if (filename == null || filename.length() == 0) {
            throw new IllegalArgumentException("Filename must be specified.");
        }
        imageFilename = filename;
    }

    @Override
    public String getAnnotationFilename() {
        return new File(imageFilename).getParent() + File.separator
                + ConversionUtils.getBaseFilename(imageFilename) + ".xml";
    }

    @Override
    public Integer getIFDOffset(String key) {
        return ifdMap.get(key);
    }

    @Override
    public Map<String, Integer> getIFDMap() {
        return ifdMap;
    }

    @Override
    public Object getIFDValue(int offset) throws IOException {
        if (parser == null) {
            parser = new TiffParser(imageFilename);
        }
        return parser.getFirstIFD().getIFDValue(offset);
    }
}
