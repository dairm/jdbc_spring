package com.netcracker.study.sql;

import com.sun.deploy.util.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataBaseManagerSpring implements DataBaseManager {

    private DriverManagerDataSource dataSource = new DriverManagerDataSource();
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public DataBaseManagerSpring() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUsername("SA");
        dataSource.setUrl("jdbc:h2:~/test");
        dataSource.setPassword("");
        setDataSource(dataSource);
    }

    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    public List<String> getTableColumns(String tableName) throws SQLException{
        return jdbcTemplate.query("SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'PUBLIC' AND TABLE_NAME = '" + tableName + "'",
                new RowMapper<String>(){
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("COLUMN_NAME");
                    }
                });
    }

    public void insertData(String tableName, Map<String, String> columnNameValuePairs) throws SQLException{
        String values = StringUtils.join( columnNameValuePairs.values(),",");
        String columns = StringUtils.join( columnNameValuePairs.keySet(),",");
        String sql = "INSERT INTO CUSTOMER " +
                "("+ columns +") VALUES ("+ values +")";

        jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(sql);
    }

    public Map<String, String>[] selectData(String tableName, String[] sqlFilters) throws SQLException {
        String sql = "";
        if(sqlFilters.length!=0) {
            for (int i = 0; i < sqlFilters.length - 1; i++) {
                sql += sqlFilters[i] + " AND ";
            }
            sql += sqlFilters[sqlFilters.length - 1];
            sql = "select * from " + tableName +
                    " where " + sql;
        }
        else {
            sql = "select * from " + tableName;
        }
        final List<String> list = getTableColumns(tableName);
        List<Map<String, String>> rows = jdbcTemplate.query(
                sql,
                new RowMapper<Map<String, String>>() {
                    public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, String> row = new HashMap<String, String>();
                        for (String l : list) {
                            row.put(l, rs.getString(l));
                        }
                        return row;
                    }
                });
        Map[] tbl;
        tbl = new HashMap[rows.size()];
        int i =0;
        for (Map a : rows) {
            tbl[i] = a;
            i++;
        }

        return tbl;
    }

    public void createTable(String creatreTableQuery) throws SQLException {
        jdbcTemplate.execute(creatreTableQuery);
    }

    public void dropTable(String tableName) throws SQLException{
        jdbcTemplate.execute("DROP TABLE " + tableName);
    }


}
