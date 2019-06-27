package com.rjsoft.magina.component.fastdfs.domain.proto.tracker.internal;

import com.rjsoft.magina.component.fastdfs.domain.proto.CmdConstants;
import com.rjsoft.magina.component.fastdfs.domain.proto.FdfsRequest;
import com.rjsoft.magina.component.fastdfs.domain.proto.ProtoHead;

/**
 * 获取存储节点请求
 *
 *
 */
public class TrackerGetStoreStorageRequest extends FdfsRequest {

    private static final byte withoutGroupCmd = CmdConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;

    /**
     * 获取存储节点
     */
    public TrackerGetStoreStorageRequest() {
        super();
        this.head = new ProtoHead(withoutGroupCmd);
    }

}
