package com.yangye.wechat.server;

import com.yangye.wechat.handler.ServerMessageHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WechatServer {

	private int port;

	public WechatServer(int port) {
		this.port = port;
	}

	public void start() {
		NioEventLoopGroup boss = null;
		NioEventLoopGroup worker = null;
		try {
			boss = new NioEventLoopGroup(1);
			worker = new NioEventLoopGroup(16);

			ServerBootstrap b = new ServerBootstrap();

			b.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel channel) {
							ChannelPipeline p = channel.pipeline();
							// 解析http协议
							p.addLast(new HttpServerCodec());
							p.addLast(new HttpObjectAggregator(64 * 1024));

							//
							p.addLast(new ChunkedWriteHandler());
							p.addLast(new WebSocketServerProtocolHandler("/ws"));
							p.addLast(new ServerMessageHandler());
						}
					});
			ChannelFuture bind = b.bind(port);
			ChannelFuture bindFuture = bind.sync();
			if (bindFuture.isSuccess()) {
				log.info("服务启动成功");
			}

			bindFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
