package com.hundsun.boss.modules.sys.utils;

import java.util.List;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.SpringContextHolder;
import com.hundsun.boss.modules.sys.dao.OfficeDao;
import com.hundsun.boss.modules.sys.entity.Office;

/**
 * 用户工具类
 */
public class OfficeUtils extends BaseService {

    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);

    /**
     * 获取部门名称
     * 
     * @param code
     * @return
     */
    public static String getOfficeNameByCode(String code) {
        String defaultName = "";
        Office office = officeDao.findByCode(code);
        if (!CommonUtil.isNullorEmpty(office)) {
            defaultName = office.getName();
        }
        return defaultName;
    }

    /**
     * 获取下级部门列表
     * 
     * @param parentId
     * @return
     */
    public static List<Office> findByParentIdsLike(String parentId) {
        List<Office> childOffices = officeDao.findByParentIdsLike("%" + parentId + ",%");
        return childOffices;
    }
}
