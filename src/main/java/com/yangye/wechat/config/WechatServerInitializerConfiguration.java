package com.yangye.wechat.config;

import com.yangye.wechat.server.WechatServer;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatServerInitializerConfiguration implements SmartLifecycle {


	@Override
	public void start() {
		WechatServer wechatServer = new WechatServer(19090);
		wechatServer.start();
	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isRunning() {
		// 只有false时，start方法才会执行
		return false;
	}
}
