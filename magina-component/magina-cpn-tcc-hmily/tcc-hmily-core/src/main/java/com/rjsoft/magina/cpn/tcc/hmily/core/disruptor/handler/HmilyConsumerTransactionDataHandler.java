package com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.handler;


import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.DisruptorConsumerFactory;
import com.rjsoft.magina.cpn.tcc.hmily.core.disruptor.AbstractDisruptorConsumerExecutor;
import com.rjsoft.magina.cpn.tcc.hmily.core.service.HmilyTransactionHandlerAlbum;

/**
 * HmilyTransactionHandler.
 * About the processing of a rotation function.
 *
 * @author chenbin sixh
 */
public class HmilyConsumerTransactionDataHandler extends AbstractDisruptorConsumerExecutor<HmilyTransactionHandlerAlbum> implements DisruptorConsumerFactory {


    @Override
    public String fixName() {
        return "HmilyConsumerTransactionDataHandler";
    }

    @Override
    public AbstractDisruptorConsumerExecutor create() {
        return this;
    }

    @Override
    public void executor(final HmilyTransactionHandlerAlbum data) {
        data.run();
    }
}

