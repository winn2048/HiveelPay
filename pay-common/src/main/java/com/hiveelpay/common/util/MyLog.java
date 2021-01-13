package com.hiveelpay.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wilson on 2018/8/27.
 */
public class MyLog extends com.hiveelpay.common.util.MyLogFace {

    private static final Map<String,MyLog> _pool = new HashMap<String,MyLog>();
    //----------
    public static synchronized Set<String> getLoggers() 	{
        return _pool.keySet();
    }
    public static synchronized void clearLoggers() 	{
        _pool.clear();
    }
    //----------
    public static synchronized MyLog getLog(String clz) 	{
        MyLog log = _pool.get(clz);
        if (log==null) {
            log = new MyLog();
            log.setName(clz);
            _pool.put(clz, log);
        }
        return log;
    }
    //----------
    public static MyLog getLog(Class<?> clz){
        return getLog(clz.getName());
    }

}
