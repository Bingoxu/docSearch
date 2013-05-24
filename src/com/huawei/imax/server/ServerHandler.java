package com.huawei.imax.server;

import java.util.List;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.imax.index.IndexManager;
import com.huawei.imax.model.CommEntity;
import com.huawei.imax.model.CommResultInfo;
import com.huawei.imax.util.Constans;

/**
 * @ClassName: ServerHandler
 * @Description: 服务器端接收处理类
 * @author xubin
 * @date 2012-9-3 上午10:10:58
 * 
 */
public class ServerHandler extends IoHandlerAdapter {

	static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	
	public ServerHandler() {
	}

	// 当连接后打开时触发此方法，一般此方法与 sessionCreated 会被同时触发
	public void sessionOpened(IoSession session) throws Exception {
	}

	public void sessionClosed(IoSession session) {
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		CommResultInfo resultInfo = new CommResultInfo();
		if (message instanceof CommEntity) {
			logger.debug("服务端接收到：" + message);
			CommEntity info = (CommEntity) message;
			String result = IndexManager.saveorupdateIndexs(info);
			resultInfo.setResult(result);
			logger.debug("索引处理结果是：" + result);
		} else if (message instanceof String) {
			logger.debug("服务端接收到搜索条件是：" + message);
			String queryOnject = (String) message;
			List<CommEntity> result = IndexManager.queryDetailList(queryOnject);
			List<CommEntity> result_tit = IndexManager
					.queryDetailList_tit(queryOnject);
			List<CommEntity> result_con = IndexManager
					.queryDetailList_conn(queryOnject);
			if (result.size() > 0 || result_tit.size() > 0
					|| result_con.size() > 0) {
				resultInfo.setResult(Constans.HAS_MATCH_DATA);
			} else {
				resultInfo.setResult(Constans.NO_MATCH_DATA);
			}
			resultInfo.setList(result);
			resultInfo.setList_tit(result_tit);
			resultInfo.setList_con(result_con);
			logger.debug("标题和内容匹配查询返回的值有：" + result.size() + "条");
			logger.debug("标题匹配查询返回的值有：" + result_tit.size() + "条");
			logger.debug("内容匹配查询返回的值有：" + result_con.size() + "条");
		}
		session.write(resultInfo);
	}

	// 当接口中其他方法抛出异常未被捕获时触发此方法
	public void exceptionCaught(IoSession session, Throwable e)
			throws Exception {
	}

	// 当消息传送到客户端后触发
	public void messageSent(IoSession arg0, Object arg1) throws Exception {
	}

	// 当一个新客户端连接后触发此方法.
	public void sessionCreated(IoSession arg0) throws Exception {

	}

	// 当连接空闲时触发此方法.
	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {

	}
}
