
package com.zzy.grpc.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zzy.rpc.proto.user.message.UserProtoRequest;
import com.zzy.rpc.proto.user.message.UserProtoResponse;
import com.zzy.rpc.proto.user.service.UserSimpleServiceGrpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;

/**
 * 最简单的服务端代码（服务器阻塞，客户端阻塞）
 */
@Service
public class UserSimpleService extends UserSimpleServiceGrpc.UserSimpleServiceImplBase {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void getUser(UserProtoRequest request, StreamObserver<UserProtoResponse> responseObserver) {

        try {
            Integer id = request.getId();
            logger.info("id: {}", id);
            if (0 == id) {
                throw new RuntimeException("not find by id:" + id);
            }

            UserProtoResponse response = UserProtoResponse.newBuilder().setAge(18).setName("zzy").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).withCause(e).asException());
        }

    }

}
