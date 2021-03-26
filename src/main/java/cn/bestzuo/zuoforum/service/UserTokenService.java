package cn.bestzuo.zuoforum.service;

import cn.bestzuo.zuoforum.pojo.UserToken;

public interface UserTokenService {
    Integer getUidByToken(String Token);

//    Boolean checkUserToken(String Token);

    void insertUserToken(UserToken userToken);

    UserToken getToken(Integer uid);
}
