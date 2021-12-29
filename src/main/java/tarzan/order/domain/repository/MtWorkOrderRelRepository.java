package tarzan.order.domain.repository;


import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.order.domain.entity.MtWorkOrderRel;
import tarzan.order.domain.vo.MtWorkOrderRelVO;

/**
 * 生产指令关系,标识生产指令的父子关系资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderRelRepository extends BaseRepository<MtWorkOrderRel>, AopProxy<MtWorkOrderRelRepository> {

    /**
     * 根据生产指令限制删除生产指令关系
     * 
     * @param tenantId
     * @param dto
     */
    void woRelDelete(Long tenantId, MtWorkOrderRel dto);

    /**
     * 获取指定关系类型下生产指令所有的上层指令
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderRelVO> woRelParentQuery(Long tenantId, MtWorkOrderRel dto);

    /**
     * 获取指定关系类型下生产指令所有的下层指令
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderRelVO> woRelSubQuery(Long tenantId, MtWorkOrderRel dto);

    /**
     * 取指定类型下生产指令所有相关的生产指令
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderRelVO> woRelTreeQuery(Long tenantId, MtWorkOrderRel dto);

    /**
     * woRelLimitChildQtyUpdate-根据生产指令更新指定类型的下层生产指令数量
     * 
     * @param tenantId
     * @param dto
     */
    void woRelLimitChildQtyUpdate(Long tenantId, MtWorkOrderRel dto);

    /**
     * 根据生产指令更新指定类型的下层新建生产指令数量
     * 
     * @param tenantId
     * @param dto
     */
    void woRelStatusLimitChildQtyUpdate(Long tenantId, MtWorkOrderRel dto);

    /**
     * 根据限制条件删除下层生产指令关系
     * 
     * @param tenantId
     * @param dto
     */
    void woLimitSubRelDelete(Long tenantId, MtWorkOrderRel dto);

    /**
     * woRelStatusReleaseQtyLimitChildQtyUpdate-根据生产指令更新指定类型的下层下达数未超限的新建和保留状态的生产指令数量
     * 
     * @param tenantId
     * @param workOrderId
     * @param relType
     */
    void woRelStatusReleaseQtyLimitChildQtyUpdate(Long tenantId, String workOrderId, String relType);

}
