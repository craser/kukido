package net.kukido.blog.datamodel;

import java.io.*;
import java.util.*;

public class Thumbnail implements Serializable
{
    private int attachmentId;
    private String fileName;
    private int maxDimension;
    private byte[] bytes;

    public void setAttachmentId(int attachmentId)
    {
	this.attachmentId = attachmentId;
    }

    public int getAttachmentId()
    {
	return attachmentId;
    }

    public void setFileName(String fileName)
    {
	this.fileName = fileName;
    }

    public String getFileName()
    {
	return fileName;
    }

    public void setMaxDimension(int maxDimension)
    {
	this.maxDimension = maxDimension;
    }

    public int getMaxDimension()
    {
	return maxDimension;
    }

    public void setBytes(byte[] bytes)
    {
	this.bytes = bytes;
    }

    public byte[] getBytes()
    {
	return bytes;
    }
}

