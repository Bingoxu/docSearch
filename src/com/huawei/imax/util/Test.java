/**   
* @Title: Test.java 
* @Package com.huawei.imax.util 
* @Description: TODO 
* @author xubin 
* @date 2012-9-18 下午4:33:15 
* @version V1.0   
*/
package com.huawei.imax.util;

import java.io.StringReader; 
import java.util.ArrayList; 
import java.util.List; 
 
import org.apache.lucene.analysis.Analyzer; 
import org.apache.lucene.analysis.TokenStream; 
import org.apache.lucene.analysis.tokenattributes.TermAttribute; 
import org.wltea.analyzer.dic.Dictionary; 
import org.wltea.analyzer.lucene.IKAnalyzer; 

/** 
 * @ClassName: Test 
 * @Description: TODO 
 * @author xubin 
 * @date 2012-9-18 下午4:33:15 
 *  
 */
public class Test {

	/** 
	 * @Title: main 
	 * @Description: TODO
	 * @param @param args    设定文件 
	 * @return void    返回类型 
	 * @throws 
	 */
	public static void main(String[] args) throws Exception {
//		String text = "的咳嗽咖啡豆拍摄方面是字符串的方法";  
 		List <String> strList=new ArrayList <String>(); 
  	        strList.add("ipad2"); 
                 Dictionary.loadExtendWords(strList); 
  
 		 
 		 
 	        String text2 = "的咳嗽咖啡豆拍摄方面是字符串的方法";   
 	        Test.testAnalyzer(new IKAnalyzer(), text2); // 使用IKAnalyzer，词库分词   
 	} 
 	 
 	 
 	  /**  
 	     * 使用指定的分词器对指定的文本进行分词，并打印结果  
 	     *  
 	     * @param analyzer  
 	     * @param text  
 	     * @throws Exception  
 	     */   
 	    public static void testAnalyzer(Analyzer analyzer, String text) throws Exception {   
 	          
 	           System.out.println("当前使用的分词器：" + analyzer.getClass());   
 	            
 	           TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));   
 	           tokenStream.addAttribute(TermAttribute.class);   
 	 
 	           while (tokenStream.incrementToken()) {   
 	                  TermAttribute termAttribute = tokenStream.getAttribute(TermAttribute.class);   
 	                  System.out.println(termAttribute.term());   
 	           }   
 	    }   

}
