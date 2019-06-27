package com.rjsoft.magina.component.fastdfs.domain.proto.tracker.internal;

import com.rjsoft.magina.component.fastdfs.domain.proto.CmdConstants;
import com.rjsoft.magina.component.fastdfs.domain.proto.FdfsRequest;
import com.rjsoft.magina.component.fastdfs.domain.proto.ProtoHead;

/**
 * 列出分组命令
 *
 *
 */
public class TrackerListGroupsRequest extends FdfsRequest {

    public TrackerListGroupsRequest() {
        head = new ProtoHead(CmdConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
    }
}
