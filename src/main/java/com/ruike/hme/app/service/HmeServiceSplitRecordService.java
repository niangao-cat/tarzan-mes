package com.ruike.hme.app.service;

import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3;
import com.ruike.hme.api.dto.HmeServiceSplitRecordDTO4;
import com.ruike.hme.domain.vo.HmeServiceSplitBomHeaderVO;
import com.ruike.hme.domain.vo.HmeServiceSplitMaterialLotVO;
import com.ruike.hme.domain.vo.HmeServiceSplitRecordVO;

/**
 * 售后返品拆机表应用服务
 *
 * @author jiangling.zheng@hand-china.com 2020-09-08 14:21:01
 */
public interface HmeServiceSplitRecordService {

    /**
     * SN编码扫描
     *
     * @param tenantId
     * @param vo
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO
     * @author jiangling.zheng@hand-china.com 2020/9/8 14:37
     */

    HmeServiceSplitRecordDTO snNumScan(Long tenantId, HmeServiceSplitRecordVO vo);

    /**
     * 查询组件信息
     *
     * @param tenantId
     * @param vo
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3
     * @author jiangling.zheng@hand-china.com 2020/9/9 16:22
     */
    HmeServiceSplitRecordDTO3 materialLotScan(Long tenantId, HmeServiceSplitMaterialLotVO vo);

    /**
     * 查询组件信息
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO3
     * @author jiangling.zheng@hand-china.com 2020/9/9 16:22
     */
    HmeServiceSplitRecordDTO3 split(Long tenantId, HmeServiceSplitRecordDTO3 dto);

    /**
     * bom信息获取
     *
     * @param tenantId   租户
     * @param siteId     站点
     * @param materialId 物料
     * @return com.ruike.hme.domain.vo.HmeServiceSplitBomHeaderVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/29 10:28:23
     */
    HmeServiceSplitBomHeaderVO bomGet(Long tenantId,
                                      String siteId,
                                      String materialId);

    /**
     * 登记撤销
     *
     * @param tenantId 租户ID
     * @param dto SN信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/5/25 02:49:04
     * @return com.ruike.hme.api.dto.HmeServiceSplitRecordDTO4
     */
    HmeServiceSplitRecordDTO4 registerCancel(Long tenantId, HmeServiceSplitRecordDTO4 dto);
}
