/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.kukido.blog.util;

import java.util.*;

/**
 * I'd LOVE to clean this up with generics.  Unfortunately,
 * this doesn't seem possible.  Observe:
 * 
 * BucketMap<K,V> would extend HashMap<K, Collection<V>>.
 * 
 * This creates a problem when we try to define BucketMap.get():
 * 
 * public Collection<V> put(K key, V val)
 * 
 * The compiler chokes on this, since V doesn't necessarily 
 * extend/implement Collection<V>.  Doh!
 * 
 * 
 * @author craser
 */
public class BucketMap extends HashMap
{
    
    public Object put(Object key, Object val)
    {
        Collection bucket = containsKey(key)
            ? (Collection)get(key)
            : getNewBucket();
        bucket.add(val);
        return super.put(key, bucket);
    }
    
    /**
     * Here to enable subclassing.
     * @return
     */
    protected Collection getNewBucket()
    {
        return new HashSet();
    }
}
