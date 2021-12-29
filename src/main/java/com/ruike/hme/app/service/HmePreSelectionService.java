package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.entity.HmeSelectionDetails;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 预挑选基础表应用服务
 *
 * @author wenzhang.yu@hand-china.com 2020-08-18 15:00:33
 */
public interface HmePreSelectionService {

    /**
     *@description 查询右侧工单数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 19:54
     *@param tenantId
     *@param dto
     *@param pageRequest
     *@return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmePreSelectionReturnDTO>
     **/
    Page<HmePreSelectionReturnDTO> workOrderQuery(Long tenantId, HmePreSelectionDTO dto, PageRequest pageRequest);

    /**
     *@description 确认开始挑选
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 19:55
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmePreSelectionReturnDTO2
     **/
    HmePreSelectionReturnDTO2 confirm(Long tenantId, HmePreSelectionDTO2 dto);

    /**
     *@description 挑选批次查询具体数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 19:56
     *@param tenantId
     *@param selectLot
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO3>
     **/
    List<HmePreSelectionReturnDTO3> selectLot(Long tenantId, String selectLot);

    /**
     *@description 查询盒子号具体数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 20:00
     *@param tenantId
     *@param materialLotCode
     *@param selectLot
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     **/
    List<HmePreSelectionReturnDTO4> materialLot(Long tenantId, String materialLotCode,String selectLot);

    /**
     *@description 扫描新盒子
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 20:01
     *@param tenantId
     *@param materialLotCode
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     **/
    List<HmePreSelectionReturnDTO4> tomaterialLot(Long tenantId, String materialLotCode);


    /**
     *@description 预装明细查询
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 20:03
     *@param tenantId
     *@param selectLot
     *@return java.util.List<com.ruike.hme.domain.entity.HmeSelectionDetails>
     **/
    List<HmeSelectionDetails> selectDetails(Long tenantId, String selectLot);

    /**
     *@description 装入新盒子
     *@author wenzhang.yu@hand-china.com
     *@date 2020/8/27 20:02
     *@param tenantId
     *@param dtos
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO4>
     **/
    List<HmePreSelectionReturnDTO4> load(Long tenantId, List<HmePreSelectionDTO3> dtos);


    List<HmeCosRuleLogicDTO> selectRule(Long tenantId, String ruleId);

    List<HmePreSelectionReturnDTO5> getMateriallot(Long tenantId, String locatorCode);

    /**
     *@description 查询盒子信息
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/2 14:29
     *@param tenantId
     *@param materialLotCode
     *@return com.ruike.hme.api.dto.HmePreSelectionReturnDTO5
     **/
    HmePreSelectionReturnDTO5 materialLotQuery(Long tenantId, String materialLotCode);

    /**
     *@description 更新查询到的盒子信息
     *@author xin.t@raycuslaser.com
     *@date 2021/08/06 16:29
     *@param tenantId
     *@param  materialLotCodeList
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>
     **/
    List<HmePreSelectionReturnDTO5> materialLotReQuery(Long tenantId, List<String> materialLotCodeList);

    /**
     *@description 挑选批次查询具体数据
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/2 15:06
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO3>
     *
     * @param tenantId
     * @param selectLot
     * @param pageRequest*/
    Page<HmePreSelectionReturnDTO3> selectLotQuery(Long tenantId, String selectLot, PageRequest pageRequest);

    /**
     *@description 装入
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/2 15:41
     *@param tenantId
     *@param dtos
     *@return void
     **/
    void loadNew(Long tenantId, List<HmePreSelectionDTO6> dtos);

    /**
     *@description 根据容器获取盒子信息
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/3 15:08
     *@param tenantId
     *@param containerCode
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO5>
     **/
    List<HmePreSelectionReturnDTO5> materialLotQueryByContainer(Long tenantId, String containerCode);

    /**
     *@description 挑选批次查询
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/2 16:10
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO6>
     *
     * @param tenantId
     * @param productTypeMeaning
     * @param ruleCode
     * @param statusMeaning
     * @param materialCode
     * @param materialName
     * @param pageRequest*/
    Page<HmePreSelectionReturnDTO6> selectLotQueryAll(Long tenantId, String productTypeMeaning, String ruleCode, String statusMeaning, String materialCode, String materialName,
                                                      PageRequest pageRequest);

    /**
     *@description 挑选未挑选批次查询
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/2 17:33
     *@param tenantId
     *@param containerCode
     *@return java.util.List<com.ruike.hme.api.dto.HmePreSelectionReturnDTO7>
     **/
    List<HmePreSelectionReturnDTO7> selectLotQueryElse(Long tenantId, String containerCode);

    List<HmePreSelectionReturnDTO8> selectLotInformation(Long tenantId, String selectLot);

    /**
     *@description 确认开始挑选
     *@author wenzhang.yu@hand-china.com
     *@date 2020/11/30 14:01
     *@param tenantId
     *@param dto
     *@return com.ruike.hme.api.dto.HmePreSelectionReturnDTO2
     **/
    HmePreSelectionReturnDTO2 confirmNew(Long tenantId, HmePreSelectionDTO1 dto);

    String qtyQueryByContainer(Long tenantId, String containerCode);

    void transfer(Long tenantId, HmePreSelectionDTO7 dto);

    /**
     * 挑选结果撤回数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 15:09:21
     * @return io.choerodon.core.domain.Page<com.ruike.hme.api.dto.HmePreSelectionReturnDTO12>
     */
    Page<HmePreSelectionReturnDTO12> recallDataQuery(Long tenantId, HmePreSelectionReturnDTO11 dto, PageRequest pageRequest);

    /**
     * 挑选结果撤回
     *
     * @param tenantId 租户ID
     * @param dto 虚拟号集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/1/12 16:18:16
     * @return com.ruike.hme.api.dto.HmePreSelectionReturnDTO13
     */
    HmePreSelectionReturnDTO13 recallData(Long tenantId, HmePreSelectionReturnDTO13 dto);
}
