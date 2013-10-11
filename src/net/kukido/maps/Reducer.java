package net.kukido.maps;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of Ramer–Douglas–Peucker algorithm.
 * @author craser
 *
 */
public class Reducer
{
    /**
     * 
     * @param maxPointes
     * @param ignored
     * @return
     */
    public GpsTrack reduce(GpsTrack track, int maxPointes, double minDistance) // This seems to confuse the Struts tags.  Changing away from getXxx(int) un-confuses Struts.
    {   
        List<Segment> segments = new LinkedList<Segment>();
        segments.add(new Segment(track, 0, track.size() - 1));
        
        while (segments.size() < (maxPointes - 1)) {
            int i = findMostInteresting(segments);
            Segment s = segments.remove(i);
            if (s.getInterest() >= minDistance) {
                segments.addAll(i, s.split());
            }
            else {
                break;
            }
        }
        
        return toTrack(segments);
    }
    
    private GpsTrack toTrack(List<Segment> segments) {
        GpsTrack track = new GpsTrack();
        for (Segment s : segments) {
            track.add(s.getStart());
        }
        track.add(segments.get(segments.size() - 1).getEnd());
        
        return track;
        
    }
    
    private int findMostInteresting(List<Segment> segments) {
        double maxInterest = 0d;
        int maxIndex = 0;
        for (int i = 0; i < segments.size(); i++) {
            double interest = segments.get(i).getInterest();
            if (interest > maxInterest) {
                maxInterest = interest;
                maxIndex = i;
            }
        }
        
        return maxIndex;
    }

}
