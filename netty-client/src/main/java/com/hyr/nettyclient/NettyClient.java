package com.hyr.nettyclient;

import com.hyr.nettycom.codec.MarshallingCodeCFactory;
import com.hyr.nettycom.entity.TranslatorData;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/*
 * @author hyr
 * @date 19-11-10-下午8:11
 * */
public class NettyClient {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8765;
    private Channel channel;
    private EventLoopGroup workGroup;
    private ChannelFuture channelFuture;

    public NettyClient() {
        this.connect(HOST, PORT);

    }

    private void connect(String host, int port) {
        //1.创建工作线程：用于处理实际业务
        workGroup = new NioEventLoopGroup();
        //2.辅助类
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    //缓冲区大小（自适应）
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    //缓存区 池化操作
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            channel.pipeline().addLast(new ClientHandler());
                            channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                        }
                    });

            channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("创建连接成功");
            //获取通道
            this.channel = channelFuture.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public TranslatorData sendData(TranslatorData data) {
        for (int i = 0; i < 10; i++) {
            TranslatorData request = new TranslatorData();
            request.setId("" + i);
            request.setName(i + "hello");
            request.setMessage("message:" + i);
            channel.writeAndFlush(request);
        }
        return null;
    }
    public void close(){
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }

    }
}
