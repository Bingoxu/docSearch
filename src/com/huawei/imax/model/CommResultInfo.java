/**   
 * @Title: CommResultInfo.java 
 * @Package com.huawei.imax.model 
 * @Description: TODO 
 * @author xubin 
 * @date 2012-9-3 上午10:12:29 
 * @version V1.0   
 */
package com.huawei.imax.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: CommResultInfo
 * @Description:
 * @author xubin
 * @date 2012-9-3 上午10:12:29
 * 
 */
public class CommResultInfo implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -9131708614028019262L;

	/**
	 * @Fields result : 索引增删改操作后返回的状态
	 */
	private String result;

	/**
	 * @Fields list : 标题和内容匹配查询返回的结果集
	 */
	private List<CommEntity> list = new ArrayList<CommEntity>();

	/**
	 * @Fields list_tit : 标题匹配查询返回的结果集
	 */
	private List<CommEntity> list_tit = new ArrayList<CommEntity>();

	/**
	 * @Fields list_con : 内容匹配查询返回的结果集
	 */
	private List<CommEntity> list_con = new ArrayList<CommEntity>();

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<CommEntity> getList() {
		return list;
	}

	public void setList(List<CommEntity> list) {
		this.list = list;
	}

	public List<CommEntity> getList_tit() {
		return list_tit;
	}

	public void setList_tit(List<CommEntity> list_tit) {
		this.list_tit = list_tit;
	}

	public List<CommEntity> getList_con() {
		return list_con;
	}

	public void setList_con(List<CommEntity> list_con) {
		this.list_con = list_con;
	}

}
