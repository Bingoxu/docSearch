package com.huawei.imax.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.imax.util.GetProperties;

/**
 * @ClassName: DocServer
 * @Description: TODO
 * @author xubin
 * @date 2012-9-3 上午10:57:41
 * 
 */
public class DocServer {
	static Logger log = LoggerFactory.getLogger(DocServer.class);
	private static DocServer mainServer = null;
	private SocketAcceptor acceptor = new NioSocketAcceptor();
	private DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
	private int bindPort = (GetProperties.getInstance().getProperty("port") != null && GetProperties
			.getInstance().getProperty("port").equals("")) ? Integer
			.parseInt(GetProperties.getInstance().getProperty("port")) : 9121;

	public static DocServer getInstances() {
		if (null == mainServer) {
			mainServer = new DocServer();
		}
		return mainServer;
	}

	private DocServer() {
		chain.addLast("logging", new LoggingFilter()); // 添加日志过滤器
		chain.addLast("myChin", new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory()));
		acceptor.setHandler(new ServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
		try {
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			log.error(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * @Title: main
	 * @Description: TODO
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		DocServer.getInstances();
		log.info("========Search Service Started!========");
	}

}
