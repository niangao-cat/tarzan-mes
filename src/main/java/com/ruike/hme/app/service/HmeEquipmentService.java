package com.ruike.hme.app.service;

import javax.servlet.http.HttpServletResponse;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.api.dto.HmeEquipmentLocatingDTO;
import com.ruike.hme.api.dto.HmeEquipmentUpadteDTO;
import com.ruike.hme.domain.entity.HmeEqManageTaskDocLine;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;

import java.util.List;

/**
 * 设备台账管理应用服务
 *
 * @author xu.deng01@hand-china.com 2020-06-03 18:27:09
 */
public interface HmeEquipmentService {

    /**
     * 设备台账管理-获取设备基础信息
     *
     * @param tenantId    租户ID
     * @param condition   查询条件
     * @param pageRequest 分页条件
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipmentVO>
     * @author Deng Xu 2020/6/3 18:47
     */
    Page<HmeEquipmentVO> listForUi(Long tenantId, HmeEquipmentVO condition, PageRequest pageRequest);

    /**
     *@description 设备台账管理-导出
     *@author wenzhang.yu@hand-china.com
     *@date 2021/3/15 15:47
     *@param tenantId
     *@param condition
     *@param exportParam
     *@return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentVO5>
     **/
    List<HmeEquipmentVO5> listForExport(Long tenantId, HmeEquipmentVO condition, ExportParam exportParam);

    /**
     * 设备台账管理-根据设备ID获取设备基础信息
     *
     * @param tenantId  租户ID
     * @param condition 查询条件（设备ID）
     * @return : com.ruike.hme.domain.vo.HmeEquipmentVO
     * @author Deng Xu 2020/6/4 14:25
     */
    HmeEquipmentVO queryOneForUi(Long tenantId, HmeEquipmentVO condition);

    /**
     * 设备台账管理-新增&更新设备基础信息
     *
     * @param tenantId 租户ID
     * @param dto      设备台账信息DTO
     * @return : com.ruike.hme.domain.entity.HmeEquipment
     * @author Deng Xu
     * @date 2020/6/3 18:48
     */
    HmeEquipment saveForUi(Long tenantId, HmeEquipment dto);

    /**
     * 工位变更历史查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipmentHisVO2>
     * @author sanfeng.zhang 2020/7/9 14:53
     */
    Page<HmeEquipmentHisVO2> queryWorkcellHisForUi(Long tenantId, HmeEquipmentHisVO dto, PageRequest pageRequest);

    /**
     * 设备信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    HmeEquipmentVO3 queryOneInfo(Long tenantId, HmeEquipment dto);

    /**
     * 工位扫描-登入校验
     *
     * @param tenantId      租户ID
     * @param dto 扫描数据
     * @return HmeEoJobSnVO4
     */
    HmeEoJobSnVO7 workcellScan(Long tenantId, HmeEoJobSnDTO dto);

    /**
     * 获取设备点检内容
     * @param tenantId
     * @param dto
     * @return
     */
    HmeEqManageTaskInfoVO2 equipmentContent(Long tenantId, HmeEquipmentLocatingDTO dto, PageRequest pageRequest);

    /**
     * 获取保养内容
     * @param tenantId
     * @param dto
     * @return
     */
    HmeEqManageTaskInfoVO2 maintainContent(Long tenantId, HmeEquipmentLocatingDTO dto, PageRequest pageRequest);

    /**
     * 修改设备点检和保养
     * @param tenantId
     * @param dto
     */
    HmeEqManageTaskDocLine update(Long tenantId, HmeEquipmentUpadteDTO dto);

    /**
     * 保养备注保存按钮
     *
     * @param tenantId
     * @param hmeEqManageTaskDocLine
     * @return com.ruike.hme.domain.entity.HmeEqManageTaskDocLine
     * @author sanfeng.zhang@hand-china.com 2021/3/4 15:13
     */
    HmeEqManageTaskDocLine maintainEquipmentUpdate(Long tenantId, HmeEqManageTaskDocLine hmeEqManageTaskDocLine);

    /**
     * 设备修改历史
     *
     * @param tenantId
     * @param equipmentId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipmentHisVO3>
     * @author sanfeng.zhang@hand-china.com 2021/3/23 11:13
     */
    Page<HmeEquipmentHisVO3> queryEquipmentHis(Long tenantId, String equipmentId, PageRequest pageRequest);

    /**
     * @param tenantId
     * @param hmeEquipmentVO6
     * @param response
     * @Description 设备资产编码标签打印
     * @Date 2021/04/01 22:44
     * @Created by penglin.sui
     */
    void print(Long tenantId, HmeEquipmentVO6 hmeEquipmentVO6,HttpServletResponse response);

    /**
     * 设备资产编码标签打印校验
     *
     * @param tenantId
     * @param hmeEquipmentVO6
     * @author sanfeng.zhang@hand-china.com 2021/4/29 14:44
     * @return void
     */
    void printCheck(Long tenantId, HmeEquipmentVO6 hmeEquipmentVO6);
}
