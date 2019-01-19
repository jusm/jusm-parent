package core.com.chidori.dao.mapper;

import core.com.chidori.model.UserWeChatInfo;
import core.com.chidori.model.UserWeChatInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserWeChatInfoMapper {
    int countByExample(UserWeChatInfoExample example);

    int deleteByExample(UserWeChatInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserWeChatInfo record);

    int insertSelective(UserWeChatInfo record);

    List<UserWeChatInfo> selectByExample(UserWeChatInfoExample example);

    UserWeChatInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserWeChatInfo record, @Param("example") UserWeChatInfoExample example);

    int updateByExample(@Param("record") UserWeChatInfo record, @Param("example") UserWeChatInfoExample example);

    int updateByPrimaryKeySelective(UserWeChatInfo record);

    int updateByPrimaryKey(UserWeChatInfo record);
}