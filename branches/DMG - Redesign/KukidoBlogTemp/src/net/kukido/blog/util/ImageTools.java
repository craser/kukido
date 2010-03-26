package net.kukido.blog.util;

import java.util.*;
import java.io.*;
import java.net.*;
import java.text.*;
import javax.servlet.*;
import java.util.regex.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;

import org.apache.sanselan.common.*;
import org.apache.sanselan.formats.jpeg.*;
import org.apache.sanselan.formats.tiff.*;
import org.apache.sanselan.formats.tiff.debug.*;
import org.apache.sanselan.*;


/**********************************************************************
 * Inserted in support of stolen image thumbnail code.  Don't touch.
 */

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.IIOImage;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.spi.IIORegistry;
import com.sun.image.codec.jpeg.*;

/*
 * End of image thumbnail imports.  Seriously.  Don't touch.
 **********************************************************************/

public class ImageTools
{
    public void saveImageAndThumb(BufferedImage im, int maxWidth, File imageFile, File thumbFile)
	throws IOException
    {
	try
	{
            int[] oldDims = new int[] { im.getWidth(), im.getHeight() };
	    int[] dims = scaleToWidth(oldDims, maxWidth);
	    BufferedImage thumb = scaleImage(im, dims);
            saveImage(im, imageFile);
	    saveImage(thumb, thumbFile);
	}
	catch (Exception e)
	{
	    try { imageFile.delete(); } catch (Exception ignored) {}
	    try { thumbFile.delete(); } catch (Exception ignored) {}
	}
    }
    
    public void saveImage(BufferedImage im, File file)
        throws IOException
    {
        ImageIO.write(im, "jpg", file);
    }
	
    public BufferedImage createImage(byte[] bytes)
	throws java.io.IOException
    {
	InputStream fis = null;
	try
	{
	    fis = new ByteArrayInputStream(bytes); 
	    com.sun.image.codec.jpeg.JPEGImageDecoder decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(fis); 
	    return decoder.decodeAsBufferedImage();
	}
	finally
	{
	    try { fis.close(); } catch (Exception ignored) {}
	}
    }

    public BufferedImage scaleImage(BufferedImage im, int[] dims)
    {
	HashMap hints = new HashMap(2);
	hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

	BufferedImage scaled = new BufferedImage(dims[0], dims[1], BufferedImage.TYPE_INT_RGB);
	Graphics2D g2D = scaled.createGraphics();
	g2D.setRenderingHints(hints);
	g2D.drawImage(im, 0, 0, dims[0], dims[1], null);

	return scaled;
    }

    /**
     * THIS CODE IS BRAIN DAMAGED: DOES NOT HANDLE MIRRORING IMAGES!  ROTATION ONLY!
     */
    public BufferedImage fixOrientation(BufferedImage im, int orientation)
    {
        if (orientation == TiffConst3.ORIENTATION_VALUE_HORIZONTAL_NORMAL)
            return im;
        
        int[] oldDims = new int[] { im.getWidth(), im.getHeight() };
        int[] newDims = rotateDims(im, orientation);
        
        double[] center = getCenterCoords(oldDims);
        
        BufferedImage rotated = new BufferedImage(newDims[0], newDims[1], BufferedImage.TYPE_INT_RGB);
        Graphics2D rotGraph = rotated.createGraphics();
        //AffineTransform at = new AffineTransform();
        //at.setToTranslation((((double)newDims[0] - im.getWidth()))/2.0, (((double)newDims[1] - im.getHeight()))/2.0);
        rotGraph.translate((((double)newDims[0] - im.getWidth()))/2.0, (((double)newDims[1] - im.getHeight()))/2.0);
        switch (orientation) {
            case TiffConst3.ORIENTATION_VALUE_MIRROR_HORIZONTAL_AND_ROTATE_270_CW:
                rotGraph.rotate(Math.toRadians(270), center[0], center[1]);
                break;
            case TiffConst3.ORIENTATION_VALUE_MIRROR_HORIZONTAL_AND_ROTATE_90_CW: 
                rotGraph.rotate(Math.toRadians(90), center[0], center[1]);
                break;
            case TiffConst3.ORIENTATION_VALUE_ROTATE_180: 
                rotGraph.rotate(Math.toRadians(180), center[0], center[1]);
                break;
            case TiffConst3.ORIENTATION_VALUE_ROTATE_270_CW:
                rotGraph.rotate(Math.toRadians(270), center[0], center[1]);
                break;
            case TiffConst3.ORIENTATION_VALUE_ROTATE_90_CW:
                rotGraph.rotate(Math.toRadians(90), center[0], center[1]);
                break;
            default:
                break; // no action necessary if we're not rotating.
        }	
        
	rotGraph.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        rotGraph.drawImage(im, 0, 0, null);
        
        return rotated;
    }
    
    public double[] getCenterCoords(int[] dims)
    {
        return new double[] {
          ((double)dims[0]) / 2,
          ((double)dims[1]) / 2
        };
    }
    
    public int[] rotateDims(BufferedImage im, int orientation)
    {
        int[] dims = new int[] { im.getWidth(), im.getHeight() };
        int[] rot = new int[] {dims[1], dims[0]}; // Rotated dimentions
        switch (orientation) {
            case TiffConst3.ORIENTATION_VALUE_HORIZONTAL_NORMAL: 
                return dims;
            case TiffConst3.ORIENTATION_VALUE_MIRROR_HORIZONTAL: 
                return dims;
            case TiffConst3.ORIENTATION_VALUE_MIRROR_HORIZONTAL_AND_ROTATE_270_CW: 
                return rot;
            case TiffConst3.ORIENTATION_VALUE_MIRROR_HORIZONTAL_AND_ROTATE_90_CW: 
                return rot;
            case TiffConst3.ORIENTATION_VALUE_MIRROR_VERTICAL:
                return dims;
            case TiffConst3.ORIENTATION_VALUE_ROTATE_180: 
                return dims;
            case TiffConst3.ORIENTATION_VALUE_ROTATE_270_CW:
                return rot;
            case TiffConst3.ORIENTATION_VALUE_ROTATE_90_CW:
                return rot;
            default: // Can't happen
                return dims;
        }
    }
        
    public BufferedImage scaleToWidth(BufferedImage im, int newWidth)
    {
        int[] oldDims = new int[] { im.getWidth(), im.getHeight() };
        int[] newDims = scaleToWidth(oldDims, newWidth);
        return scaleImage(im, newDims);
    }    
    
    public BufferedImage scaleToMaxDim(BufferedImage im, int maxDim)
    {
        int[] oldDims = new int[] { im.getWidth(), im.getHeight() };
        int[] newDims = scaleToMaxDim(oldDims, maxDim);
        return scaleImage(im, newDims);
    }    
 
    public int[] scaleToWidth(int[] dims, int newWidth)
    {
	//determine what we need to divide by to get the longest side to be MAX_THUMBSIZE
	double divisor = (double)dims[0]/(double)newWidth;
	double w = (double)dims[0]/divisor;
	double h = (double)dims[1]/divisor;
	return new int[] {(int)w, (int)h};
    }
    
    public int[] scaleToMaxDim(int[] dims, int maxDim)
    {
        int[] newDims = dims;
        
        int max = Math.max(dims[0], dims[1]);
        if (max > maxDim)
        {        
            double divisor = (double)max/(double)maxDim;
            double w = (double)dims[0]/divisor;
            double h = (double)dims[1]/divisor;
            newDims = new int[] {(int)w, (int)h};
        }
        
        return newDims;
    }
    
    public Date getDateTaken(byte[] bytes)
        throws ImageReadException, IOException, ParseException
    {
        IImageMetadata metaData = null;
        try { metaData = Sanselan.getMetadata(bytes); }    
        catch (Error e) {
            throw new IOException("Error parsing metadata: " + e);
        }
        catch (Exception e) {
            throw new IOException("Exception parsing metadata: " + e);
        }
        
        if (metaData == null) {
            throw new IOException("Null image meta data.");
        }

        if (!(metaData instanceof JpegImageMetadata)) { // TODO: Need implementation for other image types
            throw new IllegalArgumentException("EXIF data only supported for JPEG images.");
        }
        JpegImageMetadata jpegData = (JpegImageMetadata)metaData;
        String dateTime = getTagValue(jpegData, TiffConstants.TIFF_TAG_DateTimeOriginal);
        String parsableDateTime = dateTime.replace('T', ' ');
        Date dateTaken = null;
        
        try { 
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-M-d H:mm:ss.SSSzz");
            //parsableDateTime = parsableDateTime.substring(0, parsableDateTime.lastIndexOf('-'));
            //parsableDateTime = parsableDateTime.substring(0, parsableDateTime.lastIndexOf('.'));
            //parsableDateTime += " PDT";
            dateTaken = fmt.parse(parsableDateTime); // + " PDT");
            System.out.println("Parsed date: " + dateTaken.toString());
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return dateTaken;
    }
    
    public int getOrientation(byte[] bytes)
        throws ImageReadException, IOException, ParseException
    {
        IImageMetadata metaData = null;
        try { metaData = Sanselan.getMetadata(bytes); }    
        catch (Error e) {
            throw new IOException("Error parsing metadata: " + e);
        }
        catch (Exception e) {
            throw new IOException("Exception parsing metadata: " + e);
        }
        
        if (metaData == null) {
            throw new IOException("Null image meta data.");
        }

        if (!(metaData instanceof JpegImageMetadata)) { // TODO: Need implementation for other image types
            throw new IllegalArgumentException("EXIF data only supported for JPEG images.");
        }
        JpegImageMetadata jpegData = (JpegImageMetadata)metaData;
        String orientation = getTagValue(jpegData, TiffConstants.TIFF_TAG_Orientation);
        
        return Integer.parseInt(orientation);
    }
    
    
    public void dumpDateStamps(byte[] bytes)
        throws ImageReadException, IOException
    {
        IImageMetadata metaData = Sanselan.getMetadata(bytes);
        if (metaData instanceof JpegImageMetadata) {
            JpegImageMetadata jpegData = (JpegImageMetadata)metaData;
            String dateTime = getTagValue(jpegData, TiffConstants.TIFF_TAG_DateTime);
            String dateTimeDigitized = getTagValue(jpegData, TiffConstants.TIFF_TAG_DateTimeDigitized);
            String dateTimeOriginal = getTagValue(jpegData, TiffConstants.TIFF_TAG_DateTimeOriginal);
        }
        
    }
    
    public String getTagValue(JpegImageMetadata jpegData, TagInfo tagInfo)
    {
        TiffField field = jpegData.findEXIFValue(tagInfo);
        return (field == null)
            ? null
            : field.getValueDescription();
    }
}
