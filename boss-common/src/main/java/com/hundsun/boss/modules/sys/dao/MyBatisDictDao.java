package com.hundsun.boss.modules.sys.dao;

import java.util.List;

import com.hundsun.boss.base.mybatis.MyBatisDao;
import com.hundsun.boss.modules.sys.entity.Dict;

/**
 * MyBatis字典DAO接口
 */
@MyBatisDao
public interface MyBatisDictDao {

    Dict get(String id);

    List<Dict> find(Dict dict);

}
