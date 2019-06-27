package com.rjsoft.component.codegenerator.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhangzhiyi on 2018/3/28.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaRepository {

    private String repository_type;
    private String entity_type;
    private String id_type;
}
