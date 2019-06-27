package com.rjsoft.magina.component.id.uid.entity.listener;

import com.rjsoft.magina.component.id.uid.entity.UidBaseEntity;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Component("uidBaseEntityDateListener")
public class UidBaseEntityDateListener {

    /**
     * Before add entity,  add OperTime
     *
     * @param baseEntity
     */
    @PrePersist
    public void initEntity(UidBaseEntity baseEntity) {
        baseEntity.setOperTime(new Date());
    }

    /**
     * Before update entity ,update OperTime
     */
    @PreUpdate
    public void updateEntity(UidBaseEntity baseEntity) {
        baseEntity.setOperTime(new Date());
    }
}
