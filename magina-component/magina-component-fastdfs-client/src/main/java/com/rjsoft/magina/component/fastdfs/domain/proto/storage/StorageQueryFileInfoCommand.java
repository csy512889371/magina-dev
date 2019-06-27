package com.rjsoft.magina.component.fastdfs.domain.proto.storage;

import com.rjsoft.magina.component.fastdfs.domain.fdfs.FileInfo;
import com.rjsoft.magina.component.fastdfs.domain.proto.AbstractFdfsCommand;
import com.rjsoft.magina.component.fastdfs.domain.proto.FdfsResponse;
import com.rjsoft.magina.component.fastdfs.domain.proto.storage.internal.StorageQueryFileInfoRequest;

/**
 * 文件删除命令
 *
 *
 */
public class StorageQueryFileInfoCommand extends AbstractFdfsCommand<FileInfo> {

    /**
     * 文件上传命令
     *
     * @param groupName
     * @param path
     */
    public StorageQueryFileInfoCommand(String groupName, String path) {
        super();
        this.request = new StorageQueryFileInfoRequest(groupName, path);
        // 输出响应
        this.response = new FdfsResponse<FileInfo>() {
            // default response
        };
    }

}
