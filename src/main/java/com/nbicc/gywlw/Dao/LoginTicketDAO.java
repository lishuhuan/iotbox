package com.nbicc.gywlw.Dao;

import com.nbicc.gywlw.Model.LoginTicket;
import org.apache.ibatis.annotations.*;


/**
 * Created by BigMao on 2016/11/17.
 */
@Mapper
public interface LoginTicketDAO {
    String TABLE_NAME = "gywlw_login_ticket";
    String INSET_FIELDS = " user_id, expired, status, ticket, user_type ";
    String SELECT_FIELDS = " id, " + INSET_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSET_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket},#{userType})" })
    int addTicket(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ", TABLE_NAME, " set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

    @Delete({"delete from ", TABLE_NAME, "where ticket=#{ticket}"})
    void delete(@Param("ticket") String ticket);

    @Delete({"delete from ", TABLE_NAME, "where user_id=#{userId}"})
    void deleteByUserId(@Param("userId") String userId);
}
