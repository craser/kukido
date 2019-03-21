package net.kukido.blog.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sun.util.logging.resources.logging;

import net.kukido.blog.datamodel.LocationMask;
import net.kukido.blog.datamodel.Location;
import net.kukido.sql.NamedParamStatement;

public class LocationMaskDao extends Dao {

    private static final String FIND_BY_USER_ID = "select * from MASKED_LOCATIONS where User_ID = :User_ID";


    public LocationMask findByUserId(int userId) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_USER_ID);
            find.setInt("User_ID", userId);
            rs = find.executeQuery();
            return populateLocationMask(rs);
        }
        catch (Exception e) {
            throw new DataAccessException("Error retrieving location masks for user " + userId, e);
        }
        finally {
            try {
                rs.close();
            }
            catch (Exception ignored) {
            }
            try {
                find.close();
            }
            catch (Exception ignored) {
            }
            try {
                conn.close();
            }
            catch (Exception ignored) {
            }
        }
    }
    
    private LocationMask populateLocationMask(ResultSet results) throws SQLException, DataAccessException
    {
    	LocationMask mask = new LocationMask();
    	while (results.next()) {
    		Location loc = populateLocation(results);
    		mask.add(loc);
    	}
    	return mask;
    }

    private Location populateLocation(ResultSet results) throws SQLException, DataAccessException
    {
        Location mask = new Location(results.getFloat("Latitude"), results.getFloat("Longitude"), results.getDouble("Radius"));
        return mask;
    }

}
