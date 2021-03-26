package cn.bestzuo.zuoforum.mapper;

import cn.bestzuo.zuoforum.pojo.UserToken;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
public interface UserTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserToken record);

    int insertSelective(UserToken record);

    UserToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserToken record);

    int updateByPrimaryKey(UserToken record);

    int selectByUserToken(String token);

    UserToken selectByUserId(Integer uid);
}