package com.amenah.tareq.project1;

import java.io.Serializable;

public class MyCustomPair implements Serializable {

    public String first;
    public String second;

    public MyCustomPair(String first, String second) {
        this.first = first;
        this.second = second;
    }
}
