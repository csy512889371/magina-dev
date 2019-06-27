package com.rjsoft.magina.component.fastdfs.service;

import com.rjsoft.magina.component.fastdfs.TestConstants;
import com.rjsoft.magina.component.fastdfs.domain.RandomTextFile;
import com.rjsoft.magina.component.fastdfs.domain.fdfs.FileInfo;
import com.rjsoft.magina.component.fastdfs.domain.fdfs.StorePath;
import com.rjsoft.magina.component.fastdfs.domain.proto.storage.DownloadByteArray;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 文件基础操作测试演示
 *
 *
 */
public class StorageClientBasicTest extends StorageClientTestBase {

    /**
     * 基本文件上传操作测试
     *
     * @throws IOException
     */
    @Test
    public void testGenerateStorageClient() throws IOException {
        LOGGER.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        StorePath path = storageClient.uploadFile(TestConstants.DEFAULT_GROUP, file.getInputStream(),
                file.getFileSize(), file.getFileExtName());
        assertNotNull(path);
        LOGGER.debug("上传文件 result={}", path);

        LOGGER.debug("##查询文件信息..##");
        FileInfo fileInfo = storageClient.queryFileInfo(path.getGroup(), path.getPath());
        LOGGER.debug("查询文件信息 result={}", fileInfo);

        LOGGER.debug("##下载文件..##");
        DownloadByteArray callback = new DownloadByteArray();
        byte[] content = storageClient.downloadFile(path.getGroup(), path.getPath(), callback);
        assertArrayEquals(content, file.toByte());

        LOGGER.debug("##上传从文件..##");
        RandomTextFile slaveFile = new RandomTextFile();
        // TODO 120*120会报错误，看是否可以从客户端截获此错误
        StorePath slavePath = storageClient.uploadSlaveFile(path.getGroup(), path.getPath(), slaveFile.getInputStream(),
                slaveFile.getFileSize(), "120_120", slaveFile.getFileExtName());
        LOGGER.debug("上传从文件 result={}", slavePath);

        LOGGER.debug("##删除主文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
        LOGGER.debug("##删除从文件..##");
        storageClient.deleteFile(slavePath.getGroup(), slavePath.getPath());

    }

    /**
     * 演示上传文件的时候Group可以为空
     *
     * @throws IOException
     */
    @Test
    public void testGenerateStorageClientWithGroupNull() throws IOException {

        LOGGER.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile();
        StorePath path = storageClient.uploadFile(null, file.getInputStream(), file.getFileSize(),
                file.getFileExtName());
        assertNotNull(path);
        LOGGER.debug("上传文件 result={}", path);

        LOGGER.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
    }

    /**
     * 验证文件部分下载
     *
     * @throws IOException
     */
    @Test
    public void testDownLoadSubFile() throws IOException {
        LOGGER.debug("##上传文件..##");
        RandomTextFile file = new RandomTextFile("this is a good day to make money!");
        StorePath path = storageClient.uploadFile(TestConstants.DEFAULT_GROUP, file.getInputStream(),
                file.getFileSize(), file.getFileExtName());
        assertNotNull(path);
        LOGGER.debug("上传文件 result={}", path);

        LOGGER.debug("##查询文件信息..##");
        FileInfo fileInfo = storageClient.queryFileInfo(path.getGroup(), path.getPath());
        LOGGER.debug("查询文件信息 result={}", fileInfo);

        LOGGER.debug("##下载文件..##");
        DownloadByteArray callback = new DownloadByteArray();
        // 下载部分文件
        byte[] content = storageClient.downloadFile(path.getGroup(),
                path.getPath(), 2, 8, callback);

        // 源文件当中截取
        byte[] subContent = new byte[8];
        System.arraycopy(file.toByte(), 2, subContent, 0, 8);
        // 验证是否一致
        assertArrayEquals(subContent, content);

        LOGGER.debug("##删除主文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());

    }

}
