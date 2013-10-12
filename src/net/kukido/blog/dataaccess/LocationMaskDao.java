package net.kukido.blog.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.kukido.blog.datamodel.LocationMask;
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
            if (!rs.next()) {
            	throw new DataAccessException("No location mask found for user " + userId);
            }

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
        LocationMask mask = new LocationMask(results.getFloat("Latitude"), results.getFloat("Longitude"), results.getDouble("Radius"));
        return mask;
    }

}
