package com.taobao.example.xp.model;


public abstract class Action {
    public String title;
    
    
    public Action(String title) {
        super();
        this.title = title;
    }
    
    public abstract void performAction();
    
    
}
