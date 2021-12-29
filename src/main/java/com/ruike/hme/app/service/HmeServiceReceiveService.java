package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeServiceReceiveDTO;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO2;
import com.ruike.hme.api.dto.HmeServiceReceiveDTO4;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO2;
import com.ruike.hme.domain.vo.HmeServiceReceiveVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.dto.LovValueDTO;

import java.util.List;

/**
 * 营销服务部接收拆箱登记表应用服务
 *
 * @author chaonan.hu@hand-china.com 2020-09-01 14:14:21
 */
public interface HmeServiceReceiveService {

    /**
     * 售后登记-扫描物流单号
     *
     * @param tenantId 租户ID
     * @param logisticsNumber 物流单号
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 14:45:15
     * @return com.ruike.hme.domain.vo.HmeServiceReceiveVO
     */
    HmeServiceReceiveVO scanlogisticsNumber(Long tenantId, String logisticsNumber);

    /**
     * 售后登记-售后接收部门下拉框数据
     * 
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 15:09:21
     * @return java.util.List<org.hzero.boot.platform.lov.dto.LovValueDTO>
     */
    List<LovValueDTO> receiveDepartment(Long tenantId);

    /**
     * 售后登记-扫描SN
     * 
     * @param tenantId 租户ID
     * @param dto 扫描信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 03:19:36 
     * @return com.ruike.hme.domain.vo.HmeServiceReceiveVO2
     */
    HmeServiceReceiveVO2 scanSn(Long tenantId, HmeServiceReceiveDTO4 dto);

    /**
     * 售后登记-物料LOV数据
     *
     * @param tenantId 租户ID
     * @param dto 查询信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 15:35:55
     * @return com.ruike.hme.domain.vo.HmeServiceReceiveVO3
     */
    Page<HmeServiceReceiveVO3> materialLovQuery(Long tenantId, HmeServiceReceiveDTO dto, PageRequest pageRequest);

    /**
     * 售后登记-确认保存
     * 
     * @param tenantId 租户ID
     * @param dto 保存数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/9/1 16:13:53
     * @return com.ruike.hme.api.dto.HmeServiceReceiveDTO2
     */
    HmeServiceReceiveDTO2 confirm(Long tenantId, HmeServiceReceiveDTO2 dto);
}
