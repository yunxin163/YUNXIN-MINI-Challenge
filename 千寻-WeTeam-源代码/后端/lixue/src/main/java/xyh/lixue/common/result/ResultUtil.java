package xyh.lixue.common.result;

import xyh.lixue.common.enums.ResultEnum;

/**
 * @author XiangYida
 * @version 2019/5/4 13:43
 */
public class ResultUtil{

    //success
    public static ApiResult success(){
        ApiResult apiResult=new ApiResult();
        apiResult.setCode(ResultEnum.SUCCSEE.getCode());
        apiResult.setMsg(ResultEnum.SUCCSEE.getMsg());
        return apiResult;
    }

    public static <T> ApiResult<T> success(T t){
        ApiResult<T> apiResult=new ApiResult<>();
        apiResult.setCode(ResultEnum.SUCCSEE.getCode());
        apiResult.setMsg(ResultEnum.SUCCSEE.getMsg());
        apiResult.setData(t);
        return apiResult;
    }

    public static <T> ApiResult<T> success(T t,ResultEnum resultEnum){
        ApiResult<T> apiResult=new ApiResult<>();
        apiResult.setCode(resultEnum.getCode());
        apiResult.setMsg(resultEnum.getMsg());
        apiResult.setData(t);
        return apiResult;
    }
    public static <T> ApiResult<T> success(T t,String msg){
        ApiResult<T> apiResult=new ApiResult<>();
        apiResult.setCode(ResultEnum.SUCCSEE.getCode());
        apiResult.setMsg(msg);
        apiResult.setData(t);
        return apiResult;
    }

    //fail
    public static ApiResult failed(){
        ApiResult apiResult=new ApiResult();
        apiResult.setCode(ResultEnum.UNKONW_ERROR.getCode());
        apiResult.setMsg(ResultEnum.UNKONW_ERROR.getMsg());
        return apiResult;
    }

    public static ApiResult failed(ResultEnum resultEnum){
        ApiResult apiResult=new ApiResult();
        apiResult.setCode(resultEnum.getCode());
        apiResult.setMsg(resultEnum.getMsg());
        return apiResult;
    }

}
