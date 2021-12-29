package com.ruike.hme.app.service;

import java.util.List;

import com.ruike.hme.domain.vo.*;

/**
 * <p>
 * COS检验工作台
 * </p>
 * 
 * @author yapeng.yao 2020/08/25 14:21
 */
public interface HmeCosInspectPlatformService {

    /**
     * 进入界面，自动查询信息
     * 
     * @param tenantId
     * @param requestVO
     * @return
     */
    List<HmeCosInspectPlatformAutoQueryInfoResponseVO> autoQueryInfo(Long tenantId,
                                                                     HmeCosInspectPlatformQueryInfoRequestVO requestVO);

    /**
     * 检索按钮
     * @param tenantId
     * @param requestVO
     * @return
     */
    List<HmeCosInspectPlatformAutoQueryInfoResponseVO> queryInfo(Long tenantId,
                                                                 HmeCosInspectPlatformQueryInfoRequestVO requestVO);
    /**
     * @Description COS检验平台-扫描盒子
     * @param tenantId
     * @param requestVO
     * @return
     */
    HmeCosInspectPlatformScanMaterialLotCodeResponseVO scanMaterialLotCode(Long tenantId,
                                                                           HmeCosInspectPlatformQueryInfoRequestVO requestVO);

    /**
     * @Description 点击行数据，查询物料装载信息
     * @param tenantId
     * @param requestVO
     * @return
     */
    HmeCosInspectPlatformScanMaterialLotCodeResponseVO queryLoadData(Long tenantId,
                                                                     HmeCosInspectPlatformQueryInfoRequestVO requestVO);

    /**
     *
     * @Description 查询芯片及新增数据采集项
     *
     * @author yapeng.yao
     * @date 2020/8/24 17:32
     * @param tenantId 租户ID
     * @param requestVO 参数queryLoadData
     * @return void
     *
     */
    List<HmeEoJobDataRecordVO> cosInspectionQuery(Long tenantId, HmeCosInspectPlatformCosInspectRequestVO requestVO);

    /**
     * 保存COS检验结果
     *  @param tenantId
     * @param vo
     */
    HmeEoJobDataRecordVO cosInspection(Long tenantId, HmeEoJobDataRecordVO vo);

    /**
     * COS检验平台-出站前校验
     * @param tenantId
     * @param responseVO
     */
    HmeCosInspectPlatformSiteOutRequestVO checkSiteOut(Long tenantId, HmeCosInspectPlatformSiteOutRequestVO responseVO);

    /**
     * COS检验平台-COS条码完成检验出站
     * 
     * @param tenantId
     * @param requestVO
     */
    void siteOut(Long tenantId, HmeCosInspectPlatformSiteOutRequestVO requestVO);


}
