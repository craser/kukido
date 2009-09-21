/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.util;

import java.util.*;

/**
 *
 * @author craser
 */
public class BucketMap extends HashMap
{
    public interface BucketFactory {
        public Collection getNew();
    }
    
    private BucketFactory bf;
    
    public BucketMap()
    {
        this(new BucketFactory() {
            public Collection getNew() {
                return new HashSet();
            }
        });
    }
    
    public BucketMap(BucketFactory bf)
    {
        this.bf = bf;
    }
    
    public BucketMap(final Class c)
    {
        this(new BucketFactory() {
            public Collection getNew() {
                try { return (Collection)c.newInstance(); }
                catch (Exception e) { 
                    throw new IllegalStateException("Unable to create new instance of " + c.getName()); 
                }
            }
        });
    }
    
    protected Collection getNewBucket()
    {
        return this.bf.getNew();
    }
    
    public Object put(Object key, Object val)
    {
        Collection bucket = containsKey(key)
            ? (Collection)get(key)
            : getNewBucket();
        bucket.add(val);
        return super.put(key, bucket);
    }
}
