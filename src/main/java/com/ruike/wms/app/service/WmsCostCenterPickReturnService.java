package com.ruike.wms.app.service;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.vo.WmsPickReturnDetailReceiveVO;
import com.ruike.wms.domain.vo.WmsPickReturnHeadAndLine;
import com.ruike.wms.domain.vo.WmsPickReturnReceiveVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.core.cache.ProcessCacheValue;
import tarzan.inventory.domain.entity.MtInvOnhandQuantity;
import tarzan.modeling.domain.entity.MtModLocator;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 成本中心领退料
 *
 * @author han.zhang 2020/04/16 13:48
 */
public interface WmsCostCenterPickReturnService {

    /**
     * 成本中心领退料头信息查询
     *
     * @param tenantId                  租户id
     * @param pageRequest               页码请求
     * @param wmsCostCenterPickReturnVO 查询参数
     * @return Page<WmsPickReturnReceiveVO>
     * @author han.zhang 2020-04-16 13:58
     */
    @ProcessCacheValue
    Page<WmsPickReturnReceiveVO> costHeadQuery(Long tenantId, PageRequest pageRequest, WmsCostCenterPickReturnVO wmsCostCenterPickReturnVO);

    /**
     * 成本中心领退料行查询
     *
     * @param tenantId         租户id
     * @param pageRequest      页码
     * @param instructionDocId 头id
     * @return com.ruike.wms.domain.vo.WmsPickReturnReceiveVO
     * @author han.zhang 2020-04-17 15:55
     */
    Page<WmsPickReturnLineReceiveVO> costLineQuery(Long tenantId, PageRequest pageRequest, String instructionDocId);

    /**
     * 新增数据
     *
     * @param tenantId 租户id
     * @param dto      创建参数
     * @return com.ruike.wms.api.dto.WmsPickReturnAddReturnDTO
     * @author han.zhang 2020-04-17 15:54
     */
    WmsPickReturnAddReturnDTO createOrder(Long tenantId, WmsPickReturnAddDTO dto);

    /**
     * 查询仓库
     *
     * @param tenantId 租户ID
     * @param siteId   地点ID
     * @return java.util.List<tarzan.modeling.domain.vo.MtModLocatorOrgRelVO>
     * @author han.zhang 2020-05-06 16:44
     */
    List<MtModLocator> selectStorage(Long tenantId, String siteId);

    /**
     * 查询货位
     *
     * @param tenantId  租户ID
     * @param locatorId 货位ID
     * @return java.util.List<tarzan.modeling.domain.entity.MtModLocator>
     * @author han.zhang 2020-05-06 16:58
     */
    List<MtModLocator> selectLocator(Long tenantId, String locatorId);

    /**
     * 头行信息一起查询
     *
     * @param tenantId         租户ID
     * @param pageRequest      分页参数
     * @param instructionDocId 头ID
     * @return com.ruike.wms.domain.vo.WmsPickReturnHeadAndLine
     * @author han.zhang 2020-05-28 19:24
     */
    WmsPickReturnHeadAndLine costCenterOrderQueryHeadAndLine(Long tenantId, PageRequest pageRequest, String instructionDocId);

    /**
     * 明细查询
     *
     * @param tenantId      租户id
     * @param pageRequest   分页参数
     * @param dto 参数
     * @return com.ruike.wms.domain.vo.WmsPickReturnHeadAndLine
     * @author han.zhang 2020-06-02 09:49
     */
    Page<WmsPickReturnDetailReceiveVO> costCenterOrderQueryDetails(Long tenantId, PageRequest pageRequest, WmsCostCenterOrderQueryDTO dto);

    /**
     * 打印
     *
     * @param tenantId                租户ID
     * @param wmsPickReturnReceiveVoS 打印数据
     * @return java.util.List<com.ruike.wms.domain.vo.WmsPickReturnReceiveVO>
     * @author han.zhang 2020-06-16 16:32
     */
    List<WmsPickReturnReceiveVO> print(Long tenantId, List<WmsPickReturnReceiveVO> wmsPickReturnReceiveVoS);

    /**
     * 领料单打印
     * @param tenantId 租户ID
     * @param instructionDocIdList 单据ID集合
     * @param response
     */
    void printPdf(Long tenantId, List<String>  instructionDocIdList, HttpServletResponse response);

    /**
     * 库存量查询
     *
     * @param tenantId
     * @param receiveVOList
     * @return java.util.List<com.ruike.wms.api.dto.WmsPickReturnLineReceiveVO>
     * @author sanfeng.zhang@hand-china.com 2020/10/10 16:45
     */
    List<WmsPickReturnLineReceiveVO> queryLocatorQuantity(Long tenantId, List<WmsPickReturnLineReceiveVO> receiveVOList);

    /**
     * 单据取消
     *
     * @param tenantId
     * @param instructionDocId
     * @return void
     * @author sanfeng.zhang@hand-china.com 2020/12/10 10:58
     */
    void closeInstructionDoc(Long tenantId, String instructionDocId);
}