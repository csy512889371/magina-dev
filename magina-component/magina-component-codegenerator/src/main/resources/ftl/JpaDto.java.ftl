package com.rjsoft.jycj.process.service.provider.dto.jycj;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("${comment}")
public class ${toCamel(table_name)?cap_first}Dto  implements Serializable {

<#if columns??>
    <#list columns as column>

    @ApiModelProperty(value = "${column.comment}")
    private ${column.type} ${toCamel(column.name)};

    </#list>
</#if>
}
<#function toDashed(s)>
    <#return s
    <#-- "fooBar" to "foo_bar": -->
    ?replace('(_)([a-zA-Z])', '$1_$2', 'r')
    <#-- "foo_bar" to "FOO_BAR": -->
    ?upper_case
    >
</#function>
<#function toBooleanValue(bool)>
    <#return bool?string("true", "false")
    >
</#function>
<#function toCamel(s)>
    <#return s
    ?lower_case
    ?replace('_a', 'A')
    ?replace('_b', 'B')
    ?replace('_c', 'C')
    ?replace('_d', 'D')
    ?replace('_e', 'E')
    ?replace('_f', 'F')
    ?replace('_g', 'G')
    ?replace('_h', 'H')
    ?replace('_i', 'I')
    ?replace('_j', 'J')
    ?replace('_k', 'K')
    ?replace('_l', 'L')
    ?replace('_m', 'M')
    ?replace('_n', 'N')
    ?replace('_o', 'O')
    ?replace('_p', 'P')
    ?replace('_q', 'Q')
    ?replace('_r', 'R')
    ?replace('_s', 'S')
    ?replace('_t', 'T')
    ?replace('_u', 'U')
    ?replace('_v', 'V')
    ?replace('_w', 'W')
    ?replace('_x', 'X')
    ?replace('_y', 'Y')
    ?replace('_z', 'Z')
    >
</#function>