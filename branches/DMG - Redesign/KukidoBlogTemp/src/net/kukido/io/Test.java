package net.kukido.io;

import java.io.*;

public class Test
{
    public static void main(String[] args)
    {
	IndentedWriter indent =
	    new IndentedWriter(new OutputStreamWriter(System.out));

	PrintWriter out = new PrintWriter(indent);

	new Exception("Numero 1").printStackTrace(out);
	out.flush();
	indent.push();

	new Exception("Numero 2").printStackTrace(out);
	out.flush();
	indent.push();

	new Exception("Numero 3").printStackTrace(out);
	out.flush();
	indent.setIndentLevel(0);

	new Exception("Numero 4").printStackTrace(out);
	out.flush();
    }
}
