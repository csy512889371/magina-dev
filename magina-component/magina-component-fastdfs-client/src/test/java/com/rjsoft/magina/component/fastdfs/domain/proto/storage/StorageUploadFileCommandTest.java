package com.rjsoft.magina.component.fastdfs.domain.proto.storage;

import com.rjsoft.magina.component.fastdfs.TestConstants;
import com.rjsoft.magina.component.fastdfs.domain.proto.StorageCommandTestBase;
import org.junit.Test;

/**
 * 文件上传命令测试
 * 
 *
 *
 */
public class StorageUploadFileCommandTest extends StorageCommandTestBase {

    /**
     * 文件上传测试
     */
    @Test
    public void testStorageUploadFileCommand() {
        // 非append模式
        execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, false);
    }

    @Test
    public void testStorageUploadFileCommandByAppend() {
        // append模式
        execStorageUploadFileCommand(TestConstants.CAT_IMAGE_FILE, true);
    }

}
