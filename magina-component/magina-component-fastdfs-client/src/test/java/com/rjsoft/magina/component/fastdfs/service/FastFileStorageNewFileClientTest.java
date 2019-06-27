package com.rjsoft.magina.component.fastdfs.service;

import com.rjsoft.magina.component.fastdfs.FastdfsTestApplication;
import com.rjsoft.magina.component.fastdfs.domain.RandomTextFile;
import com.rjsoft.magina.component.fastdfs.domain.fdfs.MetaData;
import com.rjsoft.magina.component.fastdfs.domain.fdfs.StorePath;
import com.rjsoft.magina.component.fastdfs.domain.upload.FastFile;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * FastFileStorageNewFileClientTest客户端
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FastdfsTestApplication.class)
public class FastFileStorageNewFileClientTest {

    @Autowired
    protected FastFileStorageClient storageClient;

    /**
     * 日志
     */
    protected static Logger LOGGER = LoggerFactory.getLogger(FastFileStorageNewFileClientTest.class);

    /**
     * 测试文件上传
     *
     * @return
     */
    @Test
    public void uploadFileOnly() {
        RandomTextFile file = new RandomTextFile();
        FastFile fastFile = new FastFile.Builder()
                .withFile(file.getInputStream(), file.getFileSize(), file.getFileExtName())
                .build();
        processUploadFileAndMetaData(fastFile);
    }

    /**
     * 传输文件和元数据
     *
     * @return
     */
    @Test
    public void uploadFileAndMetaData() {
        RandomTextFile file = new RandomTextFile();
        FastFile fastFile = new FastFile.Builder()
                .withFile(file.getInputStream(), file.getFileSize(), file.getFileExtName())
                //或者
                //.withMetaData(createMetaData())
                .withMetaData("hello", "world")
                .build();
        processUploadFileAndMetaData(fastFile);
    }

    /**
     * 传输文件和元数据,指定上传group
     *
     * @return
     */
    @Test
    public void uploadFileAndMetaDataWithGroup() {
        RandomTextFile file = new RandomTextFile();
        FastFile fastFile = new FastFile.Builder()
                .withFile(file.getInputStream(), file.getFileSize(), file.getFileExtName())
                .withMetaData(createMetaData())
                .toGroup("group1")
                .build();
        processUploadFileAndMetaData(fastFile);
    }


    /**
     * 传输文件和元数据
     *
     * @return
     */
    private void processUploadFileAndMetaData(FastFile fastFile) {
        LOGGER.debug("##上传文件..##");
        // 上传文件和Metadata
        StorePath path = storageClient.uploadFile(fastFile);
        Assert.assertNotNull(path);
        LOGGER.debug("上传文件路径{}", path);

        if (!fastFile.getMetaDataSet().isEmpty()) {
            // 验证获取MetaData
            LOGGER.debug("##获取Metadata##");
            Set<MetaData> fetchMetaData = storageClient.getMetadata(path.getGroup(), path.getPath());
            assertEquals(fetchMetaData, fastFile.getMetaDataSet());
        }

        LOGGER.debug("##删除文件..##");
        storageClient.deleteFile(path.getGroup(), path.getPath());
    }


    private Set<MetaData> createMetaData() {
        Set<MetaData> metaDataSet = new HashSet<MetaData>();
        metaDataSet.add(new MetaData("Author", "rjsoft"));
        metaDataSet.add(new MetaData("CreateDate", "2019-3-14"));
        return metaDataSet;
    }


}
