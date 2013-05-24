/**   
 * @Title: TongbuClient.java 
 * @Package com.huawei.imax.client 
 * @Description: TODO 
 * @author xubin 
 * @date 2012-9-10 下午6:33:53 
 * @version V1.0   
 */
package com.huawei.imax.client;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.huawei.imax.model.CommEntity;
import com.huawei.imax.model.CommResultInfo;

/**
 * @ClassName: TongbuClient
 * @Description: mina客户端=====同步
 * @author xubin
 * @date 2012-9-10 下午6:33:53
 * 
 */
public class TongbuClient {
	NioSocketConnector connector = new NioSocketConnector();
	DefaultIoFilterChainBuilder chain = connector.getFilterChain();
	CommResultInfo info;

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Desc:初始化客户端信息
	 * </p>
	 * 
	 * @param message
	 * @throws UnsupportedEncodingException
	 */

	public TongbuClient(Object message, String url, int port) {
		chain.addLast("logging", new LoggingFilter()); // 添加日志过滤器
		chain.addLast("myChin", new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory()));
		if (message instanceof String) {
			connector.setHandler(new ClientHandler((String) message));
		} else if (message instanceof CommEntity) {
			connector.setHandler(new ClientHandler((CommEntity) message));
		}
		connector.setConnectTimeoutMillis(10000);
		SocketSessionConfig cfg = connector.getSessionConfig();
		cfg.setUseReadOperation(true);
		IoSession session = connector.connect(new InetSocketAddress(url, port))
				.awaitUninterruptibly().getSession();
		ReadFuture readFuture = session.read();
		readFuture.awaitUninterruptibly();
		this.info = (CommResultInfo) readFuture.getMessage();
		session.close(true);
		session.getService().dispose();
	}

	public CommResultInfo getResult() {
		return info;
	}
}
