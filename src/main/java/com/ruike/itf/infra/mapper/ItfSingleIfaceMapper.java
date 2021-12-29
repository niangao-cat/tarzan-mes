package com.ruike.itf.infra.mapper;

import com.ruike.itf.domain.vo.ItfSingleIfaceVO2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单件设备接口回调Mapper
 *
 * @author chaonan.hu@hand-china.com 2021-09-27 10:08:12
 */
public interface ItfSingleIfaceMapper {

    /**
     * 根据用户账号查询用户ID
     *
     * @param loginName 用户账号
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 10:50:50
     * @return java.lang.Long
     */
    Long getUserIdByLoginName(@Param("loginName") String loginName);

    /**
     * 批量删除工位设备关系表数据
     *
     * @param tenantId 租户ID
     * @param equipmentWkcRelIdList 主键集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 03:23:01
     * @return void
     */
    void batchDeleteEquipmentWkcRel(@Param("tenantId") Long tenantId, @Param("equipmentWkcRelIdList") List<String> equipmentWkcRelIdList);

    /**
     * 根据设备ID查询已存在工位设备关系的设备信息
     *
     * @param tenantId 租户ID
     * @param equipmentIdList 设备ID集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/27 06:56:53
     * @return com.ruike.itf.domain.vo.ItfSingleIfaceVO2
     */
    ItfSingleIfaceVO2 alreadyEquipmentWkcRelQuery(@Param("tenantId") Long tenantId, @Param("equipmentIdList") List<String> equipmentIdList);
}
