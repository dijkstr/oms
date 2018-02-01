package com.hundsun.boss.modules.sys.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.sys.dao.OfficeDao;
import com.hundsun.boss.modules.sys.entity.Office;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 机构Service
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends BaseService {

    @Autowired
    private OfficeDao officeDao;

    public Office get(String id) {
        return officeDao.get(id);
    }

    public List<Office> findAll() {
        return UserUtils.getOfficeList();
    }

    @Transactional(readOnly = false)
    public void save(Office office) {
        office.setParent(this.get(office.getParent().getId()));
        String oldParentIds = office.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
        office.setParentIds(office.getParent().getParentIds() + office.getParent().getId() + ",");
        officeDao.clear();
        officeDao.save(office);
        // 更新子节点 parentIds
        List<Office> list = officeDao.findByParentIdsLike("%," + office.getId() + ",%");
        for (Office e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, office.getParentIds()));
        }
        officeDao.save(list);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        officeDao.deleteById(id, "%," + id + ",%");
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    
    public Office findByCode(String code) {
        return officeDao.findByCode(code);
    }
    
    public Office findDeptByCode(String codes) {
        return officeDao.findDeptByCode(codes);
    }
    
    public List<Office> findByParentIdsLike(String parentIds){
        return officeDao.findByParentIdsLike("%"+parentIds+ ",%");
    }
    
    public List<Office> findListByAreaIds(List<String> ids) {
        DetachedCriteria dc = officeDao.createDetachedCriteria();
        dc.add(Restrictions.in("area.id", ids));
        dc.add(Restrictions.eq(Office.FIELD_DEL_FLAG, Office.DEL_FLAG_NORMAL));
        return officeDao.find(dc);
    }
}
