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
 * Thrown when trying to access a zoom level in ZoomLevelConfiguration that
 * doesn't exist
 * 
 * @author David Smith
 * 
 */
public class ZoomLevelNotFoundException extends Exception {

    /**
     * Default constructor.
     * 
     * @param message
     *            the message to pass
     */
    public ZoomLevelNotFoundException(final String message) {
        super(message);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

}
