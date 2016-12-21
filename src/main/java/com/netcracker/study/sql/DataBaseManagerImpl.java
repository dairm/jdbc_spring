package com.netcracker.study.sql;

import com.sun.rowset.JdbcRowSetImpl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.ResultSet;
import java.util.List;
import org.h2.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.sql.Statement;
import java.util.HashMap;

public class DataBaseManagerImpl implements DataBaseManager {
    private Connection con;

    public DataBaseManagerImpl() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Driver.class.newInstance();

        con = DriverManager.getConnection(
                "jdbc:h2:~/test",
                "SA",
                "");
    }

    public Connection getConnection() throws SQLException {
        return con;
    }

    public List<String> getTableColumns(String tableName) throws SQLException{
        Statement stmt = con.createStatement();
        ResultSet rs = con.getMetaData()
                .getColumns(null, null, tableName, null);
        List<String> listColumns = new ArrayList<String>();
        while (rs.next()) {
            String s = rs.getString(4);;
                    listColumns.add(s);

        }
        return listColumns;
    }

    public void insertData(String tableName, Map<String, String> columnNameValuePairs) throws SQLException{
        Statement stmt = con.createStatement();
        List<String> listColumns = getTableColumns(tableName);
        JdbcRowSetImpl jdbcRs = null;
        if(columnNameValuePairs.keySet().containsAll(listColumns)){
            jdbcRs = new JdbcRowSetImpl(con);
            jdbcRs.setCommand("select * from " + tableName);
            jdbcRs.execute();
            Iterator i = columnNameValuePairs.keySet().iterator();
            jdbcRs.moveToInsertRow();
            while(i.hasNext()){
                String strKey = i.next().toString();
                jdbcRs.updateString(strKey,columnNameValuePairs.get(strKey));
            }
            jdbcRs.insertRow();
        }
    }

    public Map<String, String>[] selectData(String tableName, String[] sqlFilters) throws SQLException{
        String str = "";
        Statement stmt = con.createStatement();
        ResultSet rsData;
        if(sqlFilters.length!=0) {
            for (int i = 0; i < sqlFilters.length - 1; i++) {
                str += sqlFilters[i] + " AND ";
            }
            str += sqlFilters[sqlFilters.length - 1];

            rsData = stmt.executeQuery("select * from " + tableName +
                    " where " + str);
        }
        else {
            rsData = stmt.executeQuery("select * from " + tableName);
        }

        Map[] tbl;
        List<String> list = getTableColumns(tableName);
        List<Map> rowList = new ArrayList<Map>();
        int i = 0;
        while (rsData.next()) {
            Map<String, String> row = new HashMap<String, String>();
            for (String aList : list) {
                String s = rsData.getString(aList);
                row.put(aList, s);
            }
            rowList.add(row);
        }

        tbl = new HashMap[rowList.size()];
        for (Map a : rowList) {
            tbl[i] = a;
            i++;
        }

        return tbl;
    }

    public void createTable(String creatreTableQuery) throws SQLException {
        Statement stmt = null;
        this.con.setAutoCommit(false);
        stmt = this.con.createStatement();

        stmt.addBatch(creatreTableQuery);
        int [] updateCounts = stmt.executeBatch();
        this.con.commit();
    }

    public void dropTable(String tableName) throws SQLException{
        Statement stmt = null;
        this.con.setAutoCommit(false);
        stmt = this.con.createStatement();

        stmt.addBatch("DROP TABLE " + tableName);
        int [] updateCounts = stmt.executeBatch();
        this.con.commit();
    }
}
