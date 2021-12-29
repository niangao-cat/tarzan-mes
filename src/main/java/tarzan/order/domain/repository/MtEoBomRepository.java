package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.vo.MtBomComponentVO19;
import tarzan.method.domain.vo.MtBomComponentVO20;
import tarzan.order.domain.entity.MtEoBom;
import tarzan.order.domain.vo.MtEoBomVO;
import tarzan.order.domain.vo.MtEoBomVO2;
import tarzan.order.domain.vo.MtEoBomVO3;

/**
 * EO装配清单资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoBomRepository extends BaseRepository<MtEoBom>, AopProxy<MtEoBomRepository> {
    /**
     * eoBomGet-获取指定执行作业的装配清单
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    String eoBomGet(Long tenantId, String eoId);

    /**
     * 自定义批量获取执行作业的装配清单
     *
     * @Author Xie.yiyang
     * @Date 2019/11/22 15:19
     * @param tenantId
     * @param eoIds
     * @return java.lang.String
     */
    List<MtEoBom> eoBomBatchGet(Long tenantId, List<String> eoIds);

    /**
     * attritionLimitEoComponentQtyQuery-获取指定执行作业的组件和组件用量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtBomComponentVO19> attritionLimitEoComponentQtyQuery(Long tenantId, MtBomComponentVO20 dto);

    /**
     * eoBomValidate-验证执行作业装配清单是否满足使用条件
     *
     * @param tenantId
     * @param eoId
     */
    void eoBomValidate(Long tenantId, String eoId);

    /**
     * eoBomUpdate-更新执行作业清单
     *
     * update remarks
     * <ul>
     * <li>2019-09-18 benjamin 添加返回参数</li>
     * </ul>
     *
     * @param tenantId 租户Id
     * @param dto MtEoBomVO
     * @return MtEoBomVO2
     */
    MtEoBomVO2 eoBomUpdate(Long tenantId, MtEoBomVO dto);

    /**
     * eoBomUpdateValidate-验证执行作业是否达到更改装配清单的需求
     *
     * @param tenantId
     * @param dto
     */
    void eoBomUpdateValidate(Long tenantId, MtEoBomVO dto);

    /**
     * eoBomBatchUpdate-批量更新执行作业清单
     *
     * @Author Xie.yiyang
     * @Date 2019/11/22 14:24
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoBomBatchUpdate(Long tenantId, MtEoBomVO3 dto);

}
