package com.rjsoft.jycj.process.entity.jycj;

<#if id_type?? && id_type?ends_with("PK")?string == 'true'>
import com.rjsoft.uums.common.entity.pk.${id_type};
</#if>
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjsoft.magina.component.query.jpa.annotation.JpaComment;
import com.rjsoft.magina.component.id.uid.entity.UidBaseEntity;
import lombok.*;
import org.springframework.format.annotation.*;

import javax.persistence.*;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JpaComment(name = "${comment}")
@Entity
@Table(name = "${table_name}", schema = "", catalog = "${catalog_name}")
<#if id_type?? && id_type?ends_with("PK")?string == 'false'>
@AttributeOverride(
        name = "id",
        column = @Column(name = "${id_name}", columnDefinition = "${id_columnDefinition}", nullable = ${toBooleanValue(id_nullable)}, length = ${id_length}, precision = ${id_precision}, scale = ${id_scale})
)
</#if>
public class ${toCamel(table_name)?cap_first}Entity extends UidBaseEntity<String>  {

    private static final long serialVersionUID = 1L;
<#if columns??>
<#list columns as column>

<#if column.name?upper_case!='ID'>

    <#if toBooleanValue(column.nullable) == 'false'>
    @NonNull
    </#if>
    @JpaComment(name = "${column.comment}")
    @Basic
    <#if column.isLob == 'true'>
    @Lob
    </#if>
    <#if column.type == 'Date'>
    // 与前端表单中的日期格式一致
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // DB 中的时间 转成 Json
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)})
    <#elseif column.type == 'String'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)}, length = ${column.length})
    <#elseif column.type == 'Long'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)}, precision = ${column.precision}, scale = ${column.scale})
    <#elseif column.type == 'Integer'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)}, precision = ${column.precision}, scale = ${column.scale})
    <#elseif column.type == 'Short'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)}, precision = ${column.precision}, scale = ${column.scale})
    <#elseif column.type == 'Byte'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)}, precision = ${column.precision}, scale = ${column.scale})
    <#elseif column.type == 'Boolean'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)})
    <#elseif column.type == 'Double'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)})
    <#elseif column.type == 'Float'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)})
    <#elseif column.type == 'Byte[]'>
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)})
    <#else >
    @Column(name = "${column.name}", nullable = ${toBooleanValue(column.nullable)})
    </#if>
    private ${column.type} ${toCamel(column.name)};

</#if>

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