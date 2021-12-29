package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import com.ruike.qms.domain.entity.QmsPqcInspectionScheme;
import com.ruike.qms.domain.vo.QmsPqcInspectionContentVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 巡检检验计划资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-08-12 16:41:12
 */
public interface QmsPqcInspectionSchemeRepository extends BaseRepository<QmsPqcInspectionScheme>, AopProxy<QmsPqcInspectionSchemeRepository> {


    /**
     * 头信息查询
     *
     * @param tenantId                租户ID
     * @param inspectionSchemeHeadDTO 查询参数
     * @param pageRequest             分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsMisHeadReturnDTO>
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:24
     */
    Page<QmsMisHeadReturnDTO> selectHeadList(Long tenantId, QmsMaterialInspectionSchemeHeadDTO inspectionSchemeHeadDTO, PageRequest pageRequest);

    /**
     * 计划检验列表行信息查询
     *
     * @param tenantId           租户ID
     * @param qmsMisLineQueryDTO 查询参数
     * @param pageRequest        分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsMaterialInspContentReturnDTO>
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:26
     */
    Page<QmsPqcInspectionContentVO> selectLineList(Long tenantId, QmsMisLineQueryDTO qmsMisLineQueryDTO, PageRequest pageRequest);

    /**
     * 物料检验计划头新增或保存
     *
     * @param tenantId               租户id
     * @param qmsPqcInspectionScheme 参数
     * @return com.ruike.qms.domain.entity.QmsPqcInspectionScheme
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:26
     */
    QmsPqcInspectionScheme addAndUpdateHead(Long tenantId, QmsPqcInspectionScheme qmsPqcInspectionScheme);

    /**
     * 物料检验计划发布
     *
     * @param tenantId            租户ID
     * @param qmsMisHeadPublishDTOS 物料检验项ID列表
     * @return java.util.List<java.lang.String>
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:26
     */
    List<String> publish(Long tenantId, List<String> qmsMisHeadPublishDTOS);

    /**
     * 检验计划删除
     *
     * @param tenantId            租户id
     * @param inspectionSchemeIds 检验计划id
     * @return java.util.List<com.ruike.qms.domain.entity.QmsPqcInspectionScheme>
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:27
     */
    List<QmsPqcInspectionScheme> delete(Long tenantId, List<QmsPqcInspectionScheme> inspectionSchemeIds);


    /**
     * 二级页面检验组查询
     *
     * @param tenantId           租户ID
     * @param qmsTagGroupQueryDTO 物料检验项id
     * @return java.util.List<com.ruike.qms.api.dto.QmsTagGroupQueryReturnDTO>
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:27
     */
    Page<QmsTagGroupQueryReturnDTO> selectQuatityList(Long tenantId, QmsTagGroupQueryDTO qmsTagGroupQueryDTO, PageRequest pageRequest);

    /**
     * 新增检验组
     *
     * @param tenantId        租户ID
     * @param addTagGroupDTOS 新增检验组
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:27
     */
    void addTagGroup(Long tenantId, List<QmsAddTagGroupDTO> addTagGroupDTOS);


    /**
     * 删除检验组下质检组
     *
     * @param tenantId       租户id
     * @param tagGroupRelIds 关系表id
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:27
     */
    void deleteTagGroup(Long tenantId, List<String> tagGroupRelIds);

    /**
     * 质检组编辑
     *
     * @param tenantId             租户ID
     * @param qmsTagContentEditDTOList 质检组
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:28
     */
    void editTag(Long tenantId, List<QmsTagContentEditDTO> qmsTagContentEditDTOList);

    /**
     * 全量同步
     *
     * @param tenantId           租户ID
     * @param qmsAddTagGroupDtoS 新增检验组数据
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:28
     */
    void allSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS);

    /**
     * 增量同步
     *
     * @param tenantId           租户ID
     * @param qmsAddTagGroupDtoS 新增检验组数据
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/8/12 17:28
     */
    void partSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS);
}
