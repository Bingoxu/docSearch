package com.huawei.imax.util;

/**
 * @ClassName: Constans
 * @Description: 常量定义
 * @author xubin
 * @date 2012-8-30 上午10:16:06
 * 
 */
public class Constans {

	// 索引创建返回结果
	public static final String INDEX_RESLUT_NULL = "传递的html文件不存在,请确认该文件存在！";
	public static final String INDEX_RESLUT_SAVE_SUCCESS = "索引处理成功！";
	public static final String INDEX_RESLUT_NO_STATE = "该状态的能力不能建立索引！";

	// 索引字段
	public static final String FIELD_TITLE = "title"; // html标题
	public static final String FIELD_CONTENT = "content";// html内容
	public static final String FIELD_HTMLPATH = "htmlPath"; // html路径
	public static final String FIELD_TIME = "time"; // html创建时间

	//查询结果
	public static final String HAS_MATCH_DATA = "hasMatchDta"; // 按关键字搜索有结果集
	public static final String NO_MATCH_DATA = "noMatchData";// 按关键字搜索没有匹配的结果集
}
