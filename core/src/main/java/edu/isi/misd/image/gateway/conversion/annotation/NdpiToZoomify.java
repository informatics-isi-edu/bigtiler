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

public class NdpiToZoomify implements AnnotationConversion {

    private static final Logger LOG = Logger.getLogger(NdpiToZoomify.class);

    public static final double SCALING_FACTOR = 0.004333;
    public static final int ZOOM_FACTOR = 15;

    private String xmlFile = null;
    private long pixelWidth = 0L;
    private long pixelHeight = 0L;
    private long nanoWidth = 0L;
    private long nanoHeight = 0L;
    private long centerX = 0L;
    private long centerY = 0L;
    private long lengthNegX = 0L;
    private long lengthNegY = 0L;

    public NdpiToZoomify(String file, long width, long height, long centerX, long centerY) {
        if (file!= null && file.length()>0) {
            this.xmlFile = file;
        } else throw new IllegalArgumentException("Incorrect file name");
        this.pixelWidth = width;
        this.pixelHeight = height;
        this.centerX = centerX;
        this.centerY = centerY;
        this.nanoWidth = ((Double)(this.pixelWidth / NdpiToZoomify.SCALING_FACTOR)).longValue();
        this.nanoHeight = ((Double)(this.pixelHeight / NdpiToZoomify.SCALING_FACTOR)).longValue();
        this.lengthNegX = (this.nanoWidth / 2) - centerX;
        this.lengthNegY = (this.nanoHeight / 2) - centerY;
    }

    @Override
    public String convertToFile() {

        String xmlZoomifyString = this.convertToString();
        // output to zoomify annotations file
        String outFile = "";
        Matcher m = Pattern.compile("(.*)(\\.[a-z]{3,4}$)").matcher(xmlFile);
        if(m.find()) {
            outFile = m.group(1) + "_zoomify" + m.group(2);
        }

        try {
            FileWriter fw = new FileWriter(new File(outFile));
            fw.write(xmlZoomifyString);
            fw.close();
        } catch(IOException ioe) {
            LOG.error("Error converting the Zoomify XML annotation.", ioe);
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
                "<POI ID=\"0\" NAME=\"Auto converted annotations\" X=\"" + pixelWidth/2 + "\" Y=\"" + pixelHeight/2 + "\" ZOOM=\"0\" USER=\"all\" DATE=\"" + currTime + "\">" +
                "<NOTES><NOTE ID=\"0\" NAME=\"Auto converted annotations\" TEXT=\"Auto converted annotations\" USER=\"all\" DATE=\"" + currTime + "\"/>" +
                "</NOTES> <LABELS> { " +
                "for $r in $d//ndpviewstate " +
                "let $title := $r/title, $details := $r/details, $lens := $r/lens, $id := $r/@id " +
                "return if ($r/annotation[@type=\"circle\"]) " +
                "then " +
                "<LABEL INTERNALID=\"" + System.currentTimeMillis() + "\" id=\"{$id}\" NAME=\"{$title}\" MEDIATYPE=\"symbol\" MEDIA=\"circle\" " +
                "X=\"{($r/annotation/x + " + lengthNegX + ") * " + NdpiToZoomify.SCALING_FACTOR + "}\" " +
                "Y=\"{($r/annotation/y + " + lengthNegY + ") * " + NdpiToZoomify.SCALING_FACTOR + "}\" ZOOM=\"{$lens * " + NdpiToZoomify.ZOOM_FACTOR + "}\" XSCALE=\"100\" YSCALE=\"100\" " +
                " URL=\"#\" URLTARGET=\"_blank\" ROLLOVER=\"0\" CAPTION=\"{$details}\" " +
                " TOOLTIP=\"\" USER=\"all\" DATE=\"" + currTime + "\" TEXTCOLOR=\"000000\" BACKCOLOR=\"ffffff\" LINECOLOR=\"ffff00\" " +
                " FILLCOLOR=\"ffffff\" TEXTVISIBLE=\"1\" BACKVISIBLE=\"1\" LINEVISIBLE=\"1\" FILLVISIBLE=\"1\" CAPTIONPOSITION=\"8\"/> " +
                "else ''" +
                "}{ " +
                "for $r in $d//ndpviewstate " +
                "let $title := $r/title, $details := $r/details, $lens := $r/lens, $id := $r/@id " +
                "return if ($r/annotation[@type=\"pointer\"]) " +
                "then " +
                "<LABEL INTERNALID=\"" + System.currentTimeMillis() + "\" id=\"{$id}\" NAME=\"{$title}\" MEDIATYPE=\"symbol\" MEDIA=\"arrowUp\" " +
                "X=\"{($r/annotation/x1 + " + lengthNegX + ") * " + NdpiToZoomify.SCALING_FACTOR + "}\" " +
                "Y=\"{($r/annotation/y1 + " + lengthNegY + ") * " + NdpiToZoomify.SCALING_FACTOR + "}\" ZOOM=\"{$lens * " + NdpiToZoomify.ZOOM_FACTOR + "}\" XSCALE=\"100\" YSCALE=\"100\" " +
                " URL=\"#\" URLTARGET=\"_blank\" ROLLOVER=\"0\" CAPTION=\"{$details}\" " +
                " TOOLTIP=\"\" USER=\"all\" DATE=\"" + currTime + "\" TEXTCOLOR=\"000000\" BACKCOLOR=\"ffffff\" LINECOLOR=\"ffff00\" " +
                " FILLCOLOR=\"ffffff\" TEXTVISIBLE=\"1\" BACKVISIBLE=\"1\" LINEVISIBLE=\"1\" FILLVISIBLE=\"1\" CAPTIONPOSITION=\"8\"/> " +
                "else ''" +
                "}{ " +
                "for $r in $d//ndpviewstate " +
                "let $title := $r/title, $details := $r/details, $lens := $r/lens, $id := $r/@id " +
                "return if ($r/annotation/specialtype=\"rectangle\") " +
                "then " +
                "<LABEL INTERNALID=\"" + System.currentTimeMillis() + "\" id=\"{$id}\" NAME=\"{$title}\" MEDIATYPE=\"symbol\" MEDIA=\"square\" " +
                "X=\"{((($r/annotation/pointlist/point[1]/x + $r/annotation/pointlist/point[3]/x) div 2) + " + lengthNegX + ") * " + NdpiToZoomify.SCALING_FACTOR + "}\" " +
                "Y=\"{((($r/annotation/pointlist/point[1]/y + $r/annotation/pointlist/point[3]/y) div 2) + " + lengthNegY + ") * " + NdpiToZoomify.SCALING_FACTOR + "}\" " +
                "ZOOM=\"{$lens * " + NdpiToZoomify.ZOOM_FACTOR + "}\" XSCALE=\"100\" YSCALE=\"100\" " +
                " URL=\"#\" URLTARGET=\"_blank\" ROLLOVER=\"0\" CAPTION=\"{$details}\" " +
                " TOOLTIP=\"\" USER=\"all\" DATE=\"" + currTime + "\" TEXTCOLOR=\"000000\" BACKCOLOR=\"ffffff\" LINECOLOR=\"ffff00\" " +
                " FILLCOLOR=\"ffffff\" TEXTVISIBLE=\"1\" BACKVISIBLE=\"1\" LINEVISIBLE=\"1\" FILLVISIBLE=\"1\" CAPTIONPOSITION=\"8\"/> " +
                "else ''" +
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
