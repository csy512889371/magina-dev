package com.rjsoft.magina.component.id.idleaf.idsegment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rjsoft.magina.component.query.jpa.annotation.JpaComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JpaComment(name = "号段存储表")
@Entity
@Table(name = "sys_id_segment", schema = "", catalog = "")
public class IdSegmentEntity {

    private static final long serialVersionUID = 1L;


    @JpaComment(name = "业务标识")
    @Basic
    @Id
    @Column(name = "BIZ_TAG", nullable = true, length = 64)
    private String bizTag;


    @JpaComment(name = "步长")
    @Basic
    @Column(name = "STEP", nullable = true, precision = 10, scale = 0)
    private Long step;

    /**
     * 中间值(缓存阈值-用于更新双buffer的阈值。目前阈值比是50%)
     */
    @Transient
    private Long middleId;

    /**
     * 最小id
     */
    @Transient
    private Long minId;


    @JpaComment(name = "最大值")
    @Basic
    @Column(name = "MAX_ID", nullable = true)
    private Long maxId;


    @JpaComment(name = "上次修改时间")
    @Basic
    // 与前端表单中的日期格式一致
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // DB 中的时间 转成 Json
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "LAST_UPDATE_TIME", nullable = true)
    private Date lastUpdateTime;


    @JpaComment(name = "当前修改时间")
    @Basic
    // 与前端表单中的日期格式一致
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // DB 中的时间 转成 Json
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "CURRENT_UPDATE_TIME", nullable = true)
    private Date currentUpdateTime;


    public Long getMiddleId() {

        if (this.middleId == null) {
            this.middleId = this.maxId - (long) Math.round(step / 2);
        }
        return middleId;
    }

    public Long getMinId() {
        if (this.minId == null) {
            if (this.maxId != null && this.step != null) {
                this.minId = this.maxId - this.step;
            } else {
                throw new RuntimeException("maxid or step is null");
            }
        }

        return minId;
    }

}
