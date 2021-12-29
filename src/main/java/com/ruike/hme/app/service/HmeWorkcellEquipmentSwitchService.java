package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO;
import com.ruike.hme.domain.vo.HmeWkcEquSwitchVO2;

import java.text.ParseException;
import java.util.List;

/**
 * 工位设备切换应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-06-23 09:34:16
 */
public interface HmeWorkcellEquipmentSwitchService {

    /**
     * 加载设备类及其设备
     *
     * @param tenantId 租户Id
     * @param dto      查询参数
     * @return com.ruike.hme.domain.vo.HmeWkcEquSwitchVO2
     * @author chaonan.hu 2020/6/23
     */
    HmeWkcEquSwitchVO2 getEquCategoryAndAssetEncoding(Long tenantId, HmeWkcEquSwitchDTO dto);

    /**
     * 绑定工位设备关系
     *
     * @param tenantId 租户Id
     * @param dto      绑定参数
     * @return com.ruike.hme.api.dto.HmeWkcEquSwitchDTO7
     * @author chaonan.hu 2020/6/23
     */
    HmeWkcEquSwitchDTO7 bandingStationAndEquipment(Long tenantId, HmeWkcEquSwitchDTO2 dto);

    /**
     * 绑定工位设备关系确认
     *
     * @param tenantId 租户ID
     * @param dto      确认操作参数
     * @author chaonan.hu 2020/7/8
     */
    void confirmBandingRel(Long tenantId, HmeWkcEquSwitchDTO2 dto);

    /**
     * 删除工位设备关系
     *
     * @param tenantId 租户Id
     * @param dto      删除数据
     * @author chaonan.hu 2020/6/23
     */
    void deleteStationAndEquipment(Long tenantId, HmeWkcEquSwitchDTO3 dto);

    /**
     * 根据设备编码查询描述
     *
     * @param tenantId      租户Id
     * @param assetEncoding 设备编码
     * @return java.lang.String
     * @author chaonan.hu 2020/6/23
     */
    String getEquipmentDesc(Long tenantId, String assetEncoding);

    /**
     * 更换工位设备关系
     *
     * @param tenantId 租户Id
     * @param dto      更换数据
     * @return com.ruike.hme.api.dto.HmeWkcEquSwitchDTO8
     * @author chaonan.hu 2020/6/23
     */
    HmeWkcEquSwitchDTO8 replaceStationAndEquipment(Long tenantId, HmeWkcEquSwitchDTO4 dto);

    /**
     * 更换工位设备关系确认
     *
     * @param tenantId 租户ID
     * @param dto      更换数据
     * @author chaonan.hu 2020/7/8
     */
    void confirmReplaceRel(Long tenantId, HmeWkcEquSwitchDTO4 dto);
}
