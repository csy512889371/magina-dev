package com.rjsoft.magina.component.id.idleaf.idsegment.repository;

import com.rjsoft.magina.component.id.idleaf.idsegment.entity.IdSegmentEntity;
import com.rjsoft.magina.component.query.jpa.GenericJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IdSegmentRepository extends GenericJpaRepository<IdSegmentEntity, String> {

    @Query(value = "select seg from  IdSegmentEntity seg where seg.bizTag= :bizTag")
    IdSegmentEntity findSegmentByBizTag(@Param("bizTag") String bizTag);

}

