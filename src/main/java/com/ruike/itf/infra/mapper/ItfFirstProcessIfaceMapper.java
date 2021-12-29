package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO;
import com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/10/15 10:39
 */
public interface ItfFirstProcessIfaceMapper {

    /**
     * 根据用户账号查询用户ID
     * @param loginName
     * @return java.lang.Long
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    Long getUserIdByLoginName(@Param("loginName") String loginName);

    /**
     * 批量删除工位设备关系表数据
     * @param tenantId
     * @param equipmentWkcRelIdList
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    void batchDeleteEquipmentWkcRel(@Param("tenantId") Long tenantId, @Param("equipmentWkcRelIdList") List<String> equipmentWkcRelIdList);

    /**
     * 根据设备ID查询已存在工位设备关系的设备信息
     * @param tenantId
     * @param equipmentIdList
     * @return com.ruike.itf.domain.vo.ItfFirstProcessIfaceVO2
     * @author sanfeng.zhang@hand-china.com 2021/10/15
     */
    ItfFirstProcessIfaceVO2 alreadyEquipmentWkcRelQuery(@Param("tenantId") Long tenantId, @Param("equipmentIdList") List<String> equipmentIdList);

}
