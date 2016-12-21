package com.netcracker.study.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataBaseManager
{
    Connection getConnection() throws SQLException;

    /**
     * @param tableName- name of table to get columns
     * @return list of colomn name of the table in upper case
     *
     * @throws SQLException
     */
    List<String> getTableColumns(String tableName) throws SQLException;

    /**
     * inserts a single row into table
     *
     * @param tableName
     * @param columnNameValuePairs
     * @throws SQLException
     */
    void insertData(String tableName, Map<String, String> columnNameValuePairs) throws SQLException;

    /**
     * Select all columns data from table with specified sql filters.
     * For instance, if we call: <br/><br/>
     *
     * <b>selectData("MY_TABLE", new String[]{"COL_A IS NOT NULL", "COL_B LIKE '%abc_'");</b><br/><br/>
     *
     * Then we should get result of the query like:<br/><br/>
     *
     * <b>select * from MY_TABLE where COL_A is not NULL and COL_B LIKE '%abc_';</b><br/>
     *
     * @param tableName
     * @param sqlFilters
     * @return
     * @throws SQLException
     */
    Map<String, String>[] selectData(String tableName, String[] sqlFilters) throws SQLException;

    /**
     * Just execute the query <b>creatreTableQuery</b> to create some table
     *
     * @param creatreTableQuery
     * @throws SQLException
     */
    void createTable(String creatreTableQuery) throws SQLException;

    /**
     * drops table with given name
     *
     * @param tableName
     * @throws SQLException
     */
    void dropTable(String tableName) throws SQLException;
}