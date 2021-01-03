package com.yangye.wechat.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.nio.charset.StandardCharsets;

public class ServerMessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
		System.out.println(textWebSocketFrame.text());

//		ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
//		buf.writeBytes(textWebSocketFrame.text().getBytes(StandardCharsets.UTF_8));

		ctx.writeAndFlush(new TextWebSocketFrame(textWebSocketFrame.text()));
	}
}
