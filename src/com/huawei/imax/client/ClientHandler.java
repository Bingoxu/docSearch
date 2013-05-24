package com.huawei.imax.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.imax.model.CommEntity;
import com.huawei.imax.model.CommResultInfo;


/** 
* @ClassName: ClientHandler 
* @Description: mina客户端的处理 
* @author xubin 
* @date 2012-9-11 上午9:37:14 
*  
*/
@Deprecated
public class ClientHandler extends IoHandlerAdapter {
	static Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	private CommEntity info = null;
	private String queryContext;
	private CommResultInfo result;

	public ClientHandler() {
	}

	public ClientHandler(CommEntity info) {
		this.info = info;
	}

	public ClientHandler(String queryContext) {
		this.queryContext = queryContext;
	}

	public void sessionOpened(IoSession session) throws Exception {
		if (info != null) {
			session.write(info);
		} else if (queryContext != null && !queryContext.equals("")) {
			session.write(queryContext);
		}
	}

	public void sessionClosed(IoSession session) {
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof CommResultInfo) {
			result = (CommResultInfo) message;
		}
	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		logger.debug("客户端已经向服务器发送了：" + (String) arg1);
	}

	public CommResultInfo getResult() {
		return result;
	}
}
