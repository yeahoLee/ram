package com.mine.product.czmtr.ram.main.util;

import java.lang.reflect.Field;

import org.jboss.netty.channel.ChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.remoting.transport.netty.NettyClient;

@Component
public class DubboShutdownListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DubboShutdownListener.class);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("Application close. Try to close Dubbo");
        //先释放dubbo所占用的资源
        ProtocolConfig.destroyAll();
        //用反射释放NettyClient所占用的资源, 以避免不能优雅shutdown的问题
        releaseNettyClientExternalResources();
    }

    private void releaseNettyClientExternalResources() {
        try {
            Field field = NettyClient.class.getDeclaredField("channelFactory");
            field.setAccessible(true);
            ChannelFactory channelFactory = (ChannelFactory) field.get(NettyClient.class);
            channelFactory.releaseExternalResources();
            field.setAccessible(false);
            logger.info("Release NettyClient's external resources");
        } catch (Exception e) {
            logger.error("Release NettyClient's external resources error", e);
        }
    }
}
