package net.kukido.io;

import java.io.*;

/**
 * This class implements text formatting using indentation.
 * Default indentation width is 8 characters.
 */
public class IndentedWriter extends FilterWriter
{
    // Class constants
    public final int DEFAULT_INDENT_WIDTH = 8;

    // Instance variables.
    private int indentLevel;
    private char[] indentSpace;
    private boolean newline;

    public IndentedWriter(Writer out)
    {
	super(out);
	newline = false;
	setIndentLevel(0); // Start out not indenting anything.
	setIndentWidth(DEFAULT_INDENT_WIDTH);
    }

    /**
     * Increments the indentation level.
     */
    public void push()
    {
	setIndentLevel(indentLevel + 1);
    }
    
    /**
     * Decrements the indentation level, to a minimum of zero.
     */
    public void pop()
    {
	setIndentLevel(indentLevel - 1);
    }
    
    /**
     * Forces the indentation level to indentLevel, or 0 if
     * indentLevel is negative.
     */
    public void setIndentLevel(int indentLevel)
    {
	if (indentLevel >= 0) {
	    this.indentLevel = indentLevel;
	}
	else {
	    this.indentLevel = 0;
	}
    }

    /**
     * Sets the number of spaces in one level of indentation.
     * Arguments < 1 result in the indentWidth being set to 1.
     */
    public void setIndentWidth(int indentWidth)
    {
	if (indentWidth < 1) indentWidth = 1;
	indentSpace = new char[indentWidth];
	for (int i = 0; i < indentSpace.length; i++)
	    indentSpace[i] = ' ';
    }
	    
    /**********************************************************************/
    // Implementation of FilterWriter
    public void write(int c)
	throws IOException
    {
	// If we just printed a newline, then indent this line before
	// continuing.
	if (newline) {
	    newline = false;
	    indentLine();
	}
	// Otherwise, just continue like normal.
	super.write(c);
	
	// But if it's a newline, remember that.
	newline = ('\n' == c);
    }

    public void write(String s, int off, int len)
	throws IOException
    {
	write(s.toCharArray(), off, len);
    }

    public void write(char[] cbuff, int off, int len)
	throws IOException
    {
	int limit = off + len; // Remember when to stop.
	for (int i = off; i < limit; i++)
	{
	    // Write the char, regardless
	    write((int)cbuff[i]);

	    // If it's a newline, indent the next line.
	    //if ('\n' == cbuff[i]) { indentLine(); }
	}
    }

    private void indentLine()
	throws IOException
    {
	for (int i = 0; i < indentLevel; i++)
	    super.write(indentSpace, 0, indentSpace.length);
	if (indentLevel > 0) super.write('|');
    }
}

    
	
	
    
