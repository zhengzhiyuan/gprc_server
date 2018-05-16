
package com.zzy.grpc.server.config;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * gprc配置
 */
@Configuration
public class GrpcServerConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Server server;

    private ExecutorService executorService;

    @Autowired
    private ServiceProperties serviceProperties;

    @Autowired
    private List<BindableService> services;

    private Server getServer() throws IOException {
        final int SRERVER_PORT = serviceProperties.getGrpc().getPort();
        ServerBuilder<?> builder = ServerBuilder.forPort(SRERVER_PORT);
        for (BindableService service : services) {
            builder.addService(service);
            logger.info("Adding rpc service: {}", service.getClass());
        }
        Server server = builder.build();
        return server;
    }

    private ExecutorService getGrpcServerThread() {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) threadPool;
        poolExecutor.setThreadFactory(getThreadFactory("grpcServerThread", null));
        return poolExecutor;
    }

    @PostConstruct
    private void setup() throws IOException {
        this.server = getServer();
        this.executorService = getGrpcServerThread();
    }

    /**
     * 启动grpc服务
     */
    public void startGrpcServer() {
        executorService.execute(() -> {
            try {
                server.start();
                logger.info("grpc started port: {}", server.getPort());
                server.awaitTermination();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Throwable e) {
                logger.error("Fail to start grpc server", e);
            }

        });
    }

    /**
     * 关闭grpc服务
     */
    public void shutdownGrpcServer() {
        server.shutdown();
        logger.info("grpc server shutdown,port:{}", server.getPort());

    }

    /**
     * 线程工程，设置线程名，异常处理
     * 
     * @param poolName
     * @param handler
     * @return
     */
    private ThreadFactory getThreadFactory(String poolName, UncaughtExceptionHandler handler) {
        return new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {

                Thread thread = new Thread(r);
                // thread名
                thread.setName(poolName + "-" + atomicInteger.getAndIncrement());

                // 异常处理机制
                if (handler != null) {
                    thread.setUncaughtExceptionHandler(handler);
                } else {
                    thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
                }
                return thread;
            }
        };
    }
}
