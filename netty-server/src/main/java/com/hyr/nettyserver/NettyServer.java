package com.hyr.nettyserver;

import com.hyr.nettycom.codec.MarshallingCodeCFactory;
import com.hyr.nettycom.disruptor.RingBufferWorkerPoolFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/*
 * @author hyr
 * @date 19-11-10-下午8:11
 * */
public class NettyServer {
    public NettyServer() {
        //1.接收连个线程组，一个处理网络请求，另一个用于处理实际业务
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //缓冲区大小（自适应）
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓存区 池化操作
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            channel.pipeline().addLast(new ServerHandler());
                            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(8765).sync();
            System.out.println("Server startup");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅停机
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("Server ShutDown....");
        }

    }
}
