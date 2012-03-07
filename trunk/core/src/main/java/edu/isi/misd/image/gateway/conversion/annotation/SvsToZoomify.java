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
package edu.isi.misd.image.gateway.conversion.annotation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.QueryProcessor;
import net.sf.saxon.query.QueryResult;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;
import net.sf.saxon.xpath.XPathException;

import org.apache.log4j.Logger;

import edu.isi.misd.image.gateway.conversion.ConversionUtils;

public class SvsToZoomify implements AnnotationConversion {

    private static final Logger LOG = Logger.getLogger(SvsToZoomify.class);

    private String xmlFile;
    private final long imgWidth;
    private final long imgHeight;

    public SvsToZoomify(String file, long width, long height) {
        if (file!= null && file.length()>0) {
            this.xmlFile = file;
        } else throw new IllegalArgumentException("Incorrect file name");
        this.imgWidth = width;
        this.imgHeight = height;
    }

    @Override
    public String convertToFile() {
        String xmlZoomifyString = this.convertToString();

        // output to zoomify annotations file
        String outFile = "";
        Matcher m = Pattern.compile("(.*)(\\.[a-z]{3,4}$)").matcher(this.xmlFile);
        if(m.find()) {
            outFile = m.group(1) + "_zoomify" + m.group(2);
        }
        try {
            FileWriter fw = new FileWriter(new File(outFile));
            fw.write(xmlZoomifyString);
            fw.close();
        } catch(IOException ioe) {
            LOG.error("Error converting annotation to Zoomify XML file.", ioe);
        }

        return outFile;
    }


    @Override
    public String convertToString() {

        String currTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String xmlOutString = "";
        String encodedXmlFile = xmlFile;
        try {
            encodedXmlFile = ConversionUtils.getFileURL(xmlFile);
        }
        catch(MalformedURLException e) {
            LOG.error("Error converting the file URL.", e);
        }

        String xQueryString = "let $d := doc(\""
                + encodedXmlFile
                + "\") "
                +
                "return " +
                "<ZAS>" +
                "<LABELSETUP INITIALVISIBILITY=\"1\" MINSCALE=\"0\" MAXSCALE=\"0\"/>" +
                "<POI ID=\"0\" NAME=\"Auto converted annotations\" X=\"" + imgWidth/2 + "\" Y=\"" + imgHeight/2 + "\" ZOOM=\"0\" USER=\"all\" DATE=\"" + currTime + "\">" +
                "<NOTES><NOTE ID=\"0\" NAME=\"Auto converted annotations\" TEXT=\"Auto converted annotations\" USER=\"all\" DATE=\"" + currTime + "\"/>" +
                "</NOTES> <LABELS> { " +
                "for $r in $d//Region[@Type=\"2\"] " +
                "let $v1x := $r/Vertices/Vertex[1]/@X , " +
                "$v1y := $r/Vertices/Vertex[1]/@Y , " +
                "$v2x := $r/Vertices/Vertex[2]/@X , " +
                "$v2y := $r/Vertices/Vertex[2]/@Y " +
                "return " +
                "<LABEL INTERNALID=\"" + System.currentTimeMillis() + "\" id=\"{$r/@Id}\" NAME=\"{$r/@Text}\" MEDIATYPE=\"symbol\" MEDIA=\"circle\" " +
                "X=\"{($v1x + $v2x) div 2}\" Y=\"{($v1y + $v2y) div 2}\" ZOOM=\"{" + imgWidth + " div $r/@Length}\" XSCALE=\"100\" YSCALE=\"100\" " +
                " URL=\"#\" URLTARGET=\"_blank\" ROLLOVER=\"0\" CAPTION=\"{$r/@Text}\" " +
                "  TOOLTIP=\"\" USER=\"all\" DATE=\"" + currTime + "\" TEXTCOLOR=\"000000\" BACKCOLOR=\"ffffff\" LINECOLOR=\"ffff00\" " +
                " FILLCOLOR=\"ffffff\" TEXTVISIBLE=\"1\" BACKVISIBLE=\"1\" LINEVISIBLE=\"1\" FILLVISIBLE=\"1\" CAPTIONPOSITION=\"8\"/> " +
                "}{" +
                "for $r in $d//Region[@Type=\"1\"] " +
                "let $v1x := $r/Vertices/Vertex[1]/@X , " +
                "$v1y := $r/Vertices/Vertex[1]/@Y , " +
                "$v3x := $r/Vertices/Vertex[3]/@X , " +
                "$v3y := $r/Vertices/Vertex[3]/@Y " +
                "return " +
                "<LABEL INTERNALID=\"" + System.currentTimeMillis() + "\" id=\"{$r/@Id}\" NAME=\"{$r/@Text}\" MEDIATYPE=\"symbol\" MEDIA=\"square\" " +
                "X=\"{($v1x + $v3x) div 2}\" Y=\"{($v1y + $v3y) div 2}\" ZOOM=\"{" + imgWidth + " div $r/@Length}\" XSCALE=\"100\" YSCALE=\"100\" " +
                " URL=\"#\" URLTARGET=\"_blank\" ROLLOVER=\"0\" CAPTION=\"{$r/@Text}\" " +
                "  TOOLTIP=\"\" USER=\"all\" DATE=\"" + currTime + "\" TEXTCOLOR=\"000000\" BACKCOLOR=\"ffffff\" LINECOLOR=\"ffff00\" " +
                " FILLCOLOR=\"ffffff\" TEXTVISIBLE=\"1\" BACKVISIBLE=\"1\" LINEVISIBLE=\"1\" FILLVISIBLE=\"1\" CAPTIONPOSITION=\"8\"/> " +
                "}{" +
                "for $r in $d//Region[@Type=\"3\"] " +
                "let $v1x := $r/Vertices/Vertex[1]/@X , " +
                "$v1y := $r/Vertices/Vertex[1]/@Y , " +
                "$v2x := $r/Vertices/Vertex[2]/@X , " +
                "$v2y := $r/Vertices/Vertex[2]/@Y " +
                "return " +
                "<LABEL INTERNALID=\"" + System.currentTimeMillis() + "\" id=\"{$r/@Id}\" NAME=\"{$r/@Text}\" MEDIATYPE=\"symbol\" MEDIA=\"arrowUp\" " +
                "X=\"{($v1x)}\" Y=\"{($v1y + ($r/@Length div 6))}\" ZOOM=\"{" + imgWidth + " div $r/@Length}\" XSCALE=\"100\" YSCALE=\"100\" " +
                " URL=\"#\" URLTARGET=\"_blank\" ROLLOVER=\"0\" CAPTION=\"{$r/@Text}\" " +
                "  TOOLTIP=\"\" USER=\"all\" DATE=\"" + currTime + "\" TEXTCOLOR=\"000000\" BACKCOLOR=\"ffffff\" LINECOLOR=\"ffff00\" " +
                " FILLCOLOR=\"ffffff\" TEXTVISIBLE=\"1\" BACKVISIBLE=\"1\" LINEVISIBLE=\"1\" FILLVISIBLE=\"1\" CAPTIONPOSITION=\"8\"/> " +
                "}</LABELS> </POI> </ZAS>";

        Configuration cfg = new Configuration();
        cfg.setHostLanguage(Configuration.XQUERY);
        StaticQueryContext sqc = new StaticQueryContext();
        sqc.setConfiguration(cfg);
        DynamicQueryContext dqc = new DynamicQueryContext();
        Properties outputProps = new Properties();
        outputProps.setProperty(OutputKeys.INDENT, "yes");

        QueryProcessor qp = new QueryProcessor(cfg, sqc);
        try {

            XQueryExpression expr = qp.compileQuery(xQueryString);

            SequenceIterator results = expr.iterator(dqc);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            while (results.hasNext()) {
                Item item = results.next();
                QueryResult.serialize(((NodeInfo)item).getRoot(),
                        new StreamResult(outStream),
                        outputProps);
            }

            xmlOutString = outStream.toString();
        } catch(XPathException xe) {
            LOG.error("Error converting annotation to Zoomify XML.", xe);
        } catch(TransformerException te) {
            LOG.error("Error converting annotation to Zoomify XML.", te);
        }
        return xmlOutString;
    }

}
