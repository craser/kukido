package net.kukido.abstraction;

/**
 * Holds global utility methods for abstracting loops and conditions.
 * (Think Scheme's map.)
 **/
public class Abstraction
{
    static public Object[] map(Function f, Object[] o)
    {
	Object[] v = new Object[o.length];
	for (int i = 0; i < o.length; i++) {
	    v[i] = f.invoke(o[i]);
	}
	return v;
    }

    static public void main(String[] args)
    {
	Integer[] ints = new Integer[] {
	    new Integer(1)
	    ,new Integer(2)
	    ,new Integer(3)
	    ,new Integer(4)
	    ,new Integer(5)
	};

	Function println = new Function() {
		public Object invoke(Object o) {
		    System.out.println(o);
		    return null;
		}
	    };

	map(println, ints);
    }
}
