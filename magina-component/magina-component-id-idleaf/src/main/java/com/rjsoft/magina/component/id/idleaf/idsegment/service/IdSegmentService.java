package com.rjsoft.magina.component.id.idleaf.idsegment.service;


import com.rjsoft.magina.component.id.idleaf.idsegment.entity.IdSegmentEntity;

public interface IdSegmentService {

    void saveIdSegment(IdSegmentEntity idSegmentEntity);

    IdSegmentEntity updateId(String bizTag);


}

