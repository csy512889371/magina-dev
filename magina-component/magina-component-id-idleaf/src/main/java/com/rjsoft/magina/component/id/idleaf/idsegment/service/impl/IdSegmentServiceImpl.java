package com.rjsoft.magina.component.id.idleaf.idsegment.service.impl;

import com.rjsoft.magina.component.id.idleaf.idsegment.entity.IdSegmentEntity;
import com.rjsoft.magina.component.id.idleaf.idsegment.repository.IdSegmentRepository;
import com.rjsoft.magina.component.id.idleaf.idsegment.service.IdSegmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("idSegmentService")
public class IdSegmentServiceImpl implements IdSegmentService {

    @Autowired
    private IdSegmentRepository idSegmentRepository;

    @Override
    @Transactional
    public void saveIdSegment(IdSegmentEntity idSegmentEntity) {
        idSegmentRepository.save(idSegmentEntity);
    }

    @Override
    @Transactional
    public IdSegmentEntity updateId(String bizTag) {

        IdSegmentEntity currentSegment = idSegmentRepository.findSegmentByBizTag(bizTag);

        if (currentSegment.getLastUpdateTime() == null) {
            currentSegment.setLastUpdateTime(new Date());
        }

        Long newMaxId = currentSegment.getMaxId() + currentSegment.getStep();
        currentSegment.setMaxId(newMaxId);
        currentSegment.setCurrentUpdateTime(new Date());

        return idSegmentRepository.save(currentSegment);
    }

}

