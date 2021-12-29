package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.entity.HmeRetestImportData;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2021/1/19 13:52
 */
public interface HmeCosRetestRepository {

    /**
     * COS复测-条码扫描
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeCosRetestVO2
     * @author sanfeng.zhang@hand-china.com 2021/1/19 14:08
     */
    HmeCosRetestVO2 scanMaterialLot(Long tenantId, HmeCosRetestVO dto);

    /**
     * COS复测-确认投料
     *
     * @param tenantId
     * @param dto
     * @return
     * @author sanfeng.zhang@hand-china.com 2021/1/19 14:10
     */
    void cosRetestFeed(Long tenantId, HmeCosRetestVO3 dto);

    /**
     * COS报废复测-根据工单获取剩余COS数量
     *
     * @param tenantId
     * @param hmeCosRetestVO4
     * @return com.ruike.hme.domain.vo.HmeCosRetestVO4
     * @author sanfeng.zhang@hand-china.com 2021/1/20 14:02
     */
    HmeCosRetestVO4 overCosNumQuery(Long tenantId, HmeCosRetestVO4 hmeCosRetestVO4);

    /**
     * COS报废复测-COS类型列表
     *
     * @param tenantId
     * @param containerTypeId
     * @param operationId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeCosRetestVO6>
     * @author sanfeng.zhang@hand-china.com 2021/1/20 14:51
     */
    List<HmeCosRetestVO6> queryCosTypeByContainerType(Long tenantId, String containerTypeId, String operationId);

    /**
     * COS报废复测-条码扫描
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeCosRetestVO2
     * @author sanfeng.zhang@hand-china.com 2021/1/20 13:54
     */
    HmeCosRetestVO2 scrapScanMaterialLot(Long tenantId, HmeCosRetestVO dto);

    /**
     * COS报废复测-拆分
     *
     * @param tenantId
     * @param hmeCosRetestVO5
     * @return com.ruike.hme.domain.vo.HmeCosRetestVO5
     * @author sanfeng.zhang@hand-china.com 2021/1/20 14:10
     */
    HmeCosRetestVO5 cosScrapSplit(Long tenantId, HmeCosRetestVO5 hmeCosRetestVO5);

    /**
     * COS返厂复测-条码扫描
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeCosRetestVO8
     * @author sanfeng.zhang@hand-china.com 2021/1/24 23:59
     */
    HmeCosRetestVO8 backFactoryScanMaterialLot(Long tenantId, HmeCosRetestVO dto);

    /**
     * COS返厂复测-拆分
     *
     * @param tenantId
     * @param dto
     * @return com.ruike.hme.domain.vo.HmeCosRetestVO9
     * @author sanfeng.zhang@hand-china.com 2021/1/25 0:04
     */
    HmeCosRetestVO9 cosBackFactorySplit(Long tenantId, HmeCosRetestVO9 dto);

    /**
     * COS复测导入-查询头信息
     *
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeCosRetestImportVO3>
     * @author sanfeng.zhang@hand-china.com 2021/1/25 10:24
     */
    Page<HmeCosRetestImportVO3> cosRetestImportHeaderDataList(Long tenantId, HmeCosRetestImportVO2 dto, PageRequest pageRequest);

    /**
     * COS复测导入-查询行信息
     *
     * @param tenantId
     * @param retestImportDataId
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.entity.HmeRetestImportData>
     * @author sanfeng.zhang@hand-china.com 2021/1/25 10:26
     */
    Page<HmeRetestImportData> cosRetestImportLineList(Long tenantId, String retestImportDataId, PageRequest pageRequest);

    /**
     * COS复测导入-打印
     *
     * @param tenantId
     * @param retestImportDataIdList
     * @param response
     * @return void
     * @author sanfeng.zhang@hand-china.com 2021/1/25 10:45
     */
    void cosRetestPrint(Long tenantId, List<String> retestImportDataIdList, HttpServletResponse response);

    /**
     * 批量生成号码段
     *
     * @param tenantId
     * @param numQty
     * @param hmeCosRetestVO5
     * @author sanfeng.zhang@hand-china.com 2021/3/3 11:23
     * @return java.util.List<java.lang.String>
     */
    List<String> createBatchGenerateNum(Long tenantId, Integer numQty, HmeCosRetestVO5 hmeCosRetestVO5);
}
