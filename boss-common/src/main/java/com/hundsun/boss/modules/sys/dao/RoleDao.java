package com.hundsun.boss.modules.sys.dao;

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import com.hundsun.boss.modules.sys.entity.Role;

/**
 * 角色DAO接口
 */
@Repository
public class RoleDao extends HibernateDao<Role> {

    public Role findByName(String name) {
        return getByHql("from Role where delFlag = :p1 and name = :p2", new Parameter(Role.DEL_FLAG_NORMAL, name));
    }

    //	@Query("from Role where delFlag='" + Role.DEL_FLAG_NORMAL + "' order by name")
    //	public List<Role> findAllList();
    //
    //	@Query("select distinct r from Role r, User u where r in elements (u.roleList) and r.delFlag='" + Role.DEL_FLAG_NORMAL +
    //			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (r.user.id=?1 and r.delFlag='" + Role.DEL_FLAG_NORMAL +
    //			"') order by r.name")
    //	public List<Role> findByUserId(Long userId);

}
