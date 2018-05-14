package com.zzy.grpc.server.listener;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.zzy.grpc.server.config.GrpcServerConfig;

/**
 * spring容器初始化
 */
@Component
public class ContextInitListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextInitListener.class);

    private AtomicBoolean initFlag = new AtomicBoolean(true);

    @Autowired
    private Environment env;

    @Autowired
    private GrpcServerConfig grpcServerConfig;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (initFlag.getAndSet(false) != true) {
            grpcServerConfig.startGrpcServer();
            return;
        }

    }

}
