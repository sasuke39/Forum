package cn.bestzuo.zuoforum.exception;

public interface CommonError {
    public int getErrorCode();
    public String getErrorMsg();
    public CommonError setErrorMsg(String errorMsg);
}