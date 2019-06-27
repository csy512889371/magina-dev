package com.rjsoft.magina.component.fastdfs.domain.proto;

import com.rjsoft.magina.component.fastdfs.domain.conn.Connection;

/**
 * Fdfs交易命令抽象
 *
 *
 */
public interface FdfsCommand<T> {

    /**
     * 执行交易
     */
    public T execute(Connection conn);

}
