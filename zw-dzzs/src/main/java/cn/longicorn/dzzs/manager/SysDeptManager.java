package cn.longicorn.dzzs.manager;

import cn.longicorn.dzzs.entity.SysDept;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.web.crud.StandardManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ywj on 2017/11/8.
 */
@Service
public class SysDeptManager extends StandardManager<SysDept,Long> {
    @Override
    public Page<SysDept> searchPage(Page<SysDept> page) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void create(SysDept sysDept) {

    }

    @Override
    public void update(SysDept sysDept) {

    }

    @Override
    public SysDept get(Long id) {
        return null;
    }

    public List<SysDept> queryAll() {
        return null;
    }

    public List<Long> queryDetpIdList(long deptId) {
        return null;
    }
}
