package com.hundsun.boss.modules.sys.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.sys.entity.User;

/**
 * 用户DAO接口
 */
@Repository
public class UserDao extends HibernateDao<User> {

    public List<User> findAllList() {
        return find("from User where delFlag=:p1 order by id", new Parameter(User.DEL_FLAG_NORMAL));
    }

    public User findByLoginName(String loginName) {
        return getByHql("from User where loginName = :p1 and delFlag = :p2", new Parameter(loginName, User.DEL_FLAG_NORMAL));
    }

    public int updatePasswordById(String newPassword, String id) {
        return update("update User set password=:p1 where id = :p2", new Parameter(newPassword, id));
    }

    public int updateLoginInfo(String loginIp, Date loginDate, String id) {
        return update("update User set loginIp=:p1, loginDate=:p2 where id = :p3", new Parameter(loginIp, loginDate, id));
    }

    public List<User> findUsersByOffice(String office_id) {
        return find("from User where (office_id=:p1 or company_id =:p1) and delFlag=:p2", new Parameter(office_id,User.DEL_FLAG_NORMAL));
    }
}
