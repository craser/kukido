package net.kukido.blog.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.kukido.maps.LocationMask;
import net.kukido.sql.NamedParamStatement;

public class MaskedLocationsDao extends Dao {

    private static final String FIND_BY_USER_ID = "select * from MASKED_LOCATIONS where User_ID = :User_ID";
    


    public Collection<LocationMask> findByUserId(int userId) throws DataAccessException
    {
        Connection conn = null;
        NamedParamStatement find = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            find = new NamedParamStatement(conn, FIND_BY_USER_ID);
            find.setInt("User_ID", userId);

            rs = find.executeQuery();

            Collection<LocationMask> masks = populateLocationMasks(rs);
            return masks;
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
    


    private Collection<LocationMask> populateLocationMasks (ResultSet results) throws SQLException, DataAccessException
    {
    	List<LocationMask> masks = new ArrayList<LocationMask>();
    	while (results.next()) {
        	LocationMask mask = new LocationMask(results.getFloat("Latitude"), results.getFloat("Longitude"), results.getDouble("Radius"));
        	masks.add(mask);
    		
    	}
    	
    	return masks;
    }

}
