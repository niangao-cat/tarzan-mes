package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeWoInputRecordDTO;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO2;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO3;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO5;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工单投料记录表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 17:41:58
 */
public interface HmeWoInputRecordService {

    /**
     * 查询工单信息
     *
     * @param tenantId
     * @param workOrderNum
     * @author jiangling.zheng@hand-china.com 2020/10/27 20:43
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO
     */
    HmeWoInputRecordDTO workOrderForUi(Long tenantId, String workOrderNum);

    /**
     * 查询投料信息
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @author jiangling.zheng@hand-china.com 2020/10/29 11:32
     * @return java.util.List<com.ruike.hme.api.dto.HmeWoInputRecordDTO2>
     */
    Page<HmeWoInputRecordDTO2> woInputRecordForUi(Long tenantId, HmeWoInputRecordDTO3 dto, PageRequest pageRequest);

    /**
     * 条码扫描
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/29 12:33
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO2
     */
    HmeWoInputRecordDTO2 materialLotScanForUi(Long tenantId, HmeWoInputRecordDTO3 dto);

    /**
     * 投料
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/10/30 1:02
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO5
     */
    HmeWoInputRecordDTO5 woInputForUi(Long tenantId, HmeWoInputRecordDTO5 dto);

    /**
     * 退料
     *
     * @param tenantId
     * @param dto
     * @author jiangling.zheng@hand-china.com 2020/12/16 13:45
     * @return com.ruike.hme.api.dto.HmeWoInputRecordDTO5
     */
    HmeWoInputRecordDTO5 woOutputForUi(Long tenantId, HmeWoInputRecordDTO5 dto);
}
