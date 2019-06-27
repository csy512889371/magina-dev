package com.rjsoft.component.codegenerator.database;


import com.rjsoft.component.codegenerator.constant.DatabaseConstant;
import com.rjsoft.component.codegenerator.utils.StringUtils;
import com.rjsoft.component.codegenerator.vo.JpaEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MysqlDbTools {

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
                DatabaseConstant.getUrl(),
                DatabaseConstant.getUsername(),
                DatabaseConstant.getPassword()
        );
        return conn;
    }

    public List<JpaEntity> getTableList(Connection conn) throws SQLException {
        List<JpaEntity> tableList = new ArrayList<>();

        String sql =
                "select TABLE_NAME, TABLE_COMMENT from INFORMATION_SCHEMA.Tables where table_schema = '" + DatabaseConstant.getTableSchema() + "'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            JpaEntity entity = new JpaEntity();
            String TABLE_NAME = rs.getString("TABLE_NAME");
            if (TABLE_NAME.startsWith("BIN$")) {
                continue;
            }

            String COMMENTS = rs.getString("TABLE_COMMENT");
            entity.setTable_name(TABLE_NAME);
            entity.setComment(COMMENTS == null ? "" : COMMENTS);
            entity.setSchema_name(DatabaseConstant.getUsername().toUpperCase());

            //获取列
            List<JpaEntity.EntityColumn> columnList = getColumnList(conn, TABLE_NAME);
            Iterator<JpaEntity.EntityColumn> iterator = columnList.iterator();
            int idNum = 0;
            while (iterator.hasNext()) {
                JpaEntity.EntityColumn column = iterator.next();
                if (column.getIsPrimaryKey()) {
                    entity.setId_name(column.getName());
                    entity.setId_type(column.getType());
                    entity.setId_nullable(column.getNullable());
                    entity.setId_length(column.getLength());
                    entity.setId_precision(column.getPrecision());
                    entity.setId_scale(column.getScale());
                    entity.setId_columnDefinition(column.getColumnDefinition());
                    idNum++;
                    iterator.remove();
                }
                if("oper_time".equals(column.getName().toLowerCase())){
                    iterator.remove();
                }
            }
            if (idNum > 1) {
                String underLineScore = entity.getTable_name().toLowerCase();
                String camel = StringUtils.underScoreCase2CamelCase(underLineScore.toLowerCase());
                String camelUpCase = StringUtils.toUpperCaseFirstOne(camel);
                entity.setId_type(camelUpCase + "EntityPK");
                entity.setId_generator("AbstractEmbeddedPersistable");
            }
            entity.setColumns(columnList);

            if ("String".equals(entity.getId_type())) {
                entity.setId_generator("AbstractUUIDPersistable");
            } else if ("UUID".equals(entity.getId_type())) {
                entity.setId_generator("AbstractUUIDHexPersistable");
            }

            //这里是过滤特殊的表，比如只生成SYS开头的表
            //if (TABLE_NAME.startsWith("SYS"))
            tableList.add(entity);
            System.out.println("TABLE_NAME ==>" + TABLE_NAME + "  COMMENTS==>" + COMMENTS);
        }
        rs.close();
        ps.close();
        return tableList;
    }

    public List<JpaEntity.EntityColumn> getColumnList(Connection conn, String tableName)
            throws SQLException {


        List<JpaEntity.EntityColumn> columnList = new ArrayList<>();


        String sql = "SELECT TABLE_NAME AS TABLE_NAME,COLUMN_NAME AS COLUMN_NAME,DATA_TYPE AS DATA_TYPE,CHARACTER_MAXIMUM_LENGTH AS DATA_LENGTH,NUMERIC_PRECISION AS DATA_PRECISION,\n" +
                "        NUMERIC_SCALE AS DATA_SCALE,COLUMN_DEFAULT AS DATA_DEFAULT,IS_NULLABLE AS NULLABLE,COLUMN_COMMENT AS COMMENTS,COLUMN_KEY AS PRIMARY_KEY\n" +
                "        FROM\n" +
                "        INFORMATION_SCHEMA.COLUMNS\n" +
                "                WHERE\n" +
                "        TABLE_NAME = ?\n" +
                "        AND TABLE_SCHEMA= ?\n" +
                "        ORDER BY ORDINAL_POSITION ASC";


        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, tableName);
        ps.setString(2, DatabaseConstant.getTableSchema());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            JpaEntity.EntityColumn column = new JpaEntity.EntityColumn();

            String COLUMN_NAME = rs.getString("COLUMN_NAME");
            String DATA_TYPE = rs.getString("DATA_TYPE");//VARCHAR2
            String DATA_LENGTH = rs.getString("DATA_LENGTH");//200
            String DATA_PRECISION = rs.getString("DATA_PRECISION");//200
            String DATA_SCALE = rs.getString("DATA_SCALE");//200
            String DATA_DEFAULT = rs.getString("DATA_DEFAULT");
            String NULLABLE = rs.getString("NULLABLE");
            String COMMENTS = rs.getString("COMMENTS");
            String PRIMARY_KEY = rs.getString("PRIMARY_KEY");

            column.setName(COLUMN_NAME);
            column.setType(DATA_TYPE);
            column.setLength(DATA_LENGTH == null ? "0" : DATA_LENGTH.replace(",", ""));
            column.setPrecision(Integer.valueOf(DATA_PRECISION == null ? "0" : DATA_PRECISION));
            column.setScale(Integer.valueOf(DATA_SCALE == null ? "0" : DATA_SCALE));
            column.setDefaultValue(DATA_DEFAULT == null ? "" : DATA_DEFAULT);
            column.setNullable("N".equals(NULLABLE) ? false : true);
            column.setComment(COMMENTS == null ? "" : COMMENTS.replace("\n", " | "));
            column.setIsPrimaryKey("true".equals(PRIMARY_KEY) ? true : false);

            if ("VARCHAR".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("String");
                column.setColumnDefinition(String.format("VARCHAR(%s)", column.getLength()));
            }else if ("TEXT".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("String");
            } else if ("CHAR".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("String");
                column.setColumnDefinition(String.format("CHAR(%s)", column.getLength()));
            } else if ("CLOB".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("String");
                column.setColumnDefinition(String.format("CLOB"));
                column.setIsLob("true");
            } else if ("BLOB".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Byte[]");
                column.setColumnDefinition(String.format("BLOB", column.getLength()));
                column.setIsLob("true");
            } else if ("RAW".equalsIgnoreCase(DATA_TYPE) && "16".equals(DATA_LENGTH)) {
                column.setType("UUID");
                column.setColumnDefinition("RAW(16)");
            } else if ("DATE".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Date");
            } else if ("DATETIME".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Date");
            } else if ("INT".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Integer");
                column.setColumnDefinition("INT DEFAULT 0");
            } else if ("BIGINT".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Long");
                column.setColumnDefinition("Long DEFAULT 0L");
            } else if ("TINYINT".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Boolean");
                column.setColumnDefinition("INT DEFAULT 0");
            } else if ("FLOAT".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Float");
                column.setColumnDefinition(String.format("float(%d,%d)", column.getPrecision(), column.getScale()));
            } else if ("DOUBLE".equalsIgnoreCase(DATA_TYPE)) {
                column.setType("Double");
                column.setColumnDefinition(String.format("double(%d,%d)", column.getPrecision(), column.getScale()));
            } else if ("NUMBER".equalsIgnoreCase(DATA_TYPE)) {
                if ("0".equals(DATA_SCALE)) {
                    if (column.getPrecision() < 3) {
                        column.setType("Byte");
                    } else if (column.getPrecision() < 5) {
                        column.setType("Short");
                    } else if (column.getPrecision() < 10) {
                        column.setType("Integer");
                    } else {
                        column.setType("Long");
                    }
                    column.setColumnDefinition(String.format("NUMBER(%d,0)", column.getPrecision()));
                } else {
                    if (column.getPrecision() + column.getScale() < 8) {
                        column.setType("Float");
                    } else {
                        column.setType("Double");
                    }
                    column.setColumnDefinition(String.format("NUMBER(%d, %d)", column.getPrecision(), column.getScale()));
                }
            }
            if ("NUMBER".equals(DATA_TYPE) && "false".equals(NULLABLE)) {
                column.setType(column.getType().toLowerCase());
            }
            columnList.add(column);

            System.out.println("COLUMN_NAME ==>" + COLUMN_NAME + "  DATA_TYPE==>" + DATA_TYPE + "  DATA_LENGTH==>" + DATA_LENGTH + " NULLABLE==>" + NULLABLE + "  COMMENTS==>" + COMMENTS + " PRIMARY_KEY==>" + PRIMARY_KEY);
        }
        rs.close();
        ps.close();
        return columnList;
    }
}
