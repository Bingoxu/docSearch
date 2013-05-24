/**   
 * @Title: htmlTest.java 
 * @Package com.huawei.imax.util 
 * @Description: TODO 
 * @author xubin 
 * @date 2012-8-29 上午11:27:07 
 * @version V1.0   
 */
package com.huawei.imax.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;

import com.huawei.imax.client.SearchClient;
import com.huawei.imax.client.TongbuClient;
import com.huawei.imax.index.IndexManager;
import com.huawei.imax.model.CommEntity;
import com.huawei.imax.model.CommResultInfo;

/**
 * @ClassName: htmlTest
 * @Description: TODO
 * @author xubin
 * @date 2012-8-29 上午11:27:07
 * 
 */
public class htmlTest {

	/**
	 * @throws IOException 
	 * @Title: main
	 * @Description: TODO
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) throws IOException {

		 String path1 = "E:\\templete\\ActionScript 3.0 编程.pdf";
//		 String path2 = "D:\\testSearch\\2.html";
//		 String path3 = "D:\\testSearch\\3.html";
//		 String path4 = "D:\\testSearch\\4.html";
//		 String path5 = "D:\\testSearch\\5.html";
//		 String path1 = "http://www.189works.com/article-73172-1.html";
//		 String path2 = "http://www.189works.com/article-96260-1.html";
//		 String path3 = "http://bbs.189works.com/thread-62536-1-1.html";
//		 String path4 = "http://wenku.baidu.com/view/22ad3f68011ca300a6c3909a.html";
//		 String path5 = "http://www.189works.com/article-96254-1.html";
		 CommEntity info = new CommEntity(path1, 0, "2011-08-21 11:11:11");
//		 CommEntity info1 = new CommEntity(path2, 0, "2011-02-21 11:11:11");
//		 CommEntity info2 = new CommEntity(path3, 0, "2011-03-21 11:11:11");
//		 CommEntity info3 = new CommEntity(path4, 0, "2011-04-21 11:11:11");
//		 CommEntity info4 = new CommEntity(path5, 0, "2011-05-21 11:11:11");
		
		 IndexManager.saveorupdateIndexs(info);
//		 IndexManager.saveorupdateIndexs(info1);
//		 IndexManager.saveorupdateIndexs(info3);
//		 IndexManager.saveorupdateIndexs(info2);
//		 IndexManager.saveorupdateIndexs(info4);
		 System.out.println("================ok==============");
		 
//		 new SearchClient(info,"127.0.0.1",9121);
//		 new SearchClient(info1,"127.0.0.1",9121);
//		 new SearchClient(info2,"127.0.0.1",9121);
//		 new SearchClient(info3,"127.0.0.1",9121);
//		 new SearchClient(info4,"127.0.0.1",9121);

//		List<CommEntity> list = new ArrayList<CommEntity>();
//		list = IndexManager.queryDetailList("是字符串的方法");
//		System.out.println("================" + list.size()
//				+ "====================");
//		 org.jsoup.nodes.Document d;
//		 try {
//			d = Jsoup.connect("http://192.168.8.12:8090/iMaxWiki/wikiDoc/ww.html").get();
//			d.title();
//			System.out.println(d.title());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		CommEntity info = new CommEntity(path1, 0, "2011-08-21 11:11:11");
//		new SearchClient(info,"192.168.8.12",9121);
		
//		List<CommEntity> list = new ArrayList<CommEntity>();
//		TongbuClient tc = new TongbuClient("基于","127.0.0.1",9121);
//		CommResultInfo info = tc.getResult();
//		if (info == null || info.getList().size() <= 0) {
//			System.out.println("无匹配结果集！");
//		} else {
//			list = info.getList();
//		}
//		System.out.println("==========");
	}

}
