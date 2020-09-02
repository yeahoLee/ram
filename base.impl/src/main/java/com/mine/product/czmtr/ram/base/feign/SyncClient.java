package com.mine.product.czmtr.ram.base.feign;

import feign.Param;
import feign.RequestLine;

public interface SyncClient {
    @RequestLine("GET ?servicename={serviceName}&name=abs&password=918504E9BE8BD2907437E6263CE2299C")
    String getDataByServiceName(@Param("serviceName") String serviceName);
}
