package com.hundsun.boss.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.modules.sys.dao.AreaDao;
import com.hundsun.boss.modules.sys.entity.Area;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 区域Service
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends BaseService {

    @Autowired
    private AreaDao areaDao;

    public Area get(String id) {
        return areaDao.get(id);
    }

    public List<Area> findAll() {
        return UserUtils.getAreaList();
    }

    @Transactional(readOnly = false)
    public void save(Area area) {
        area.setParent(this.get(area.getParent().getId()));
        String oldParentIds = area.getParentIds(); // 获取修改前的parentIds，用于更新子节点的parentIds
        area.setParentIds(area.getParent().getParentIds() + area.getParent().getId() + ",");
        areaDao.clear();
        areaDao.save(area);
        // 更新子节点 parentIds
        List<Area> list = areaDao.findByParentIdsLike("%," + area.getId() + ",%");
        for (Area e : list) {
            e.setParentIds(e.getParentIds().replace(oldParentIds, area.getParentIds()));
        }
        areaDao.save(list);
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
    }

    @Transactional(readOnly = false)
    public void delete(String id) {
        areaDao.deleteById(id, "%," + id + ",%");
        UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
    }

    /**
     * 查询某个区域及下级区域的id集合
     * @param id
     * @return
     */
    public List<String> findAreasById(String id) {
        return areaDao.findAreasById(id, "%," + id + ",%");
    }
}
