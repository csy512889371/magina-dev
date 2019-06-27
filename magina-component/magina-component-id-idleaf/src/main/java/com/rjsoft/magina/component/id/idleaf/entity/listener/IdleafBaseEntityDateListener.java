package com.rjsoft.magina.component.id.idleaf.entity.listener;

import com.rjsoft.magina.component.id.idleaf.entity.IdLeafBaseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Component("idleafBaseEntityDateListener")
public class IdleafBaseEntityDateListener {

    /**
     * Before add entity,  add OperTime
     *
     * @param baseEntity
     */
    @PrePersist
    public void initEntity(IdLeafBaseEntity baseEntity) {
        baseEntity.setOperTime(new Date());
    }

    /**
     * Before update entity ,update OperTime
     */
    @PreUpdate
    public void updateEntity(IdLeafBaseEntity baseEntity) {
        baseEntity.setOperTime(new Date());
    }
}
