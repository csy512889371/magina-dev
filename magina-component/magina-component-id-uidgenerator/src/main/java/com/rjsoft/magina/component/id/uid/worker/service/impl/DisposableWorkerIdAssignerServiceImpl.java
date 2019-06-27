
package com.rjsoft.magina.component.id.uid.worker.service.impl;

import com.rjsoft.magina.component.id.uid.worker.entity.WorkerNodeEntity;
import com.rjsoft.magina.component.id.uid.worker.service.WorkerIdAssignerService;
import com.rjsoft.magina.component.id.uid.worker.service.WorkerNodeService;
import com.rjsoft.magina.component.id.uid.worker.service.WorkerNodeType;
import com.rjsoft.magina.component.id.uid.worker.utils.DockerUtils;
import com.rjsoft.magina.component.id.uid.worker.utils.NetUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Represents an implementation of {@link WorkerIdAssignerService},
 * the worker id will be discarded after assigned to the UidGenerator
 *
 * @author yutianbao
 */
public class DisposableWorkerIdAssignerServiceImpl implements WorkerIdAssignerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisposableWorkerIdAssignerServiceImpl.class);

    @Resource
    private WorkerNodeService workerNodeService;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Transactional
    public long assignWorkerId() {
        // build worker node entity
        WorkerNodeEntity workerNodeEntity = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        workerNodeService.addWorkerNode(workerNodeEntity);
        LOGGER.info("Add worker node:" + workerNodeEntity);

        return workerNodeEntity.getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkerNodeEntity buildWorkerNode() {
        WorkerNodeEntity workerNodeEntity = new WorkerNodeEntity();
        workerNodeEntity.setLaunchDate(new Date());
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.value());
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());

        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.value());
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(0, 100000));
        }

        return workerNodeEntity;
    }

}
