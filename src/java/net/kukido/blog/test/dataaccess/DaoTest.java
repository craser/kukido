package net.kukido.blog.test.dataaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.*;
import static org.junit.Assert.*;

import net.kukido.blog.dataaccess.*;

public class DaoTest
{
    @Test
    public void test_instantiation() throws DataAccessException
    {
        Dao dao = new Dao() {}; // Create a dummy subclass of abstract Dao.
    }
    
    @Test
    public void test_getConnection() throws DataAccessException, SQLException
    {
        Dao dao = new Dao() {}; // dummy subclass
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = dao.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select 'foo'");
            assertTrue(rs.next());
            assertTrue("foo".equals(rs.getString(1)));
        }
        finally {
            try { rs.close(); } catch (Exception ignored) {}
            try { stmt.close(); } catch (Exception ignored) {}
            try { conn.close(); } catch (Exception ignored) {}
        }
    }
    
    @Test
    public void test_getLastCreatedId() {
        // FIXME: No idea how to verify this.  It's implicitly tested
        // when we test the subclasses, but I'd like to have a direct
        // test of the single method here.
    }
    
    @Test
    public void test_buildParamList() 
    {
        Dao dao = new Dao() {};
        assertTrue("()".equals(dao.buildParamList(-1)));
        assertTrue("()".equals(dao.buildParamList(0)));
        assertTrue("(?,?,?)".equals(dao.buildParamList(3)));
    }

}
