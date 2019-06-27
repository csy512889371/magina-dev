package com.rjsoft.component.codegenerator.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhiyi on 2018/3/28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaEntity {

    private String table_name;
    private String schema_name;
    private String catalog_name = "";
    private String comment;

    private String id_name;
    private String id_type;
    private String id_generator = "AbstractAssignedPersistable";
    private Boolean id_nullable;
    private String id_length;
    private Integer id_precision;
    private Integer id_scale;
    private String id_columnDefinition;

    private List<EntityColumn> columns = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntityColumn {
        private String type;
        private String name;
        private Boolean nullable;
        private String length = "0";
        private Integer precision = 0;
        private Integer scale = 0;
        private String comment;
        private String defaultValue;
        private Boolean isPrimaryKey;
        private String columnDefinition = "";
        private String isLob = "false";
    }
}
