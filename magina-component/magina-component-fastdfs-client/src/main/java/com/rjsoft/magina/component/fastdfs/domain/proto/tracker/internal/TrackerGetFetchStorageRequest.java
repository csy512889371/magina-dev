package com.rjsoft.magina.component.fastdfs.domain.proto.tracker.internal;

import com.rjsoft.magina.component.fastdfs.domain.proto.OtherConstants;
import org.apache.commons.lang3.Validate;

import com.rjsoft.magina.component.fastdfs.domain.proto.CmdConstants;
import com.rjsoft.magina.component.fastdfs.domain.proto.FdfsRequest;
import com.rjsoft.magina.component.fastdfs.domain.proto.ProtoHead;
import com.rjsoft.magina.component.fastdfs.domain.proto.mapper.DynamicFieldType;
import com.rjsoft.magina.component.fastdfs.domain.proto.mapper.FdfsColumn;

/**
 * 获取源服务器
 *
 *
 */
public class TrackerGetFetchStorageRequest extends FdfsRequest {

    private static final byte fetchCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ONE;
    private static final byte updateCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;

    /**
     * 组名
     */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /**
     * 路径名
     */
    @FdfsColumn(index = 1, dynamicField = DynamicFieldType.allRestByte)
    private String path;

    /**
     * 获取文件源服务器
     *
     * @param groupName
     * @param path
     */
    public TrackerGetFetchStorageRequest(String groupName, String path, boolean toUpdate) {
        Validate.notBlank(groupName, "分组不能为空");
        Validate.notBlank(path, "文件路径不能为空");
        this.groupName = groupName;
        this.path = path;
        if (toUpdate) {
            head = new ProtoHead(updateCmd);
        } else {
            head = new ProtoHead(fetchCmd);
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public String getPath() {
        return path;
    }

}
