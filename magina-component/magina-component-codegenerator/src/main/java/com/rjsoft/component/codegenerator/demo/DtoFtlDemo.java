package com.rjsoft.component.codegenerator.demo;

import com.rjsoft.component.codegenerator.database.MysqlDbTools;
import com.rjsoft.component.codegenerator.utils.FtUtil;
import com.rjsoft.component.codegenerator.utils.StringUtils;
import com.rjsoft.component.codegenerator.vo.JpaEntity;

import java.sql.Connection;
import java.util.List;

public class DtoFtlDemo {

    public static void main(String[] args) throws Exception {

        String path = "D:\\src\\";//FtlDemo.class.getResource("/").getPath();
        if (null != path && path.endsWith("/target/classes/")) {
            int lastIndexOf = path.lastIndexOf("/target/classes/");
            path = path.substring(0, lastIndexOf) + "/src/main/tmp/";
        }
        System.out.println(path);


        MysqlDbTools tools = new MysqlDbTools();
        Connection conn = tools.getConnection();
        List<JpaEntity> tables = tools.getTableList(conn);


        FtUtil ftUtil = new FtUtil();

        for (JpaEntity entity : tables) {
            String underLineScore = entity.getTable_name().toLowerCase();
            String camel = StringUtils.underScoreCase2CamelCase(underLineScore.toLowerCase());
            {
                String fileName =
                        StringUtils.toUpperCaseFirstOne(camel) + "Dto.java";
                String outDir = path + "com/rjsoft/process/dto/jycj/";
                ftUtil.generateFile("/ftl/", "JpaDto.java.ftl", entity, outDir, fileName);
            }
        }
    }
}
