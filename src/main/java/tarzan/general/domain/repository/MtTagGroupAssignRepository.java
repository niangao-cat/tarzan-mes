package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtTagGroupAssign;
import tarzan.general.domain.vo.MtTagGroupAssignVO;

/**
 * 数据收集项分配收集组表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupAssignRepository
                extends BaseRepository<MtTagGroupAssign>, AopProxy<MtTagGroupAssignRepository> {
    /**
     * tagGroupAssignGet-获取数据项组分配属性
     *
     * @param tenantId
     * @param tagGroupAssignId
     * @author guichuan.li
     * @date 2019/7/02
     */
    MtTagGroupAssign tagGroupAssignGet(Long tenantId, String tagGroupAssignId);

    /**
     * tagGroupAssignBatchUpdate-数据收集组分配数据项批量新增&更新
     *
     * @author benjamin
     * @date 2019/9/19 9:53 AM
     * @param tenantId 租户Id
     * @param tagGroupAssignList List<MtTagGroupAssignVO>
     * @param fullUpdate 全量更新标识
     * @return Long
     */
    Long tagGroupAssignBatchUpdate(Long tenantId, List<MtTagGroupAssignVO> tagGroupAssignList, String fullUpdate);

    /**
     * propertyLimitTagGroupAssignQuery-根据属性获取数据收集组
     *
     * @author: chuang.yang
     * @date: 2020/02/25 20:42
     * @param tenantId :
     * @param dto :
     * @return java.util.List<java.lang.String>
     */
    List<String> propertyLimitTagGroupAssignQuery(Long tenantId, MtTagGroupAssign dto);
}
