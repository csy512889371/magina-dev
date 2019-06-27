package com.rjsoft.magina.component.id.uid.worker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JpaComment(name = "DB WorkerID Assigner for UID Generator")
@Entity
@Table(name = "sys_worker_node", schema = "", catalog = "")
public class WorkerNodeEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;

    @JpaComment(name = "主机名")
    @Basic
    @Column(name = "HOST_NAME", nullable = true, length = 64)
    private String hostName;


    @JpaComment(name = "端口")
    @Basic
    @Column(name = "PORT", nullable = true, length = 64)
    private String port;


    @JpaComment(name = "节点类型: ACTUAL or CONTAINER")
    @Basic
    @Column(name = "TYPE", nullable = true, precision = 10, scale = 0)
    private Integer type;


    @JpaComment(name = "启动时间")
    @Basic
    // 与前端表单中的日期格式一致
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // DB 中的时间 转成 Json
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "LAUNCH_DATE", nullable = true)
    private Date launchDate;


}
