
package com.zzy.grpc.server.test.client;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.grpc.server.config.ServiceProperties;
import com.zzy.grpc.server.test.config.DefaultConfig;
import com.zzy.rpc.proto.user.message.UserProtoRequest;
import com.zzy.rpc.proto.user.message.UserProtoResponse;
import com.zzy.rpc.proto.user.service.UserSimpleServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableConfigurationProperties(ServiceProperties.class)
@ContextConfiguration(classes = { DefaultConfig.class })
@SpringBootTest
public class UserSimpleClient {

    private static Logger logger = LoggerFactory.getLogger(UserSimpleClient.class);

    @Autowired
    private ServiceProperties serviceProperties;

    @Test
    public void test() throws InterruptedException {
        String host = serviceProperties.getGrpc().getHost();
        Integer port = serviceProperties.getGrpc().getPort();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();

        UserSimpleServiceGrpc.UserSimpleServiceBlockingStub blockingStub = UserSimpleServiceGrpc.newBlockingStub(channel);

        UserProtoRequest request = UserProtoRequest.newBuilder().setId(0).build();
        try {
            UserProtoResponse response = blockingStub.getUser(request);
            logger.info("name:{}, age:{}", response.getName(), response.getAge());
        } catch (StatusRuntimeException ex) {
            if (ex.getStatus().getCode() == Code.NOT_FOUND) {
                logger.error("not find user");
            } else {
                logger.error("grpc error status code:{}", ex.getStatus().getCode());
            }
        }

        boolean success = channel.shutdown().awaitTermination(3, TimeUnit.SECONDS);
        if (success) {
            logger.info("grpc client has been closed.");
        } else {
            logger.error("close grpc client timeout.");
        }
    }

}
