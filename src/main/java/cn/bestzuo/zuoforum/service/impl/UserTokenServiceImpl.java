package cn.bestzuo.zuoforum.service.impl;

import cn.bestzuo.zuoforum.mapper.UserTokenMapper;
import cn.bestzuo.zuoforum.pojo.UserToken;
import cn.bestzuo.zuoforum.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    UserTokenMapper tokenMapper;

    @Override
    public Integer getUidByToken(String Token) {
        return tokenMapper.selectByUserToken(Token);
    }

    @Override
    public void insertUserToken(UserToken userToken) {
        tokenMapper.insert(userToken);
    }

    @Override
    public UserToken getToken(Integer uid) {
        return tokenMapper.selectByUserId(uid);
    }
}
