package xyh.lixue.common.enums;

/**
 * @author XiangYida
 * @version 2019/5/4 14:12
 */
public enum ResultEnum {
    UNKONW_ERROR(500, "内部错误"),
    SUCCSEE(200, "成功"),
    NULL_POINTER_ERR(501,"空指针异常"),
    AUTHORITY_ERR(401,"权限错误");
    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
