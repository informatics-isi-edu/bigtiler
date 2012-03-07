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

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ImageSource;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;
import edu.isi.misd.image.gateway.conversion.UnsupportedConversionException;

/**
 * Convenience command class for retrieving global or series metadata from an
 * image source (all values or specific ones).
 * 
 * @author David Smith
 * 
 */
public class ReadImageMetadata {

    private static final String USAGE = "Usage: "
        + ReadImageMetadata.class.getName()
        + " [--series=<series>] <image file> [<metadata field 1> <metadata field 2> ...]";
    private static final Logger LOG = Logger
    .getLogger(ReadImageMetadata.class);

    private static final String SERIES_FLAG = "--series=";

    public static void main(final String[] args) {
        if (args.length < 1) {
            System.err.println(USAGE);
            System.exit(1);
        }

        int currentArg = 0;
        boolean endOfFlags = false;
        boolean useSeries = false;
        int series = -1;
        for (int i = 0; i < args.length && !endOfFlags; i++) {
            if (args[i].startsWith(SERIES_FLAG)) {
                useSeries = true;
                series = Integer
                .parseInt(args[i].replaceFirst(SERIES_FLAG, ""));
            } else {
                endOfFlags = true;
                break;
            }
            currentArg++;
        }

        if (args.length <= currentArg) {
            System.err.println(USAGE);
            System.exit(1);
        }

        final String imageFilename = args[currentArg++];
        final ArrayList<String> metadataKeys = new ArrayList<String>(
                args.length - currentArg);
        for (int i = currentArg; i < args.length; i++) {
            metadataKeys.add(args[i]);
        }

        try {
            final ImageSource source = ImageSourceFactory
            .getImageSource(imageFilename);
            Map<String, Object> metadata = null;
            if (!useSeries) {
                // read global metadata by default
                if (metadataKeys.size() == 0) {
                    // all metadata
                    metadata = source.getGlobalMetadata();
                } else {
                    // specific values
                    metadata = new TreeMap<String, Object>();
                    for (String key : metadataKeys) {
                        metadata.put(key, source.getMetadataValue(key));
                    }
                }
            } else {
                // read series data
                source.setSeriesNumber(series);
                if (metadataKeys.size() == 0) {
                    // all metadata
                    metadata = source.getSeriesMetadata();
                } else {
                    metadata = new TreeMap<String, Object>();
                    for (String key : metadataKeys) {
                        metadata.put(key, source.getSeriesMetadataValue(key));
                    }
                }
            }
            if (metadata != null) {
                Set<String> keys = metadata.keySet();
                for (String key : keys) {
                    System.out.println(key + "=" + metadata.get(key));
                }
            }
        } catch (final UnsupportedConversionException e) {
            LOG.error("Error retrieving metadata for image " + imageFilename
                    + ".",
                    e);
            System.exit(1);
        }
    }
}
