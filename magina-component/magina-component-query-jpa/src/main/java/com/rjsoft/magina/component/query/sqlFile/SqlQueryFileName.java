package com.rjsoft.magina.component.query.sqlFile;


import lombok.Getter;

/**
 * SqlQuery文件名
 */
@Getter
public enum SqlQueryFileName {

    infoSet("infoSet", "动态信息集");

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 描述
     */
    private String des;

    SqlQueryFileName(String fileName, String des) {
        this.fileName = fileName;
        this.des = des;
    }


}
