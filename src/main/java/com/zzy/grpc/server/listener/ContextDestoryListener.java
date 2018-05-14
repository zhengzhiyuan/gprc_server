package com.zzy.grpc.server.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import com.zzy.grpc.server.config.GrpcServerConfig;

/**
 * spring容器销毁
 */
@Component
public class ContextDestoryListener implements ApplicationListener<ContextClosedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextDestoryListener.class);

    @Autowired
    private GrpcServerConfig grpcServerConfig;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        grpcServerConfig.shutdownGrpcServer();

        LOGGER.info("web app is stoped");
    }

}
