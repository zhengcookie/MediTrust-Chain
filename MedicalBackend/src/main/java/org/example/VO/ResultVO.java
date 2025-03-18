package org.example.VO;

import lombok.Data;

@Data
public class ResultVO<T> {
//    返回状态码
    private int code;
//    返回数据
    private T msg;
}
