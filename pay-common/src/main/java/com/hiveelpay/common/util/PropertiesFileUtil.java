package com.hiveelpay.common.util;

import java.util.ResourceBundle;

/**
 * @Description: 属性文件工具类
 * @author wilson  wilson@hiveel.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: pay.hiveel.com
 */
public class PropertiesFileUtil {

	private ResourceBundle rb = null;

	public PropertiesFileUtil(String bundleFile) {
		rb = ResourceBundle.getBundle(bundleFile);
	}

	public String getValue(String key) {
		return rb.getString(key);
	}

	public static void main(String[] args) {


	}
}
