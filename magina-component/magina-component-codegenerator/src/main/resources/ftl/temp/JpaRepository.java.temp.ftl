package com.rjsoft.jycj.process.repository.jycj;

<#if id_type?? && id_type?ends_with("PK")?string == 'true'>
import com.rjsoft.jycj.process.entity.jycj.pk.${id_type};
</#if>
import com.rjsoft.jycj.process.entity.jycj.${toCamel(table_name)?cap_first}Entity;
import com.rjsoft.magina.component.query.jpa.GenericJpaRepository;

/**
 * Created by xxx on .
 */
public interface ${toCamel(table_name)?cap_first}Repository extends GenericJpaRepository<${toCamel(table_name)?cap_first}Entity, String> {

}

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