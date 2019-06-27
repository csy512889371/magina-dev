
package com.rjsoft.magina.component.id.idleaf.service.impl;

import com.rjsoft.magina.component.id.idleaf.idsegment.service.IdSegmentService;
import com.rjsoft.magina.component.id.idleaf.service.IdLeafService;
import com.rjsoft.magina.component.id.idleaf.service.IdLeafServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author sunff
 */
@Service
public class DefaultIdLeafServiceFactory implements IdLeafServiceFactory {

    private static ConcurrentHashMap<String, IdLeafService> bizTagIdLeaf = new ConcurrentHashMap<>();

    /**
     * 填充长度
     */
    protected int paddingLength = 8;


    @Autowired
    private IdSegmentService idSegmentService;

    private ExecutorService taskExecutor;

    public void setTaskExecutor(ExecutorService taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public Long getIdByBizTag(String bizTag) {

        IdLeafService issdervice = null;

        if (bizTagIdLeaf.get(bizTag) == null) {
            synchronized (bizTagIdLeaf) {
                if (bizTagIdLeaf.get(bizTag) == null) {
                    MysqlIdLeafServiceImpl idleafService = new MysqlIdLeafServiceImpl(idSegmentService);
                    idleafService.setBizTag(bizTag);
                    idleafService.setAsynLoadingSegment(true);
                    idleafService.setTaskExecutor(taskExecutor);
                    idleafService.init();
                    bizTagIdLeaf.putIfAbsent(bizTag, idleafService);

                }
            }
        }

        issdervice = bizTagIdLeaf.get(bizTag);
        return issdervice.getId();
    }

    @Override
    public int nextIntValue(String bizTag)
            throws DataAccessException {
        return getIdByBizTag(bizTag).intValue();
    }

    @Override
    public long nextLongValue(String bizTag)
            throws DataAccessException {
        return getIdByBizTag(bizTag);
    }

    @Override
    public String nextStringValue(String bizTag)
            throws DataAccessException {
        String s = Long.toString(getIdByBizTag(bizTag));
        int len = s.length();
        if (len < this.paddingLength) {
            StringBuilder sb = new StringBuilder(this.paddingLength);
            for (int i = 0; i < this.paddingLength - len; i++) {
                sb.append('0');
            }
            sb.append(s);
            s = sb.toString();
        }
        return s;
    }
}
