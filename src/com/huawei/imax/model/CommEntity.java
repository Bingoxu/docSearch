/**   
 * @Title: commEntity.java 
 * @Package com.huawei.imax.model 
 * @Description: TODO 
 * @author xubin 
 * @date 2012-8-29 上午10:17:10 
 * @version V1.0   
 */
package com.huawei.imax.model;

import java.io.Serializable;

/**
 * @ClassName: commEntity
 * @Description: 生成索引的公共实体bean
 * @author xubin
 * @date 2012-8-29 上午10:17:10
 * 
 */
public class CommEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	public CommEntity() {
		super();
	}

	public CommEntity(String htmlPath, int flag, String time) {
		this.htmlPath = htmlPath;
		this.flag = flag;
		this.time = time;
	}

	/**
	 * @Fields title : html的标题信息
	 */
	private String title;
	/**
	 * @Fields content : html的页面内容
	 */
	private String content;
	/**
	 * @Fields htmlPath : html页面的路径
	 */
	private String htmlPath;
	/**
	 * @Fields flag : html页面的标志位(0表示新增，1表示更新，2表示删除，默认为0)
	 */
	private int flag;
	/** 
	* @Fields time : html创建时间
	*/ 
	private String time;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getHtmlPath() {
		return htmlPath;
	}

	public void setHtmlPath(String htmlPath) {
		this.htmlPath = htmlPath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
