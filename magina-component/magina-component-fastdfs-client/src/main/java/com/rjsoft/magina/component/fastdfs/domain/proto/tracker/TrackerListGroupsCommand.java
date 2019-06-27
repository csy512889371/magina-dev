package com.rjsoft.magina.component.fastdfs.domain.proto.tracker;

import com.rjsoft.magina.component.fastdfs.domain.fdfs.GroupState;
import com.rjsoft.magina.component.fastdfs.domain.proto.AbstractFdfsCommand;
import com.rjsoft.magina.component.fastdfs.domain.proto.tracker.internal.TrackerListGroupsRequest;
import com.rjsoft.magina.component.fastdfs.domain.proto.tracker.internal.TrackerListGroupsResponse;

import java.util.List;

/**
 * 列出组命令
 *
 *
 */
public class TrackerListGroupsCommand extends AbstractFdfsCommand<List<GroupState>> {

    public TrackerListGroupsCommand() {
        super.request = new TrackerListGroupsRequest();
        super.response = new TrackerListGroupsResponse();
    }

}
