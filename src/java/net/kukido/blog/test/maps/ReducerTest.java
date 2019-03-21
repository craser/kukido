package net.kukido.blog.test.maps;


import java.util.Date;

import net.kukido.maps.GpsLocation;
import net.kukido.maps.GpsTrack;
import net.kukido.maps.Reducer;

import org.junit.* ;
import static org.junit.Assert.* ;

public class ReducerTest {
	
	@Test
	public void test_reduce() {
		Reducer reducer = new Reducer();
		GpsTrack track = new GpsTrack();
		track.add(new GpsLocation(0f, 0f, 0f, new Date()));
		track.add(new GpsLocation(10f, 20f, 0f, new Date()));
		track.add(new GpsLocation(20f, 19f, 0f, new Date()));
		track.add(new GpsLocation(30f, 20f, 0f, new Date()));
		track.add(new GpsLocation(40f, 0f, 0f, new Date()));
		
		GpsTrack reduced = reducer.reduce(track, 4, 200);
		assertTrue(reduced.size() == 4);
		assertTrue(reduced.get(0).equals(track.get(0)));
		assertTrue(reduced.get(1).equals(track.get(1)));
		assertTrue(reduced.get(2).equals(track.get(3)));
		assertTrue(reduced.get(3).equals(track.get(4)));
	}

}
