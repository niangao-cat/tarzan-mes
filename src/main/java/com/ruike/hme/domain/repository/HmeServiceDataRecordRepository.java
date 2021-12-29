package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeEoJobSnDTO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO4;
import com.ruike.hme.domain.vo.HmeServiceDataRecordVO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeServiceDataRecord;

/**
 * 售后返品信息采集确认表资源库
 *
 * @author sanfeng.zhang@hand-china.com 2020-09-03 15:20:59
 */
public interface HmeServiceDataRecordRepository extends BaseRepository<HmeServiceDataRecord>,AopProxy<HmeServiceDataRecordRepository> {

    /**
     * 工位扫描-登入校验
     *
     * @param tenantId      租户ID
     * @param hmeEoJobSnDTO 扫描数据
     * @return HmeEoJobSnVO4
     */
    HmeEoJobSnVO4 workcellScan(Long tenantId, HmeEoJobSnDTO hmeEoJobSnDTO);

    /**
     * 扫描返修序列号
     *
     * @param tenantId      租户id
     * @param record        参数
     * @author sanfeng.zhang@hand-china.com 2020/9/3 15:46
     * @return com.ruike.hme.domain.vo.HmeServiceDataRecordVO
     */
    HmeServiceDataRecordVO scanRepairCode(Long tenantId, HmeServiceDataRecord record);

    /**
     * 保存
     *
     * @param tenantId      租户id
     * @param record        参数
     * @author sanfeng.zhang@hand-china.com 2020/9/4 9:44
     * @return com.ruike.hme.domain.vo.HmeServiceDataRecordVO
     */
    HmeServiceDataRecordVO saveRecord(Long tenantId, HmeServiceDataRecordVO record);

    /**
     * 完成
     *
     * @param tenantId     租户id
     * @param record        参数
     * @author sanfeng.zhang@hand-china.com 2020/9/4 9:55
     * @return com.ruike.hme.domain.vo.HmeServiceDataRecordVO
     */
    HmeServiceDataRecordVO completeRecord(Long tenantId, HmeServiceDataRecordVO record);
}
