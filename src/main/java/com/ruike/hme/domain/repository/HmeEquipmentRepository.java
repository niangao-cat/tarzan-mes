package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeEquipmentHisVO;
import com.ruike.hme.domain.vo.HmeEquipmentHisVO2;
import com.ruike.hme.domain.vo.HmeEquipmentVO;
import com.ruike.hme.domain.vo.HmeEquipmentVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 设备表资源库
 *
 * @author xu.deng01@hand-china.com 2020-06-03 18:27:09
 */
public interface HmeEquipmentRepository extends BaseRepository<HmeEquipment> {

    /**
     * 设备台账管理-获取设备基础信息
     *
     * @param tenantId  租户ID
     * @param condition 查询条件
     * @return : java.util.List<com.ruike.hme.domain.vo.HmeEquipmentVO>
     */
    List<HmeEquipmentVO> queryEquipmentList(Long tenantId, HmeEquipmentVO condition);

    /**
     * 设备台账管理-新增&更新设备基础信息
     *
     * @param tenantId 租户ID
     * @param dto      设备台账信息DTO
     * @return : com.ruike.hme.domain.entity.HmeEquipment
     */
    HmeEquipment equipmentBasicPropertyUpdate(Long tenantId, HmeEquipment dto);

    /**
     * 工位变更历史查询
     *
     * @param tenantId    租户ID
     * @param dto         查询参数
     * @param pageRequest 分页参数
     * @return : io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeEquipmentHisVO2>
     * @author sanfeng.zhang 2020/7/9 14:55
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

}
