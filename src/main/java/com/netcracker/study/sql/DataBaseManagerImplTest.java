package com.netcracker.study.sql;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.sql.JDBCType.NULL;


public class DataBaseManagerImplTest {

    private DataBaseManagerImpl db = new DataBaseManagerImpl();
    private final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    public DataBaseManagerImplTest() throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException {
    }

    @Test
    public void getConnection() throws SQLException {
        Assert.assertEquals("conn0: url=jdbc:h2:~/test user=SA", db.getConnection().toString());
    }

    @Test
    public void getTableColumns() throws SQLException {
        List<String> l = new ArrayList<String>();
        l.add("ID");
        l.add("NAME");
        Assert.assertEquals(l, db.getTableColumns("TEST"));
    }

    @Test
    public void insertData() throws SQLException {
//        Map<String, String> row = new HashMap<String, String>();
//        row.put("ID", "3");
//        row.put("NAME", "ADD");
//        db.insertData("TEST", row);
    }

    @Test
    public void selectData() throws SQLException {
        String[] filters1 = {};
        Map<String,String>[] m = db.selectData("TEST", filters1);
        Assert.assertEquals(3, m.length);
        Assert.assertEquals("2", m[1].get("ID"));

        String[] filters2 = {"ID = 3"};
        Map<String,String>[] mf = db.selectData("TEST", filters2);
        Assert.assertEquals(1, mf.length);
        Assert.assertEquals("3", mf[0].get("ID"));
    }

    @Test
    public void createAndDrop() throws SQLException {
        db.createTable("CREATE TABLE TestTBL");
        db.dropTable("TestTBL");
    }
}