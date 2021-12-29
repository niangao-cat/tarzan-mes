package com.ruike.wms.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.vo.HmeEquipmentImportVO;
import com.ruike.hme.domain.vo.HmeTagGroupImportVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO1;
import com.ruike.wms.api.dto.WmsDistributionBasicDataDTO2;
import com.ruike.wms.app.service.WmsDistributionBasicDataService;
import com.ruike.wms.domain.entity.WmsDistributionBasicData;
import com.ruike.wms.domain.entity.WmsDistributionBasicDataProductionLine;
import com.ruike.wms.domain.entity.WmsItemGroup;
import com.ruike.wms.domain.repository.WmsDistributionBasicDataRepository;
import com.ruike.wms.domain.repository.WmsItemGroupRepository;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO;
import com.ruike.wms.domain.vo.WmsDistributionBasicDataVO1;
import com.ruike.wms.domain.vo.WmsDistributionBasicVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.MtExtendSettingsVO2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.MtModOrganizationRelRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;
import tarzan.modeling.domain.vo.MtModOrganizationItemVO;
import tarzan.modeling.domain.vo.MtModOrganizationVO2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ruike.wms.infra.constant.WmsConstant.DistributionType.*;

/**
 * 配送基础数据表应用服务默认实现
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 21:05:25
 */
@Service
@ImportService(templateCode = "WMS.DISTRIBUTION_BASIC")
public class WmsDistributionBasicDataServiceImpl implements WmsDistributionBasicDataService, IBatchImportService {

    @Autowired
    private WmsDistributionBasicDataRepository wmsDistributionBasicDataRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;

    @Autowired
    private WmsItemGroupRepository wmsItemGroupRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;

    @Autowired
    private MtModOrganizationRelRepository mtModOrganizationRelRepository;

    @Override
    public Page<WmsDistributionBasicDataVO> query(Long tenantId, WmsDistributionBasicDataDTO2 dto, PageRequest pageRequest) {
        return wmsDistributionBasicDataRepository.query(tenantId, dto, pageRequest);
    }

    @Override
    public List<WmsDistributionBasicDataVO> dataExport(Long tenantId, WmsDistributionBasicDataDTO2 dto) {
        return wmsDistributionBasicDataRepository.dataExport(tenantId, dto);
    }

    @Override
    public void create(Long tenantId, WmsDistributionBasicDataDTO dto) {
        wmsDistributionBasicDataRepository.create(tenantId, dto);
    }

    @Override
    public void update(Long tenantId, WmsDistributionBasicDataVO dto) {
        wmsDistributionBasicDataRepository.update(tenantId, dto);
    }

    @Override
    public Page<WmsDistributionBasicDataVO1> processLovQuery(Long tenantId, WmsDistributionBasicDataVO1 dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "站点"));
        }
        if (StringUtils.isEmpty(dto.getProdLineIds())) {
            throw new MtException("HME_EQUIPMENT_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQUIPMENT_0001", "HME", "产线"));
        }
        Page<WmsDistributionBasicDataVO1> resultPage = new Page<>();
        List<WmsDistributionBasicDataVO1> resultList = new ArrayList<>();
        List<String> prodList = Arrays.asList(dto.getProdLineIds().split(","));
        //去重
        List<String> prodLineIdList = prodList.stream().distinct().collect(Collectors.toList());
        for(String prodLineId:prodLineIdList) {
            MtModProductionLine mtModProductionLine = new MtModProductionLine();
            mtModProductionLine.setTenantId(tenantId);
            mtModProductionLine.setProdLineId(prodLineId);
            mtModProductionLine = mtModProductionLineRepository.selectOne(mtModProductionLine);
            //调用API {subOrganizationRelQuery}根据产线找工段
            MtModOrganizationVO2 mtModOrganizationVO2 = new MtModOrganizationVO2();
            mtModOrganizationVO2.setTopSiteId(dto.getSiteId());
            mtModOrganizationVO2.setParentOrganizationType("PROD_LINE");
            mtModOrganizationVO2.setParentOrganizationId(prodLineId);
            mtModOrganizationVO2.setOrganizationType("WORKCELL");
            mtModOrganizationVO2.setQueryType("TOP");
            List<MtModOrganizationItemVO> mtModOrganizationItemVOS = mtModOrganizationRelRepository.subOrganizationRelQuery(tenantId, mtModOrganizationVO2);
            WmsDistributionBasicDataVO1 wmsDistributionBasicDataVO1 = null;
            for (MtModOrganizationItemVO vo : mtModOrganizationItemVOS) {
                boolean flag = true;
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(vo.getOrganizationId());
                wmsDistributionBasicDataVO1 = new WmsDistributionBasicDataVO1();
                wmsDistributionBasicDataVO1.setProdLineCode(mtModProductionLine.getProdLineCode());
                wmsDistributionBasicDataVO1.setProdLineId(prodLineId);
                wmsDistributionBasicDataVO1.setWorkcellId(vo.getOrganizationId());
                wmsDistributionBasicDataVO1.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                wmsDistributionBasicDataVO1.setWorkcellName(mtModWorkcell.getWorkcellName());
                if (StringUtils.isNotEmpty(dto.getWorkcellCode()) && !mtModWorkcell.getWorkcellCode().contains(dto.getWorkcellCode())) {
                    flag = false;
                }
                if (StringUtils.isNotEmpty(dto.getWorkcellName()) && !mtModWorkcell.getWorkcellName().contains(dto.getWorkcellName())) {
                    flag = false;
                }
                if (flag) {
                    resultList.add(wmsDistributionBasicDataVO1);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(resultList)) {
            List<WmsDistributionBasicDataVO1> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), resultList);
            resultPage = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), resultList.size());
        }
        return resultPage;
    }

    @Override
    public Boolean doImport(List<String> data) {
        //获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();

        if (CollectionUtils.isNotEmpty(data)) {
            List<WmsDistributionBasicDataDTO> basicDataDTOList = new ArrayList<>();
            List<WmsDistributionBasicDataVO> basicDataVOList = new ArrayList<>();
            for (String vo : data) {

                WmsDistributionBasicDataDTO basicDataDTO = new WmsDistributionBasicDataDTO();
                WmsDistributionBasicDataVO basicDataVO = new WmsDistributionBasicDataVO();
                WmsDistributionBasicVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, WmsDistributionBasicVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }

                //策略类型
                List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("WMS.DISTRIBUTION", tenantId);
                List<LovValueDTO> collect = lovValueDTOS.stream().filter(e -> StringUtils.equals(e.getMeaning(), importVO.getDistributionType())).collect(Collectors.toList());
                String distributionType = "";
                if (CollectionUtils.isNotEmpty(collect)) {
                    distributionType = collect.get(0).getValue();
                }

                //有效性
                List<LovValueDTO> flagList = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
                List<LovValueDTO> enableFlagList = flagList.stream().filter(e -> StringUtils.equals(e.getMeaning(), importVO.getEnableFlag())).collect(Collectors.toList());
                String enableFlag = "";
                if (CollectionUtils.isNotEmpty(enableFlagList)) {
                    enableFlag = enableFlagList.get(0).getValue();
                }

                //站点
                MtModSite mtModSite = new MtModSite();
                mtModSite.setSiteCode(importVO.getSiteCode());
                mtModSite.setEnableFlag(HmeConstants.ConstantValue.YES);
                List<MtModSite> siteList = mtModSiteRepository.select(mtModSite);
                if (CollectionUtils.isEmpty(siteList)) {
                    throw new MtException("HME_EXCEL_IMPORT_021", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_021", "HME", importVO.getSiteCode()));
                }

                if (StringUtils.isBlank(importVO.getMaterialGroupCode()) && StringUtils.isBlank(importVO.getMaterialCode())) {
                    throw new MtException("HME_EXCEL_IMPORT_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_EXCEL_IMPORT_019", "HME"));
                }

                //物料组
                String itemGroupCode = "";
                String itemGroupId = "";
                if (StringUtils.isNotBlank(importVO.getMaterialGroupCode())) {
                    WmsItemGroup wmsItemGroup = new WmsItemGroup();
                    wmsItemGroup.setItemGroupCode(importVO.getMaterialGroupCode());
                    List<WmsItemGroup> wmsItemGroupList = wmsItemGroupRepository.select(wmsItemGroup);
                    itemGroupCode = wmsItemGroupList.get(0).getItemGroupCode();
                    itemGroupId = wmsItemGroupList.get(0).getItemGroupId();
                }

                //物料
                String materialId = "";
                if (StringUtils.isNotBlank(importVO.getMaterialCode())) {
                    MtMaterial mtMaterial = new MtMaterial();
                    mtMaterial.setMaterialCode(importVO.getMaterialCode());
                    mtMaterial.setEnableFlag(HmeConstants.ConstantValue.YES);
                    List<MtMaterial> mtMaterialList = mtMaterialRepository.select(mtMaterial);
                    if (CollectionUtils.isNotEmpty(mtMaterialList)) {
                        materialId = mtMaterialList.get(0).getMaterialId();
                    }
                }

                //唯一性
                List<WmsDistributionBasicData> basicDataList = wmsDistributionBasicDataRepository.selectByCondition(Condition.builder(WmsDistributionBasicData.class)
                        .andWhere(Sqls.custom().andEqualTo(WmsDistributionBasicData.FIELD_TENANT_ID, tenantId)
                                .andEqualTo(WmsDistributionBasicData.FIELD_SITE_ID, siteList.get(0).getSiteId())
                                .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_GROUP_ID, itemGroupId)
                                .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_ID, materialId)
                                .andEqualTo(WmsDistributionBasicData.FIELD_MATERIAL_VERSION, importVO.getMaterialVersion())
                                .andEqualTo(WmsDistributionBasicData.FIELD_ENABLE_FLAG, enableFlag)).build());

                //若不存在，则create
                if (basicDataList.size() == 0) {
                    boolean flag = false;
                    for (WmsDistributionBasicDataDTO vo2 : basicDataDTOList) {
                        if (StringUtils.equals(importVO.getSiteCode(), vo2.getSiteCode()) && StringUtils.equals(importVO.getMaterialGroupCode(), vo2.getMaterialGroupCode()) && StringUtils.equals(importVO.getMaterialCode(), vo2.getMaterialCode())) {
                            flag = true;

                            if (StringUtils.isNotBlank(importVO.getProductionLineCode())) {
                                WmsDistributionBasicDataDTO1 lineDto = new WmsDistributionBasicDataDTO1();
                                List<WmsDistributionBasicDataDTO1> lineList = new ArrayList<>(vo2.getLineList());
                                //产线
                                MtModProductionLine productionLine = new MtModProductionLine();
                                productionLine.setProdLineCode(importVO.getProductionLineCode());
                                productionLine.setEnableFlag(HmeConstants.ConstantValue.YES);
                                List<MtModProductionLine> prodLineList = mtModProductionLineRepository.select(productionLine);
                                if (CollectionUtils.isEmpty(prodLineList)) {
                                    throw new MtException("HME_EXCEL_IMPORT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EXCEL_IMPORT_020", "HME", importVO.getProductionLineCode()));
                                }
                                lineDto.setProdLineId(prodLineList.get(0).getProdLineId());

                                //工段
                                if(StringUtils.isNotBlank(importVO.getWorkcellCode())) {
                                    MtModWorkcell mtModWorkcell = new MtModWorkcell();
                                    mtModWorkcell.setTenantId(tenantId);
                                    mtModWorkcell.setWorkcellCode(importVO.getWorkcellCode());
                                    mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                                    if (Objects.isNull(mtModWorkcell)) {
                                        // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                                        throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "MT_ASSEMBLE_0004", "ASSEMBLE", "工段", importVO.getWorkcellCode()));
                                    }
                                    lineDto.setWorkcellId(mtModWorkcell.getWorkcellId());
                                }
                                //安全库存值
                                lineDto.setEveryQty(importVO.getEveryQty());

                                //是否启用线边库存计算逻辑
                                lineDto.setBackflushFlag(importVO.getBackflushFlag());

                                lineList.add(lineDto);
                                vo2.setLineList(lineList);
                            }
                            break;
                        }
                    }

                    if (flag) {
                        continue;
                    }
                    //处理数据
                    BeanUtils.copyProperties(importVO, basicDataDTO);
                    basicDataDTO.setDistributionType(distributionType);
                    basicDataDTO.setEnableFlag(enableFlag);
                    basicDataDTO.setSiteId(siteList.get(0).getSiteId());
                    basicDataDTO.setSiteCode(importVO.getSiteCode());
                    basicDataDTO.setMaterialGroupId(itemGroupId);
                    basicDataDTO.setMaterialGroupCode(importVO.getMaterialGroupCode());
                    basicDataDTO.setMaterialId(materialId);

                    if (StringUtils.isNotBlank(basicDataDTO.getDistributionType())) {
                        switch (basicDataDTO.getDistributionType()) {
                            case MIN_MAX:
                                if (basicDataDTO.getInventoryLevel() == null) {
                                    throw new MtException("WMS_DIS_BASIC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_DIS_BASIC_0002", "WMS"));
                                }
                                break;
                            case PROPORTION_DISTRIBUTION:
                                if (basicDataDTO.getProportion() == null) {
                                    throw new MtException("WMS_DIS_BASIC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_DIS_BASIC_0003", "WMS"));
                                }
                                break;
                            case PACKAGE_DELIVERY:
                                if (basicDataDTO.getMinimumPackageQty() == null) {
                                    throw new MtException("WMS_DIS_BASIC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_DIS_BASIC_0004", "WMS"));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    WmsDistributionBasicDataDTO1 lineDto = new WmsDistributionBasicDataDTO1();
                    List<WmsDistributionBasicDataDTO1> lineList = new ArrayList<>();
                    //产线
                    MtModProductionLine productionLine = new MtModProductionLine();
                    productionLine.setProdLineCode(importVO.getProductionLineCode());
                    productionLine.setEnableFlag(HmeConstants.ConstantValue.YES);
                    List<MtModProductionLine> prodLineList = mtModProductionLineRepository.select(productionLine);
                    if (CollectionUtils.isEmpty(prodLineList)) {
                        throw new MtException("HME_EXCEL_IMPORT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_020", "HME", importVO.getProductionLineCode()));
                    }
                    lineDto.setProdLineId(prodLineList.get(0).getProdLineId());

                    //工段
                    if(StringUtils.isNotBlank(importVO.getWorkcellCode())) {
                        MtModWorkcell mtModWorkcell = new MtModWorkcell();
                        mtModWorkcell.setTenantId(tenantId);
                        mtModWorkcell.setWorkcellCode(importVO.getWorkcellCode());
                        mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                        if (Objects.isNull(mtModWorkcell)) {
                            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "工段", importVO.getWorkcellCode()));
                        }
                        lineDto.setWorkcellId(mtModWorkcell.getWorkcellId());
                    }
                    //安全库存值
                    lineDto.setEveryQty(importVO.getEveryQty());

                    //是否启用线边库存计算逻辑
                    lineDto.setBackflushFlag(importVO.getBackflushFlag());

                    lineList.add(lineDto);
                    basicDataDTO.setLineList(lineList);
                    basicDataDTOList.add(basicDataDTO);
                }
                //存在，则update
                else{
                    basicDataVO.setHeaderId(basicDataList.get(0).getHeaderId());
                    boolean flag = false;
                    for (WmsDistributionBasicDataVO vo2 : basicDataVOList) {
                        if (StringUtils.equals(importVO.getSiteCode(), vo2.getSiteCode()) && StringUtils.equals(importVO.getMaterialGroupCode(), vo2.getMaterialGroupCode()) && StringUtils.equals(importVO.getMaterialCode(), vo2.getMaterialCode())) {
                            flag = true;

                            if (StringUtils.isNotBlank(importVO.getProductionLineCode())) {
                                //List<String> proLineList = new ArrayList<>(vo2.getProdLineIdList());
                                WmsDistributionBasicDataProductionLine lineDto = new WmsDistributionBasicDataProductionLine();
                                List<WmsDistributionBasicDataProductionLine> lineList = new ArrayList<>(vo2.getWmsDistributionBasicDataProductionLines());
                                //产线
                                MtModProductionLine productionLine = new MtModProductionLine();
                                productionLine.setProdLineCode(importVO.getProductionLineCode());
                                productionLine.setEnableFlag(HmeConstants.ConstantValue.YES);
                                List<MtModProductionLine> prodLineList = mtModProductionLineRepository.select(productionLine);
                                if (CollectionUtils.isEmpty(prodLineList)) {
                                    throw new MtException("HME_EXCEL_IMPORT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "HME_EXCEL_IMPORT_020", "HME", importVO.getProductionLineCode()));
                                }
                                lineDto.setProductionLineId(prodLineList.get(0).getProdLineId());

                                //工段
                                if(StringUtils.isNotBlank(importVO.getWorkcellCode())) {
                                    MtModWorkcell mtModWorkcell = new MtModWorkcell();
                                    mtModWorkcell.setTenantId(tenantId);
                                    mtModWorkcell.setWorkcellCode(importVO.getWorkcellCode());
                                    mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                                    if (Objects.isNull(mtModWorkcell)) {
                                        // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                                        throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                                "MT_ASSEMBLE_0004", "ASSEMBLE", "工段", importVO.getWorkcellCode()));
                                    }
                                    lineDto.setWorkcellId(mtModWorkcell.getWorkcellId());
                                }
                                //安全库存值
                                lineDto.setEveryQty(importVO.getEveryQty());

                                //是否启用线边库存计算逻辑
                                lineDto.setBackflushFlag(importVO.getBackflushFlag());

                                lineList.add(lineDto);
                                vo2.setWmsDistributionBasicDataProductionLines(lineList);
                            }
                            break;
                        }
                    }

                    if (flag) {
                        continue;
                    }
                    //处理数据
                    BeanUtils.copyProperties(importVO, basicDataVO);
                    basicDataVO.setDistributionType(distributionType);
                    basicDataVO.setEnableFlag(enableFlag);
                    basicDataVO.setSiteId(siteList.get(0).getSiteId());
                    basicDataVO.setSiteCode(importVO.getSiteCode());
                    basicDataVO.setMaterialGroupId(itemGroupId);
                    basicDataVO.setMaterialGroupCode(importVO.getMaterialGroupCode());
                    basicDataVO.setMaterialId(materialId);

                    if (StringUtils.isNotBlank(basicDataVO.getDistributionType())) {
                        switch (basicDataVO.getDistributionType()) {
                            case MIN_MAX:
                                if (basicDataVO.getInventoryLevel() == null) {
                                    throw new MtException("WMS_DIS_BASIC_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_DIS_BASIC_0002", "WMS"));
                                }
                                break;
                            case PROPORTION_DISTRIBUTION:
                                if (basicDataVO.getProportion() == null) {
                                    throw new MtException("WMS_DIS_BASIC_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_DIS_BASIC_0003", "WMS"));
                                }
                                break;
                            case PACKAGE_DELIVERY:
                                if (basicDataVO.getMinimumPackageQty() == null) {
                                    throw new MtException("WMS_DIS_BASIC_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                            "WMS_DIS_BASIC_0004", "WMS"));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    WmsDistributionBasicDataProductionLine lineDto = new WmsDistributionBasicDataProductionLine();
                    List<WmsDistributionBasicDataProductionLine> lineList = new ArrayList<>();
                    //产线
                    MtModProductionLine productionLine = new MtModProductionLine();
                    productionLine.setProdLineCode(importVO.getProductionLineCode());
                    productionLine.setEnableFlag(HmeConstants.ConstantValue.YES);
                    List<MtModProductionLine> prodLineList = mtModProductionLineRepository.select(productionLine);
                    if (CollectionUtils.isEmpty(prodLineList)) {
                        throw new MtException("HME_EXCEL_IMPORT_020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "HME_EXCEL_IMPORT_020", "HME", importVO.getProductionLineCode()));
                    }
                    lineDto.setProductionLineId(prodLineList.get(0).getProdLineId());

                    //工段
                    if(StringUtils.isNotBlank(importVO.getWorkcellCode())) {
                        MtModWorkcell mtModWorkcell = new MtModWorkcell();
                        mtModWorkcell.setTenantId(tenantId);
                        mtModWorkcell.setWorkcellCode(importVO.getWorkcellCode());
                        mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
                        if (Objects.isNull(mtModWorkcell)) {
                            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "MT_ASSEMBLE_0004", "ASSEMBLE", "工段", importVO.getWorkcellCode()));
                        }
                        lineDto.setWorkcellId(mtModWorkcell.getWorkcellId());
                    }
                    //安全库存值
                    lineDto.setEveryQty(importVO.getEveryQty());

                    //是否启用线边库存计算逻辑
                    lineDto.setBackflushFlag(importVO.getBackflushFlag());

                    lineList.add(lineDto);
                    basicDataVO.setWmsDistributionBasicDataProductionLines(lineList);
                    basicDataVOList.add(basicDataVO);
                }
            }
            //新增
            for (WmsDistributionBasicDataDTO basicDataDTO : basicDataDTOList) {

                wmsDistributionBasicDataRepository.create(tenantId, basicDataDTO);
            }
            //更新
            for(WmsDistributionBasicDataVO basicDataVO:basicDataVOList){
                wmsDistributionBasicDataRepository.update(tenantId, basicDataVO);
            }
        }
        return true;
    }
}
