package com.ruike.wms.infra.mapper;

import com.ruike.wms.domain.vo.WmsLocatorSiteVO;
import com.ruike.wms.domain.vo.WmsPrepareExecInsDocVO;
import com.ruike.wms.domain.vo.WmsPrepareExecInsVO;
import com.ruike.wms.domain.vo.WmsInstructionActualDetailVO;
import org.apache.ibatis.annotations.Param;
import tarzan.modeling.domain.entity.MtModLocator;

import java.util.List;

/**
 * 备料执行 mapper
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 16:54
 */
public interface WmsPrepareExecuteMapper {

    /**
     * 通过配送单号查询配送单
     *
     * @param tenantId          租户
     * @param instructionDocNum 配送单号
     * @return com.ruike.wms.domain.vo.WmsDistDemandInsDocVO
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 08:27:20
     */
    WmsPrepareExecInsDocVO selectDistDocByNum(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionDocNum") String instructionDocNum);

    /**
     * 通过单据ID查询配送单行
     *
     * @param tenantId         租户
     * @param instructionDocId 配送单ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsDistDemandInsVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/24 09:41:54
     */
    List<WmsPrepareExecInsVO> selectInsListByDocId(@Param(value = "tenantId") Long tenantId, @Param(value = "instructionDocId") String instructionDocId);

    /**
     * 通过站点查询推荐的备料货位
     *
     * @param tenantId        租户
     * @param materialId      物料
     * @param materialVersion 物料版本
     * @param siteId          站点ID
     * @param soNum           销售订单号
     * @param soLineNum       销售订单行号
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 04:05:04
     */
    MtModLocator selectDistLocatorBySiteId(@Param(value = "tenantId") Long tenantId
            , @Param(value = "materialId") String materialId
            , @Param(value = "materialVersion") String materialVersion
            , @Param(value = "siteId") String siteId
            , @Param(value = "soNum") String soNum
            , @Param(value = "soLineNum") String soLineNum);

    /**
     * 查询货位是否在线边仓
     *
     * @param tenantId  租户
     * @param locatorId 货位
     * @return tarzan.modeling.domain.entity.MtModLocator
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/25 04:05:04
     */
    MtModLocator selectLocatorOnWarehouse(@Param(value = "tenantId") Long tenantId
            , @Param(value = "locatorId") String locatorId);

    /**
     * 通过指令行ID查询指令实际
     *
     * @param tenantId      租户
     * @param instructionId 指令行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 02:56:17
     */
    List<WmsInstructionActualDetailVO> selectActualDetailByInstId(@Param(value = "tenantId") Long tenantId
            , @Param(value = "instructionId") String instructionId);

    /**
     * 通过指令ID查询指令实际
     *
     * @param tenantId         租户
     * @param instructionDocId 指令行ID
     * @return java.util.List<com.ruike.wms.domain.vo.WmsInstructionActualVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 02:56:17
     */
    List<WmsInstructionActualDetailVO> selectActualDetailByDocId(@Param(value = "tenantId") Long tenantId
            , @Param(value = "instructionDocId") String instructionDocId);

    /**
     * 根据条码查询货位站点信息
     *
     * @param tenantId     租户
     * @param loadTypeCode 标志
     * @param loadTypeId   货位
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 08:07:14
     */
    WmsLocatorSiteVO selectLocatorByBarcode(@Param(value = "tenantId") Long tenantId
            , @Param(value = "loadTypeCode") String loadTypeCode
            , @Param(value = "loadTypeId") String loadTypeId);

    /**
     * 根据货位ID查询货位站点信息
     *
     * @param tenantId  租户
     * @param locatorId 货位
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/27 08:07:14
     */
    WmsLocatorSiteVO selectLocatorById(@Param(value = "tenantId") Long tenantId
            , @Param(value = "locatorId") String locatorId);
}
