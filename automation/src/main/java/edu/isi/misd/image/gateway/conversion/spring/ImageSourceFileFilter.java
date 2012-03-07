package edu.isi.misd.image.gateway.conversion.spring;


import java.util.Set;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;

import edu.isi.misd.image.gateway.conversion.ConversionUtils;
import edu.isi.misd.image.gateway.conversion.ImageSourceFactory;

/**
 * File filter for determining supported image types
 * 
 * @author David Smith
 * 
 */
@SuppressWarnings("rawtypes")
public class ImageSourceFileFilter implements GenericFileFilter {

    private Set<String> exclude;

    /**
     * Sets the file types to explicitly exclude
     * 
     * @param exclude
     */
    public void setExclude(Set<String> exclude) {
        this.exclude = exclude;
    }

    /**
     * 
     * @return the file types to explicitly exclude
     */
    public Set<String> getExclude() {
        return this.exclude;
    }

    @Override
    public boolean accept(GenericFile arg0) {
        final String ext = ConversionUtils.getExtension(arg0.getFileName());
        return !exclude.contains(ext)
                && ImageSourceFactory.isSupportedType(ext);
    }
}
