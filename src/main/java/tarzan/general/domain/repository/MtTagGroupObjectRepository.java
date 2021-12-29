package tarzan.general.domain.repository;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.api.dto.MtTagGroupObjectDTO3;
import tarzan.general.api.dto.MtTagGroupObjectDTO4;
import tarzan.general.domain.entity.MtTagGroupObject;
import tarzan.general.domain.vo.MtTagGroupObjectVO;
import tarzan.general.domain.vo.MtTagGroupObjectVO2;
import tarzan.general.domain.vo.MtTagGroupObjectVO3;

/**
 * 数据收集组关联对象表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
public interface MtTagGroupObjectRepository
        extends BaseRepository<MtTagGroupObject>, AopProxy<MtTagGroupObjectRepository> {
    /**
     * tagGroupObjectGet-获取数据组与对象关系属性
     *
     * @author benjamin
     * @date 2019-07-02 10:15
     * @param tenantId IRequest
     * @param tagGroupObjectId 数据收集组对象分配ID
     * @return MtTagGroupObject
     */
    MtTagGroupObject tagGroupObjectGet(Long tenantId, String tagGroupObjectId);

    /**
     * 根据GroupObjectId集合批量获取数据
     *
     * @author benjamin
     * @date 2019-07-02 19:52
     * @param tenantId Long
     * @param tagGroupObjectIdList 数据收集组对象分配Id集合
     * @return List
     */
    List<MtTagGroupObject> tagGroupObjectBatchGet(Long tenantId, List<String> tagGroupObjectIdList);


    /**
     * propertyLimitTagGroupObjectQuery-根据属性获取数据组与对象关系
     *
     * @author benjamin
     * @date 2019-07-02 10:34
     * @param tenantId Long
     * @param mtTagGroupObject MtTagGroupObject
     * @return List
     */
    List<String> propertyLimitTagGroupObjectQuery(Long tenantId, MtTagGroupObject mtTagGroupObject);

    /**
     * 批量根据属性获取数据组与对象关系
     *
     * 需传入数据收集组Id集合
     *
     * @author benjamin
     * @date 2019-07-09 19:20
     * @param tenantId IRequest
     * @param tagGroupIdList 数据采集组Id集合
     * @param mtTagGroupObject MtTagGroupObject
     * @return List
     */
    List<String> propertyLimitTagGroupObjectBatchQuery(Long tenantId, List<String> tagGroupIdList,
                                                       MtTagGroupObject mtTagGroupObject);


    /**
     * tagGroupObjectBatchUpdate-数据收集组关联对象批量新增&更新
     *
     * @param tenantId
     * @author guichuan.li
     * @date 2019/9/19
     */
    Long tagGroupObjectBatchUpdate(Long tenantId, List<MtTagGroupObjectVO> dto, String fullUpdate);

    /**
     * objectLimitTagGroupQuery-根据对象限制获取对应数据采集组
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> objectLimitTagGroupQuery(Long tenantId, MtTagGroupObjectVO2 dto);

    /**
     * 生产版本LOV数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/8/10 15:58:15
     * @return io.choerodon.core.domain.Page<tarzan.general.api.dto.MtTagGroupObjectDTO3>
     */
    Page<MtTagGroupObjectDTO3> productionVersionQuery(Long tenantId, MtTagGroupObjectDTO4 dto, PageRequest pageRequest);
}
