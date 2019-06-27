
package com.rjsoft.magina.component.id.idleaf.service.impl;

import com.rjsoft.magina.component.id.idleaf.idsegment.entity.IdSegmentEntity;
import com.rjsoft.magina.component.id.idleaf.idsegment.service.IdSegmentService;
import com.rjsoft.magina.component.id.idleaf.service.IdLeafService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunff
 */

@Slf4j
public class MysqlIdLeafServiceImpl implements IdLeafService {

    private IdSegmentService idSegmentService;

    // 创建线程池
    private ExecutorService taskExecutor;

    private volatile IdSegmentEntity[] segment = new IdSegmentEntity[2]; // 这两段用来存储每次拉升之后的最大值
    private volatile boolean sw;
    private AtomicLong currentId;
    private ReentrantLock lock = new ReentrantLock(); // 功能性严重bug #5 一个实例一把锁
    private volatile FutureTask<Boolean> asynLoadSegmentTask = null;

    private String bizTag;

    private boolean asynLoadingSegment;

    public MysqlIdLeafServiceImpl(IdSegmentService idSegmentService) {
        this.idSegmentService = idSegmentService;
    }

    public MysqlIdLeafServiceImpl(IdSegmentService idSegmentService, String bizTag) {
        this.idSegmentService = idSegmentService;
        this.bizTag = bizTag;
    }

    public void setTaskExecutor(ExecutorService taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void init() {
        if (this.bizTag == null) {
            throw new RuntimeException("bizTag must be not null");
        }

        if (taskExecutor == null) {
            taskExecutor = Executors.newSingleThreadExecutor();
        }
        segment[0] = doUpdateNextSegment(bizTag);
        // segment[1] = doUpdateNextSegment(bizTag);
        setSw(false);
        currentId = new AtomicLong(segment[index()].getMinId()); // 初始id
        log.info("init run success...");
    }


    private Long asynGetId2() {

        if (segment[index()].getMiddleId() <= currentId.longValue() && isNotLoadOfNextsegment()
                && asynLoadSegmentTask == null) {
            try {
                lock.lock();
                if (segment[index()].getMiddleId() <= currentId.longValue()) {
                    // 前一段使用了50%

                    asynLoadSegmentTask = new FutureTask<>(new Callable<Boolean>() {

                        @Override
                        public Boolean call() throws Exception {
                            final int currentIndex = reIndex();
                            segment[currentIndex] = doUpdateNextSegment(bizTag);
                            // System.out.println("异步job执行完毕");
                            return true;
                        }

                    });
                    taskExecutor.submit(asynLoadSegmentTask);
                    System.out.println("init asynLoadSegmentTask...，taskExecutor=" + taskExecutor.toString());
                }

            } finally {
                lock.unlock();
            }
        }

        if (segment[index()].getMaxId() <= currentId.longValue()) {
            try {
                lock.lock();
                if (segment[index()].getMaxId() <= currentId.longValue()) {

					/*
                     * final int currentIndex = index(); segment[currentIndex] =
					 * doUpdateNextSegment(bizTag);
					 */
                    boolean loadingResult = false;
                    try {
                        loadingResult = asynLoadSegmentTask.get(500, TimeUnit.MILLISECONDS);
                        if (loadingResult) {
                            setSw(!isSw()); // 切换
                            currentId = new AtomicLong(segment[index()].getMinId()); // 进行切换
                            asynLoadSegmentTask = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //System.out.println("异常则设置asynLoadSegmentTask=null");
                        loadingResult = false;
                        asynLoadSegmentTask = null;
                    }
                    if (!loadingResult) {
                        while (isNotLoadOfNextsegment()) {
                            // 强制同步切换
                            final int currentIndex = reIndex();
                            segment[currentIndex] = doUpdateNextSegment(bizTag);
                        }
                        setSw(!isSw()); // 切换
                        currentId = new AtomicLong(segment[index()].getMinId()); // 进行切换

                    }
                }
            } finally {
                lock.unlock();
            }
        }

        return currentId.incrementAndGet();

    }

    private boolean isNotLoadOfNextsegment() {
        if (segment[reIndex()] == null) {
            return true;
        }
        if (segment[reIndex()].getMinId() < segment[index()].getMinId()) {
            return true;
        }
        return false;
    }

    private long synGetId2() {
        if (segment[index()].getMiddleId() <= currentId.longValue() && isNotLoadOfNextsegment()) { // 需要加载了
            try {
                lock.lock();
                if (segment[index()].getMiddleId() <= currentId.longValue() && isNotLoadOfNextsegment()) {
                    // 使用50%以上，并且没有加载成功过，就进行加载
                    final int currentIndex = reIndex();
                    segment[currentIndex] = doUpdateNextSegment(bizTag);
                }
            } finally {
                lock.unlock();
            }
        }

        if (segment[index()].getMaxId() <= currentId.longValue()) { // 需要进行切换了
            try {
                lock.lock();
                if (segment[index()].getMaxId() <= currentId.longValue()) {
                    while (isNotLoadOfNextsegment()) {
                        // 使用50%以上，并且没有加载成功过，就进行加载,直到在功
                        final int currentIndex = reIndex();
                        segment[currentIndex] = doUpdateNextSegment(bizTag);
                    }
                    setSw(!isSw()); // 切换
                    currentId = new AtomicLong(segment[index()].getMinId()); // 进行切换

                }

            } finally {
                lock.unlock();
            }
        }
        return currentId.incrementAndGet();

    }

    @Override
    public Long getId() {
        if (asynLoadingSegment) {
            return asynGetId2();
        } else {
            return synGetId2();
        }
    }

    private boolean isSw() {
        return sw;
    }

    private void setSw(boolean sw) {
        this.sw = sw;
    }

    private int index() {
        if (isSw()) {
            return 1;
        } else {
            return 0;
        }
    }

    private int reIndex() {
        if (isSw()) {
            return 0;
        } else {
            return 1;
        }
    }

    private IdSegmentEntity doUpdateNextSegment(String bizTag) {
        try {
            return updateId(bizTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private IdSegmentEntity updateId(String bizTag) throws Exception {
        return idSegmentService.updateId(bizTag);
    }


    public void setBizTag(String bizTag) {
        this.bizTag = bizTag;
    }


    public void setAsynLoadingSegment(boolean asynLoadingSegment) {
        this.asynLoadingSegment = asynLoadingSegment;
    }


}
