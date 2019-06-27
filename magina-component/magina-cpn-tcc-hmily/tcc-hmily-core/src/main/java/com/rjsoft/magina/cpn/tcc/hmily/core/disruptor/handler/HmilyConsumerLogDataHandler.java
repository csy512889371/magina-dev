package com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.handler;


import com.rjsoft.magina.cpn.tcc.hmily.common.enums.EventTypeEnum;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.DisruptorConsumerFactory;
import com.rjsoft.magina.cpn.tcc.hmily.common.bean.entity.HmilyTransaction;
import com.rjsoft.magina.cpn.tcc.hmily.core.concurrent.ConsistentHashSelector;
import com.rjsoft.magina.cpn.tcc.hmily.core.coordinator.HmilyCoordinatorService;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.AbstractDisruptorConsumerExecutor;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.event.HmilyTransactionEvent;

/**
 * this is disruptor consumer.
 *
 * @author xiaoyu(Myth)
 */
public class HmilyConsumerLogDataHandler extends AbstractDisruptorConsumerExecutor<HmilyTransactionEvent> implements DisruptorConsumerFactory {

    private ConsistentHashSelector executor;

    private final HmilyCoordinatorService coordinatorService;

    public HmilyConsumerLogDataHandler(final ConsistentHashSelector executor, final HmilyCoordinatorService coordinatorService) {
        this.executor = executor;
        this.coordinatorService = coordinatorService;
    }

    @Override
    public String fixName() {
        return "HmilyConsumerDataHandler";
    }

    @Override
    public AbstractDisruptorConsumerExecutor create() {
        return this;
    }

    @Override
    public void executor(final HmilyTransactionEvent event) {
        String transId = event.getHmilyTransaction().getTransId();
        executor.select(transId).execute(() -> {
            if (event.getType() == EventTypeEnum.SAVE.getCode()) {
                coordinatorService.save(event.getHmilyTransaction());
            } else if (event.getType() == EventTypeEnum.UPDATE_PARTICIPANT.getCode()) {
                coordinatorService.updateParticipant(event.getHmilyTransaction());
            } else if (event.getType() == EventTypeEnum.UPDATE_STATUS.getCode()) {
                final HmilyTransaction hmilyTransaction = event.getHmilyTransaction();
                coordinatorService.updateStatus(hmilyTransaction.getTransId(), hmilyTransaction.getStatus());
            } else if (event.getType() == EventTypeEnum.DELETE.getCode()) {
                coordinatorService.remove(event.getHmilyTransaction().getTransId());
            }
            event.clear();
        });
    }
}
