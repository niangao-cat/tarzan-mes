package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeWkcEquSwitchDTO5;
import com.ruike.hme.domain.vo.HmeEoJobSnVO3;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobEquipment;

import java.util.List;

/**
 * SN进出站设备状态记录表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-06-28 16:52:11
 */
public interface HmeEoJobEquipmentRepository extends BaseRepository<HmeEoJobEquipment>, AopProxy<HmeEoJobEquipmentRepository> {

    /**
     * SN进出站设备及其状态信息记录
     *
     * @param tenantId 租户ID
     * @param dto      进出站记录数据
     * @return java.util.List<com.ruike.hme.domain.entity.HmeEoJobEquipment>
     * @author chaonan.hu 2020/6/28
     */
    List<HmeEoJobEquipment> snInToSiteEquipmentRecord(Long tenantId, HmeWkcEquSwitchDTO5 dto);

    /**
     *
     * @Description 出站批量新增设备数据
     *
     * @author yuchao.wang
     * @date 2020/11/19 0:23
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    void batchSaveEquipmentRecordForOutSite(Long tenantId, HmeEoJobSnVO3 dto);
}
