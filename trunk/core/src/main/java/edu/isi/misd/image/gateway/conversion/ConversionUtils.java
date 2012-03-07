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



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Utilities used for image conversions.
 * 
 * @author David Smith
 * 
 */
public class ConversionUtils {

    private static final Logger LOG = Logger.getLogger(ConversionUtils.class);

    /**
     * 
     * @param filename
     *            filename
     * @return the extension of the filename
     */
    public static String getExtension(final String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename must be specified.");
        }
        String extension = "";
        final int lastPeriod = filename.lastIndexOf(".");
        if (lastPeriod > 0) {
            extension = filename.substring(lastPeriod + 1, filename.length())
                    .toLowerCase();
        }
        return extension;
    }

    /**
     * Reads a file into a list of strings, one for each line
     * 
     * @param file
     *            the file to read
     * @return the contents
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readFile(String file)
            throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    public static List<String> readStream(InputStream stream)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    /**
     * 
     * @param file
     *            filename
     * @return the base filename (no extension) of the filename
     */
    public static String getBaseFilename(String file) {
        return new File(file).getName().replaceFirst(
                "\\." + getExtension(file) + "$", "");
    }

    /**
     * Replaces back slashes with forward slashes
     * 
     * @param str
     *            the string to convert
     * @return
     */
    public static String replaceBackSlashes(String str) {
        return str.replaceAll("\\\\", "/");
    }

    /**
     * Retrieves the filename in URL format.
     * @param str the filename
     * @return the filename in URL format.
     */
    public static String getFileURL(String str)
            throws MalformedURLException {
        return new File(str).toURI().toURL().toString();
    }

    /**
     * Creates a directory structure under a file if it doesn't exist
     * 
     * @param baseDir
     *            the base directory (don't create this one)
     * @param filePath
     *            the path to the file from the base directory
     * @return true if the directory was created successfully or already exists
     */
    public static boolean createDirectoryTree(String baseDir, String filePath) {

        String[] pieces = filePath.split("/");
        StringBuffer currentDir = new StringBuffer(baseDir);
        for (int i = 0; i < pieces.length - 1; i++) {
            currentDir.append(File.separator).append(pieces[i]);
            File dir = new File(currentDir.toString());
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    LOG.warn("Could not create directory " + currentDir);
                    return false;
                }
            }
        }
        return true;
    }
}
