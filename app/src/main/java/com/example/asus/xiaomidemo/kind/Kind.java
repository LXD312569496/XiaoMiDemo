package com.example.asus.xiaomidemo.kind;

/**
 * Created by asus on 2017/10/25.
 */

public class Kind {
    String name;//每个种类的名字
    int category;//种类的代号
    int icon;//图片的id

    public Kind(String name, int category, int icon) {
        this.name = name;
        this.category = category;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
