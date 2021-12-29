package tarzan.material.domain.repository;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.material.domain.entity.MtPfepSchedule;

/**
 * 物料计划属性资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:09:58
 */
public interface MtPfepScheduleRepository extends BaseRepository<MtPfepSchedule>, AopProxy<MtPfepScheduleRepository> {

    /**
     * pfepScheduleAttrPropertyUpdate-物料计划PFEP属性新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/19 11:35
     * @param tenantId
     * @param dto
     * @return void
     */
    void pfepScheduleAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
