package xyh.lixue.common.result;

import lombok.Data;

/**
 * @author XiangYida
 * @version 2019/5/4 13:39
 */
@Data
public class ApiResult<E> {
    //状态码
    private Integer code;

    //消息
    private String msg;

    //数据
    private E data;
}
