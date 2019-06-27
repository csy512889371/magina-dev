package com.rjsoft.magina.component.id.idleaf.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rjsoft.magina.component.id.idleaf.entity.generator.IdleafIdentifierGenerator;
import com.rjsoft.magina.component.id.idleaf.entity.listener.IdleafBaseEntityDateListener;
import com.rjsoft.magina.component.query.jpa.annotation.JpaComment;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * .
 *
 * @author stormning on 16/6/13.
 */
@MappedSuperclass
@EntityListeners(value = {IdleafBaseEntityDateListener.class})
public abstract class IdLeafBaseEntity<PK extends Serializable> implements Persistable<PK> {

    @Id
    @GenericGenerator(name = "idleaf", strategy = IdleafIdentifierGenerator.TYPE)
    @GeneratedValue(generator = "idleaf")
    @JsonProperty
    private PK id;

    @JpaComment(name = "操作时间")
    @JsonProperty
    // 与前端表单中的日期格式一致
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // DB 中的时间 转成 Json
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "oper_time")
    private Date operTime;


    public PK getId() {
        return id;
    }

    protected void setId(final PK id) {
        this.id = id;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    @Transient
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }

        AbstractPersistable<?> that = (AbstractPersistable<?>) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }
}
