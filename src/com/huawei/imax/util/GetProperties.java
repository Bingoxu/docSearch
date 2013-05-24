package com.huawei.imax.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
* @ClassName: GetProperties
* @Desc: 读取配置文件的信息
* @author zc
* @date 2012-8-7 上午11:06:03
*/

public class GetProperties {
	
	public static Properties prop= null;
	
	public static FileInputStream fileInupt = null;

    public static synchronized Properties getInstance(){
    	try {
    		if (prop ==null){
    			prop= new Properties();
    			//读取src目录下配置文件的方法
    			//fileInupt = Object.class.getResourceAsStream("../conf/config.properties");
    			//读取非src目录的其他文件夹下的配置文件
    			fileInupt = new FileInputStream("conf/config.properties");
    			prop.load(fileInupt);
    		}  
    	}catch (IOException e) {
			e.printStackTrace();
		}finally
        {
            //关闭文件
            if (fileInupt != null)
            {
                try
                {
                    fileInupt.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }  
    	return prop;
    }
}
