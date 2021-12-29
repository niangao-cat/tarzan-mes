package com.ruike.qms.app.service;

import com.ruike.qms.api.dto.*;
import com.ruike.qms.domain.entity.QmsMaterialInspScheme;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 物料检验计划应用服务
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:43
 */
public interface QmsMaterialInspSchemeService {
    /**
     * 头信息查询
     *
     * @param tenantId                租户ID
     * @param inspectionSchemeHeadDTO 查询参数
     * @param pageRequest             分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsMisHeadReturnDTO>
     * @author han.zhang 2020-04-22 10:12
     */
    Page<QmsMisHeadReturnDTO> selectHeadList(Long tenantId, QmsMaterialInspectionSchemeHeadDTO inspectionSchemeHeadDTO, PageRequest pageRequest);

    /**
     * 计划检验列表行信息查询
     *
     * @param tenantId           租户ID
     * @param inspectionSchemeId 查询参数
     * @param pageRequest        分页参数
     * @return io.choerodon.core.domain.Page<com.ruike.qms.api.dto.QmsMisLineReturnDTO>
     * @author han.zhang 2020-04-22 10:24
     */
    Page<QmsMaterialInspContentReturnDTO> selectLineList(Long tenantId, QmsMisLineQueryDTO inspectionSchemeId, PageRequest pageRequest);

    /**
     * 物料检验计划头新增或保存
     *
     * @param tenantId                    租户id
     * @param qmsMaterialInspectionScheme 参数
     * @return com.ruike.qms.domain.entity.QmsMaterialInspectionScheme
     * @author han.zhang 2020-04-22 15:57
     */
    QmsMaterialInspScheme addAndUpdateHead(Long tenantId, QmsMaterialInspScheme qmsMaterialInspectionScheme);

    /**
     * 物料检验计划发布
     *
     * @param tenantId            租户ID
     * @param inspectionSchemeIds 物料检验项ID列表
     * @return java.util.List<java.lang.String>
     * @author han.zhang 2020-04-22 16:04
     */
    List<String> publish(Long tenantId, List<String> inspectionSchemeIds);

    /**
     * 检验计划删除
     *
     * @param tenantId            租户id
     * @param inspectionSchemeIds 检验计划id
     * @return java.util.List<com.ruike.qms.domain.entity.QmsMaterialInspectionScheme>
     * @author han.zhang 2020-04-22 17:43
     */
    List<QmsMaterialInspScheme> delete(Long tenantId, List<QmsMaterialInspScheme> inspectionSchemeIds);

    /**
     * 二级页面检验组查询
     *
     * @param tenantId           租户ID
     * @param inspectionSchemeId 物料检验项id
     * @return java.util.List<com.ruike.qms.api.dto.QmsTagGroupQueryReturnDTO>
     * @author han.zhang 2020-04-22 21:53
     */
    Page<QmsTagGroupQueryReturnDTO> selectQuatityList(Long tenantId, QmsTagGroupQueryDTO inspectionSchemeId, PageRequest pageRequest);

    /**
     * 新增检验组
     *
     * @param tenantId        租户ID
     * @param addTagGroupDTOS 新增检验组
     * @author han.zhang 2020-04-22 21:53
     */
    void addTagGroup(Long tenantId, List<QmsAddTagGroupDTO> addTagGroupDTOS);

    /**
     * 删除检验组下质检组
     *
     * @param tenantId       租户id
     * @param tagGroupRelIds 关系表id
     * @author han.zhang 2020-04-24 11:48
     */
    void deleteTagGroup(Long tenantId, List<String> tagGroupRelIds);

    /**
     * 质检组编辑
     *
     * @param tenantId             租户ID
     * @param qmsTagContentEditDTO 质检组
     * @author han.zhang 2020-04-24 14:36
     */
    void editTag(Long tenantId, List<QmsTagContentEditDTO> qmsTagContentEditDTO);

    /**
     * 全量同步
     *
     * @param tenantId           租户ID
     * @param qmsAddTagGroupDtoS 新增检验组数据
     * @author han.zhang 2020-04-24 17:09
     */
    void allSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS);

    /**
     * 增量同步
     *
     * @param tenantId           租户ID
     * @param qmsAddTagGroupDtoS 新增检验组数据
     * @author han.zhang 2020-04-24 17:10
     */
    void partSynchronize(Long tenantId, List<QmsAddTagGroupDTO> qmsAddTagGroupDtoS);

    /**
     * 物料检验计划复制按钮
     *
     * @param tenantId
     * @param qmsMaterialInspSchemeDTO
     * @return java.lang.String
     * @auther wenqiang.yin@hand-china.com 2021/2/7 14:24
    */
    String copy(Long tenantId, QmsMaterialInspSchemeDTO qmsMaterialInspSchemeDTO);
}
