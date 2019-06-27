package com.rjsoft.magina.component.fastdfs.domain.proto.tracker;

import com.rjsoft.magina.component.fastdfs.TestConstants;
import com.rjsoft.magina.component.fastdfs.domain.proto.tracker.internal.TrackerListGroupsRequest;
import org.junit.Test;

/**
 * 列举Groups请求
 * 
 *
 *
 */
public class TrackerListGroupsRequestTest {

    @Test
    public void testGetByteContent() {
        TrackerListGroupsRequest request = new TrackerListGroupsRequest();
        printRequest(request.getHeadByte(TestConstants.DEFAULT_CHARSET));
    }

    private void printRequest(byte[] request) {
        for (int i = 0; i < request.length; i++) {
            System.out.print(request[i]);
            System.out.print(" ");
        }
    }

}
