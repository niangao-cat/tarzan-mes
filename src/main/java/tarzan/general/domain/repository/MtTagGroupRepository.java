package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.general.api.dto.MtTagGroupDTO8;
import tarzan.general.domain.entity.MtTagGroup;
import tarzan.general.domain.vo.MtTagGroupVO1;
import tarzan.general.domain.vo.MtTagGroupVO2;

/**
 * 数据收集组表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupRepository extends BaseRepository<MtTagGroup>, AopProxy<MtTagGroupRepository> {
    /**
     * tagGroupGet-获取数据组属性
     *
     * @author benjamin
     * @date 2019-07-02 09:56
     * @param tenantId IRequest
     * @param tagGroupId 数据收集组Id
     * @return MtTagGroup
     */
    MtTagGroup tagGroupGet(Long tenantId, String tagGroupId);

    /**
     * propertyLimitTagGroupQuery-根据属性获取数据组
     *
     * @author benjamin
     * @date 2019-07-02 10:05
     * @param tenantId IRequest
     * @param mtTagGroup MtTagGroup
     * @return List
     */
    List<String> propertyLimitTagGroupQuery(Long tenantId, MtTagGroup mtTagGroup);

    /**
     * tagGroupBatchGet-批量获取数据组属性
     *
     * @param tenantId
     * @param tagGroupIds
     * @return List
     * @author guichuan.li
     * @date 2019/7/03
     */
    List<MtTagGroup> tagGroupBatchGet(Long tenantId, List<String> tagGroupIds);

    /**
     * tagGroupBasicBatchUpdate-数据收集组批量新增&更新
     *
     * @param tenantId
     * @param dto
     * @param fullUpdate
     * @return
     */
    Long tagGroupBasicBatchUpdate(Long tenantId, List<MtTagGroupDTO8> dto, String fullUpdate);

    /**
     * 根据属性获取数据收集组信息
     *
     * @Author peng.yuan
     * @Date 2019/10/17 10:45
     * @param tenantId :
     * @param dto :
     * @return java.util.List<tarzan.general.domain.vo.MtTagGroupVO2>
     */
    List<MtTagGroupVO2> propertyLimitTagGroupPropertyQuery(Long tenantId, MtTagGroupVO1 dto);

    /**
     * 根据前端页面查询条件查找数据
     *
     * @Author peng.yuan
     * @Date 2019/10/21 11:15
     * @param tenantId
     * @param mtTagGroup :
     * @return java.util.List<tarzan.general.domain.vo.MtTagGroupVO2>
     */
    List<MtTagGroupVO2> selectCondition(Long tenantId, MtTagGroup mtTagGroup);


    /**
     * 自定义根据数据收集组编码批量查询
     *
     * @Author Xie.yiyang
     * @Date 2019/11/18 16:25
     * @param tenantId
     * @param tagGroupCodes
     * @return java.util.List<MtTagGroup>
     */
    List<MtTagGroup> selectTagGroupByTagGroupCodes(Long tenantId, List<String> tagGroupCodes);

    /**
     * tagGroupAttrPropertyUpdate-数据采集组新增&更新扩展表属性
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/20
     */
    void tagGroupAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto);
}
