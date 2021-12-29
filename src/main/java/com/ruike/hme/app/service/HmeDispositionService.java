package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeDispositionFunctionDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDTO;
import com.ruike.hme.api.dto.HmeDispositionGroupDetailDTO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.entity.MtGenType;
import tarzan.method.domain.entity.MtDispositionFunction;

import java.util.List;

/**
 * description 处置方法与处置组功能service
 *
 * @author quan.luo@hand-china.com 2020/11/24 10:01
 */
public interface HmeDispositionService {

    /**
     * 列表条件查询
     *
     * @param tenantId                  租户id
     * @param pageRequest               分页信息
     * @param hmeDispositionFunctionDTO 条件
     * @return 数据
     */
    Page<HmeDispositionFunctionDTO> queryFunctionByCondition(Long tenantId, PageRequest pageRequest,
                                                             HmeDispositionFunctionDTO hmeDispositionFunctionDTO);

    /**
     * 处置方法类型下拉框查询
     *
     * @param tenantId 租户id
     * @return 数据
     */
    List<MtGenType> functionTypeQuery(Long tenantId);

    /**
     * 处置方法保存
     *
     * @param tenantId              租户id
     * @param mtDispositionFunction 数据
     * @return 数据
     */
    MtDispositionFunction functionSave(Long tenantId, MtDispositionFunction mtDispositionFunction);

    /**
     * 处置方法删除
     *
     * @param tenantId                  租户id
     * @param dispositionFunctionIdList 主键id
     * @return id
     */
    List<String> functionDel(Long tenantId, List<String> dispositionFunctionIdList);

    /**
     * 条件查询维护组数据
     *
     * @param tenantId               租户id
     * @param pageRequest            分页信息
     * @param hmeDispositionGroupDTO 查询参数
     * @return 查询数据
     */
    Page<HmeDispositionGroupDTO> queryGroupByCondition(Long tenantId, PageRequest pageRequest,
                                                       HmeDispositionGroupDTO hmeDispositionGroupDTO);

    /**
     * 处置方法组行数据查询
     *
     * @param tenantId           租户id
     * @param pageRequest        分页信息
     * @param dispositionGroupId 组id
     * @return 数据
     */
    Page<HmeDispositionFunctionDTO> groupDetailQuery(Long tenantId, PageRequest pageRequest, String dispositionGroupId);

    /**
     * 处置方法组与关系保存或修改
     *
     * @param tenantId                     租户id
     * @param hmeDispositionGroupDetailDTO 保存数据
     * @return 主键
     */
    String groupSaveOrUpdate(Long tenantId, HmeDispositionGroupDetailDTO hmeDispositionGroupDetailDTO);

    /**
     * 处置方法关系删除
     *
     * @param tenantId                  租户id
     * @param hmeDispositionFunctionDTO 删除的数据
     * @return 主键
     */
    String groupMemberDel(Long tenantId, HmeDispositionFunctionDTO hmeDispositionFunctionDTO);

    /**
     * 处置方法组删除
     *
     * @param tenantId               租户id
     * @param dispositionGroupIdList 主键
     * @return 主键
     */
    List<String> groupDel(Long tenantId, List<String> dispositionGroupIdList);
}
