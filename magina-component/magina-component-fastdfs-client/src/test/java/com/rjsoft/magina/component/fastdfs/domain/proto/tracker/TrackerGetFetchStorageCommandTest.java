package com.rjsoft.magina.component.fastdfs.domain.proto.tracker;

import com.rjsoft.magina.component.fastdfs.domain.fdfs.StorageNodeInfo;
import com.rjsoft.magina.component.fastdfs.domain.fdfs.StorePath;
import com.rjsoft.magina.component.fastdfs.domain.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 获取源服务器
 *
 *
 */
public class TrackerGetFetchStorageCommandTest extends StorageCommandTestBase {

    /**
     * 获取源服务器
     */
    @Test
    public void testTrackerGetFetchStorageCommand() {

        // 上传文件
        StorePath path = uploadDefaultFile();

        // 获取源服务器
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(path.getGroup(), path.getPath(),
                false);
        StorageNodeInfo client = executeTrackerCmd(command);
        LOGGER.debug("----获取源服务器-----");
        LOGGER.debug(client.toString());
    }
}
