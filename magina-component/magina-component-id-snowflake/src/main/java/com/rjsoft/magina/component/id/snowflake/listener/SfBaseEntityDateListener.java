package com.rjsoft.magina.component.id.snowflake.listener;

import com.rjsoft.magina.component.id.snowflake.entity.SfBaseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Component("sfBaseEntityDateListener")
public class SfBaseEntityDateListener {

    /**
     * Before add entity,  add OperTime
     *
     * @param baseEntity
     */
    @PrePersist
    public void initEntity(SfBaseEntity baseEntity) {
        baseEntity.setOperTime(new Date());
    }

    /**
     * Before update entity ,update OperTime
     */
    @PreUpdate
    public void updateEntity(SfBaseEntity baseEntity) {
        baseEntity.setOperTime(new Date());
    }
}
