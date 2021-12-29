package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.WmsStocktakeDocQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeDocSelectQueryDTO;
import com.ruike.wms.api.dto.WmsStocktakeMaterialDetailQueryDTO;
import com.ruike.wms.domain.repository.WmsStocktakeDocRepository;
import com.ruike.wms.domain.repository.WmsStocktakeRangeRepository;
import com.ruike.wms.domain.vo.*;
import com.ruike.wms.infra.constant.WmsConstant;
import com.ruike.wms.infra.mapper.WmsStocktakeDocMapper;
import com.ruike.wms.infra.util.DatetimeUtils;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.stocktake.domain.entity.MtStocktakeDoc;
import tarzan.stocktake.infra.mapper.MtStocktakeDocMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 库存盘点单据 资源库实现
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/8/12 09:38
 */
@Component
public class WmsStocktakeDocRepositoryImpl implements WmsStocktakeDocRepository {

    private final WmsStocktakeRangeRepository rangeRepository;
    private final WmsStocktakeDocMapper docMapper;
    private final MtStocktakeDocMapper mtStocktakeDocMapper;

    @Autowired
    public WmsStocktakeDocRepositoryImpl(WmsStocktakeRangeRepository rangeRepository, WmsStocktakeDocMapper docMapper, MtStocktakeDocMapper mtStocktakeDocMapper) {
        this.rangeRepository = rangeRepository;
        this.docMapper = docMapper;
        this.mtStocktakeDocMapper = mtStocktakeDocMapper;
    }

    private void locatorGet(Long tenantId, WmsStocktakeDocVO vo) {
        List<WmsStocktakeRangeVO> locatorList = rangeRepository.selectListByDocId(tenantId, vo.getStocktakeId(), WmsConstant.StocktakeRangeObjectType.LOCATOR);
        if (CollectionUtils.isNotEmpty(locatorList) && locatorList.size() == 1) {
            vo.setLocatorCode(locatorList.get(0).getRangeObjectCode());
        }
    }

    @Override
    @ProcessLovValue
    public Page<WmsStocktakeDocVO> pageAndSort(Long tenantId, WmsStocktakeDocQueryDTO dto, PageRequest pageRequest) {
        Page<WmsStocktakeDocVO> page = PageHelper.doPage(pageRequest, () -> docMapper.selectListByCondition(dto));
        page.forEach(rec -> this.locatorGet(tenantId, rec));
        return page;
    }

    @Override
    public WmsStocktakeDocVO selectById(Long tenantId, String stocktakeId) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        WmsStocktakeDocQueryDTO dto = new WmsStocktakeDocQueryDTO();
        dto.setUserId(userDetails.getUserId());
        dto.setTenantId(tenantId);
        dto.setStocktakeId(stocktakeId);
        List<WmsStocktakeDocVO> list = docMapper.selectListByCondition(dto);
        if (CollectionUtils.isEmpty(list)) {
            return new WmsStocktakeDocVO();
        }
        WmsStocktakeDocVO wmsStocktakeDoc = list.get(0);
        this.locatorGet(tenantId, wmsStocktakeDoc);
        return wmsStocktakeDoc;
    }

    @Override
    public List<String> notNewStocktakeNums(Long tenantId, List<String> idList) {
        return docMapper.selectNotNewStocktakeNums(tenantId, idList);
    }

    @Override
    public List<String> notNewStocktakeIds(Long tenantId, List<String> idList) {
        return docMapper.selectNotNewStocktakeIds(tenantId, idList);
    }

    @Override
    @ProcessLovValue
    public List<WmsStocktakeDocSelectVO> stocktakeSelectLov(Long tenantId, WmsStocktakeDocSelectQueryDTO dto) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        dto.setUserId(userDetails.getUserId());
        dto.setTenantId(tenantId);
        if (Objects.nonNull(dto.getCreationDate())) {
            dto.setStartDate(DatetimeUtils.getBeginOfDate(dto.getCreationDate()));
            dto.setEndDate(DatetimeUtils.getEndOfDate(dto.getCreationDate()));
        }
        return docMapper.stocktakeSelectLov(dto);
    }

    @Override
    public WmsStocktakeDocSelectVO selectImplDocByNum(Long tenantId, String stocktakeNum) {
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        WmsStocktakeDocSelectVO doc = docMapper.selectStocktakeDocByScan(tenantId, userDetails.getUserId(), stocktakeNum);
        WmsCommonUtils.processValidateMessage(tenantId, Objects.isNull(doc), "WMS_STOCKTAKE_020",
                "WMS", stocktakeNum);
        return doc;
    }

    @Override
    public List<WmsStocktakeCostCenterVO> selectCostCenterByIds(Long tenantId, String stocktakeId) {
        return docMapper.selectCostCenterByIds(tenantId, stocktakeId);
    }

    @Override
    public List<WmsStocktakeMaterialDetailVO> selectMaterialDetailByIds(Long tenantId, WmsStocktakeMaterialDetailQueryDTO dto) {
        return docMapper.selectMaterialDetailByIds(tenantId, dto);
    }

    @Override
    public List<MtModLocator> selectStocktakeLocator(Long tenantId, String warehouseId) {
        return docMapper.selectStocktakeLocator(tenantId, warehouseId);
    }

    @Override
    public List<MtMaterial> selectStocktakeMaterial(Long tenantId, String warehouseId) {
        return docMapper.selectStocktakeMaterial(tenantId, warehouseId);
    }

    @Override
    public List<String> leakDocGet(Long tenantId, List<String> stocktakeIdList) {
        List<String> list = new ArrayList<>();
        for (String stocktakeId : stocktakeIdList) {
            if (docMapper.selectLeakByDoc(tenantId, stocktakeId) > 0) {
                MtStocktakeDoc doc = mtStocktakeDocMapper.selectByPrimaryKey(stocktakeId);
                list.add(doc.getStocktakeNum());
            }
        }
        return list;
    }

}
