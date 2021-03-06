package com.rjsoft.magina.component.fastdfs.domain.proto.storage;

import java.io.InputStream;

import com.rjsoft.magina.component.fastdfs.domain.proto.AbstractFdfsCommand;
import com.rjsoft.magina.component.fastdfs.domain.proto.FdfsResponse;
import com.rjsoft.magina.component.fastdfs.domain.proto.storage.internal.StorageModifyRequest;

/**
 * 文件修改命令
 *
 *
 */
public class StorageModifyCommand extends AbstractFdfsCommand<Void> {

    /**
     * 文件修改命令
     *
     * @param path
     * @param inputStream
     * @param fileSize
     * @param fileOffset
     */
    public StorageModifyCommand(String path, InputStream inputStream, long fileSize, long fileOffset) {
        super();
        this.request = new StorageModifyRequest(inputStream, fileSize, path, fileOffset);
        // 输出响应
        this.response = new FdfsResponse<Void>() {
            // default response
        };
    }

}
