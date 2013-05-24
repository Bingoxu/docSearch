package com.huawei.imax.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.huawei.imax.model.CommResultInfo;

/**
 * @ClassName: Client
 * @Description: 搜索客户端======异步
 * @author xubin
 * @date 2012-9-3 下午2:07:02
 * 
 */
public class SearchClient extends IoHandlerAdapter {
	
	private SocketConnector connector;
	/**
	 * @Fields result : 返回信息
	 */
	private static CommResultInfo result;

	/**
	 * @Fields message : 请求的参数（字符串为查询，EnableInfo为建立索引信息）
	 */
	private Object message;

	public SearchClient() {
	}

	/**
	 * <p>
	 * Desc:初始化连接信息
	 * </p>
	 * 
	 * @param message
	 * @param url
	 * @param port
	 */

	public SearchClient(Object message, String url, int port) {
		super();
		this.message = message;
		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logging", new LoggingFilter()); // 添加日志过滤器
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.setHandler(this);
		InetSocketAddress address = new InetSocketAddress(url, port);
		connector.connect(address);
	}

	/*
	 * <p>Desc:服务端返回值，客户端接受并处理 </p>
	 * 
	 * @param session
	 * 
	 * @param message
	 * 
	 * @throws Exception
	 * 
	 * @see
	 * org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache
	 * .mina.core.session.IoSession, java.lang.Object)
	 */

	public void messageReceived(IoSession session, Object ma) throws Exception {
		if (ma instanceof CommResultInfo) {
			result = (CommResultInfo) ma;
		}
		connector.dispose();
	}

	public void sessionCreated(IoSession session) throws Exception {
	}

	public void sessionOpened(IoSession session) throws Exception {
		// 1.客户端若没有信息传送，就关闭连接
		// 2.客户端有就传送信息
		if (message == null) {
			connector.dispose();
		} else {
			session.write(message);
		}

	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
	}

	public CommResultInfo getResult() {
		return result;
	}

}