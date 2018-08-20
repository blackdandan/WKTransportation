package com.wk.wktransportation.rxbus;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/17 23:21
 * **********************************
 **/
public class Event {
    private int code = -1;///
    private Object object = null;
    public Event(int code,Object o){
        this.code = code;
        this.object = o;
    }
    public int code(){
        return code;
    }

    public Object object() {
        return object;
    }
}
