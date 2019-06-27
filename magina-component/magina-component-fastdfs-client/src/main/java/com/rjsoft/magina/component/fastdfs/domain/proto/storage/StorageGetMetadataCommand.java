package com.rjsoft.magina.component.fastdfs.domain.proto.storage;

import com.rjsoft.magina.component.fastdfs.domain.fdfs.MetaData;
import com.rjsoft.magina.component.fastdfs.domain.proto.AbstractFdfsCommand;
import com.rjsoft.magina.component.fastdfs.domain.proto.storage.internal.StorageGetMetadataRequest;
import com.rjsoft.magina.component.fastdfs.domain.proto.storage.internal.StorageGetMetadataResponse;

import java.util.Set;

/**
 * 设置文件标签
 *
 *
 */
public class StorageGetMetadataCommand extends AbstractFdfsCommand<Set<MetaData>> {


    /**
     * 设置文件标签(元数据)
     *
     * @param groupName
     * @param path
     */
    public StorageGetMetadataCommand(String groupName, String path) {
        this.request = new StorageGetMetadataRequest(groupName, path);
        // 输出响应
        this.response = new StorageGetMetadataResponse();
    }

}
