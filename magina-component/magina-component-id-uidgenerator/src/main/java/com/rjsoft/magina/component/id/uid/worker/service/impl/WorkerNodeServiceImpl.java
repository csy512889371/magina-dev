package com.rjsoft.magina.component.id.uid.worker.service.impl;

import com.rjsoft.magina.component.id.uid.worker.entity.WorkerNodeEntity;
import com.rjsoft.magina.component.id.uid.worker.repository.WorkerNodeRepository;
import com.rjsoft.magina.component.id.uid.worker.service.WorkerNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkerNodeServiceImpl implements WorkerNodeService {

    @Autowired
    private WorkerNodeRepository workerNodeRepository;

    @Transactional
    @Override
    public void addWorkerNode(WorkerNodeEntity workerNodeEntity) {
        WorkerNodeEntity save = workerNodeRepository.save(workerNodeEntity);
        System.out.println(save);
    }
}

