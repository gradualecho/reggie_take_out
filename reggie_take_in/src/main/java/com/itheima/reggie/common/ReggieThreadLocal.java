package com.itheima.reggie.common;

public class ReggieThreadLocal {
    private static ThreadLocal<Long> threadLocal= new ThreadLocal();

    public static void setCurrentThreadLocal(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentThreadLocal(){
       return threadLocal.get();
    }
}
