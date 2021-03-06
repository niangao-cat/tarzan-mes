package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeWipStocktakeDocService;
import com.ruike.hme.domain.entity.HmeWipStocktakeDoc;
import com.ruike.hme.domain.entity.HmeWipStocktakeRange;
import com.ruike.hme.domain.repository.HmeWipStocktakeDocRepository;
import com.ruike.hme.domain.repository.HmeWipStocktakeRangeRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWipStocktakeDocMapper;
import com.ruike.wms.api.dto.WmsStocktakeRangeQueryDTO;
import com.ruike.wms.infra.util.WmsCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.domain.PageInfo;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtUserRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.sys.MtUserInfo;
import io.tarzan.common.domain.util.PageUtil;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.method.domain.entity.MtBom;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;
import tarzan.modeling.domain.repository.MtModAreaRepository;
import tarzan.modeling.domain.repository.MtModProductionLineRepository;
import tarzan.modeling.domain.repository.MtModWorkcellRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.*;

/**
 * ???????????????????????????????????????
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@Service
@Slf4j
public class HmeWipStocktakeDocServiceImpl implements HmeWipStocktakeDocService {

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private HmeWipStocktakeDocMapper hmeWipStocktakeDocMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtUserRepository mtUserRepository;
    @Autowired
    private HmeWipStocktakeDocRepository hmeWipStocktakeDocRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private HmeWipStocktakeRangeRepository hmeWipStocktakeRangeRepository;

    @Override
    @ProcessLovValue
    public Page<HmeWipStocktakeDocVO> wipStocktakeDocPageQuery(Long tenantId, HmeWipStocktakeDocDTO dto, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocVO> resultPage = new Page<>();
        CustomUserDetails curUser = DetailsHelper.getUserDetails();
        Long userId = curUser == null ? -1L : curUser.getUserId();
        //????????????????????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getSiteId())) {
            List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, new MtUserOrganization() {{
                setOrganizationType("SITE");
                setUserId(userId);
            }});
            if (isEmpty(mtUserOrganizationList)) {
                return resultPage;
            } else {
                List<String> siteIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).distinct().collect(Collectors.toList());
                dto.setSiteIdList(siteIdList);
            }
        }
        //????????????????????????????????????????????????????????????
        if (StringUtils.isEmpty(dto.getAreaId())) {
            List<MtUserOrganization> mtUserOrganizationList = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, new MtUserOrganization() {{
                setOrganizationType("AREA");
                setUserId(userId);
            }});
            if (isEmpty(mtUserOrganizationList)) {
                return resultPage;
            } else {
                List<String> areaIdList = mtUserOrganizationList.stream().map(MtUserOrganization::getOrganizationId).distinct().collect(Collectors.toList());
                HmeWipStocktakeDocDTO2 hmeWipStocktakeDocDTO2 = new HmeWipStocktakeDocDTO2();
                List<HmeWipStocktakeDocDTO2> mtModAreas = hmeWipStocktakeDocMapper.departmentListQuery(tenantId, areaIdList, hmeWipStocktakeDocDTO2);
                if (isEmpty(mtModAreas)) {
                    return resultPage;
                } else {
                    dto.setAreaIdList(mtModAreas.stream().map(HmeWipStocktakeDocDTO2::getAreaId).distinct().collect(Collectors.toList()));
                }
            }
        }
        resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.wipStocktakeDocPageQuery(tenantId, dto));
        for (HmeWipStocktakeDocVO result : resultPage.getContent()) {
            //??????????????????????????????????????????????????????????????????????????????LOV????????????
            List<String> prodLineRangeList = hmeWipStocktakeDocMapper.plStocktakeRangeQuery(tenantId, result.getStocktakeId());
            result.setProdLineRangeList(prodLineRangeList);
        }
        return resultPage;
    }

    @Override
    public Page<HmeWipStocktakeDocVO4> stocktakeRangePageQuery(Long tenantId, WmsStocktakeRangeQueryDTO dto, PageRequest pageRequest) {
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????ID"));
        }
        if (StringUtils.isEmpty(dto.getRangeObjectType())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????????????????"));
        }
        return PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.stocktakeRangePageQuery(tenantId, dto));
    }

    @Override
    @ProcessLovValue
    public Page<HmeWipStocktakeDocVO2> wipStocktakeDetailPageQuery(Long tenantId, HmeWipStocktakeDocDTO5 dto, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocVO2> resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.wipStocktakeDetailPageQuery(tenantId, dto));
        if (isNotEmpty(resultPage.getContent())) {
            //List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
            //List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
            for (HmeWipStocktakeDocVO2 hmeWipStocktakeDocVO2 : resultPage) {
//                if (cosItemGroupList.contains(hmeWipStocktakeDocVO2.getItemGroup())) {
//                    //?????????COS?????????
//                    MtBom mtBom = hmeWipStocktakeDocMapper.cosItemGroupQuery(tenantId, hmeWipStocktakeDocVO2.getMaterialLotId());
//                    if (Objects.nonNull(mtBom)) {
//                        hmeWipStocktakeDocVO2.setBomName(mtBom.getBomName());
//                        hmeWipStocktakeDocVO2.setDescription(mtBom.getDescription());
//                    }
//                } else {
//                    //????????????COS?????????
//                    MtBom mtBom = hmeWipStocktakeDocMapper.noCosItemGroupQuery(tenantId, hmeWipStocktakeDocVO2.getMaterialLotCode());
//                    if (Objects.nonNull(mtBom)) {
//                        hmeWipStocktakeDocVO2.setBomName(mtBom.getBomName());
//                        hmeWipStocktakeDocVO2.setDescription(mtBom.getDescription());
//                    }
//                }
                if (Objects.nonNull(hmeWipStocktakeDocVO2.getFirstcountBy())) {
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeWipStocktakeDocVO2.getFirstcountBy());
                    hmeWipStocktakeDocVO2.setFirstcountByName(mtUserInfo.getRealName());
                }
                if (Objects.nonNull(hmeWipStocktakeDocVO2.getRecountBy())) {
                    MtUserInfo reCountUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeWipStocktakeDocVO2.getRecountBy());
                    hmeWipStocktakeDocVO2.setRecountByName(reCountUserInfo.getRealName());
                }
            }
        }
        return resultPage;
    }

    @Override
    public Page<HmeWipStocktakeDocVO3> wipStocktakeSumPageQuery(Long tenantId, HmeWipStocktakeDocDTO7 dto, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocVO3> resultPage = new Page<>();
        if (StringUtils.isEmpty(dto.getFirstcountConsistentFlag()) && StringUtils.isEmpty(dto.getRecountConsistentFlag())) {
            resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.wipStocktakeSumPageQuery(tenantId, dto));
            for (HmeWipStocktakeDocVO3 hmeWipStocktakeDocVO3 : resultPage) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getMaterialId());
                hmeWipStocktakeDocVO3.setMaterialCode(mtMaterial.getMaterialCode());
                hmeWipStocktakeDocVO3.setMaterialName(mtMaterial.getMaterialName());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                hmeWipStocktakeDocVO3.setUomId(mtMaterial.getPrimaryUomId());
                hmeWipStocktakeDocVO3.setUomCode(mtUom.getUomCode());
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getProdLineId());
                hmeWipStocktakeDocVO3.setProdLineCode(mtModProductionLine.getProdLineCode());
                hmeWipStocktakeDocVO3.setProdLineName(mtModProductionLine.getProdLineName());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getWorkcellId());
                hmeWipStocktakeDocVO3.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                hmeWipStocktakeDocVO3.setWorkcellName(mtModWorkcell.getWorkcellName());
                //????????????
                BigDecimal currentQuantity = hmeWipStocktakeDocMapper.currentQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                        hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                hmeWipStocktakeDocVO3.setCurrentQuantity(currentQuantity);
                //????????????
                BigDecimal firstcountQuantity = hmeWipStocktakeDocMapper.firstcountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                        hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                hmeWipStocktakeDocVO3.setFirstcountQuantity(firstcountQuantity);
                //????????????
                BigDecimal recountQuantity = hmeWipStocktakeDocMapper.recountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                        hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                hmeWipStocktakeDocVO3.setRecountQuantity(recountQuantity);
                //????????????
                hmeWipStocktakeDocVO3.setFirstcountDiff(firstcountQuantity.subtract(currentQuantity));
                //????????????
                hmeWipStocktakeDocVO3.setRecountDiff(recountQuantity.subtract(currentQuantity));
            }
        } else {
            List<HmeWipStocktakeDocVO3> hmeWipStocktakeDocVO3List = hmeWipStocktakeDocMapper.wipStocktakeSumPageQuery(tenantId, dto);
            if (isNotEmpty(hmeWipStocktakeDocVO3List)) {
                for (HmeWipStocktakeDocVO3 hmeWipStocktakeDocVO3 : hmeWipStocktakeDocVO3List) {
                    //????????????
                    BigDecimal currentQuantity = hmeWipStocktakeDocMapper.currentQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                            hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                    hmeWipStocktakeDocVO3.setCurrentQuantity(currentQuantity);
                    //????????????
                    BigDecimal firstcountQuantity = hmeWipStocktakeDocMapper.firstcountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                            hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                    hmeWipStocktakeDocVO3.setFirstcountQuantity(firstcountQuantity);
                    //????????????
                    BigDecimal recountQuantity = hmeWipStocktakeDocMapper.recountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                            hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                    hmeWipStocktakeDocVO3.setRecountQuantity(recountQuantity);
                    //????????????
                    hmeWipStocktakeDocVO3.setFirstcountDiff(firstcountQuantity.subtract(currentQuantity));
                    //????????????
                    hmeWipStocktakeDocVO3.setRecountDiff(recountQuantity.subtract(currentQuantity));
                }
                if (StringUtils.isNotBlank(dto.getFirstcountConsistentFlag())) {
                    if ("Y".equals(dto.getFirstcountConsistentFlag())) {
                        //?????????????????????0
                        hmeWipStocktakeDocVO3List = hmeWipStocktakeDocVO3List.stream().filter(data ->
                                data.getFirstcountDiff().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
                    } else {
                        //??????????????????0
                        hmeWipStocktakeDocVO3List = hmeWipStocktakeDocVO3List.stream().filter(data ->
                                data.getFirstcountDiff().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
                    }
                }
                if (StringUtils.isNotBlank(dto.getRecountConsistentFlag())) {
                    if ("Y".equals(dto.getRecountConsistentFlag())) {
                        //?????????????????????0
                        hmeWipStocktakeDocVO3List = hmeWipStocktakeDocVO3List.stream().filter(data ->
                                data.getRecountDiff().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
                    } else {
                        //??????????????????0
                        hmeWipStocktakeDocVO3List = hmeWipStocktakeDocVO3List.stream().filter(data ->
                                data.getRecountDiff().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
                    }
                }
                if (isNotEmpty(hmeWipStocktakeDocVO3List)) {
                    for (HmeWipStocktakeDocVO3 hmeWipStocktakeDocVO3 : hmeWipStocktakeDocVO3List) {
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getMaterialId());
                        hmeWipStocktakeDocVO3.setMaterialCode(mtMaterial.getMaterialCode());
                        hmeWipStocktakeDocVO3.setMaterialName(mtMaterial.getMaterialName());
                        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                        hmeWipStocktakeDocVO3.setUomId(mtMaterial.getPrimaryUomId());
                        hmeWipStocktakeDocVO3.setUomCode(mtUom.getUomCode());
                        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getProdLineId());
                        hmeWipStocktakeDocVO3.setProdLineCode(mtModProductionLine.getProdLineCode());
                        hmeWipStocktakeDocVO3.setProdLineName(mtModProductionLine.getProdLineName());
                        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getWorkcellId());
                        hmeWipStocktakeDocVO3.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                        hmeWipStocktakeDocVO3.setWorkcellName(mtModWorkcell.getWorkcellName());
                    }
                    List<HmeWipStocktakeDocVO3> pagedList = PageUtil.pagedList(pageRequest.getPage(), pageRequest.getSize(), hmeWipStocktakeDocVO3List);
                    resultPage = new Page<>(pagedList, new PageInfo(pageRequest.getPage(), pageRequest.getSize()), hmeWipStocktakeDocVO3List.size());
                }
            }
        }
        return resultPage;
    }

    @Override
    public List<MtMaterial> getMaterialByMaterialGroup(Long tenantId, String itemGroupId) {
        return hmeWipStocktakeDocMapper.getMaterialByMaterialGroup(tenantId, itemGroupId);
    }

    @Override
    public List<MtModProductionLine> getProdLineByAreaId(Long tenantId, String areaId) {
        return hmeWipStocktakeDocMapper.getProdLineByAreaId(tenantId, areaId);
    }

    @Override
    public List<MtModWorkcell> getWorkcellByProdLineId(Long tenantId, String prodLineId) {
        return hmeWipStocktakeDocMapper.getWorkcellByProdLineId(tenantId, prodLineId);
    }

    @Override
    public HmeWipStocktakeDocDTO6 createStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO6 dto) {
        if (isEmpty(dto.getProdLineIdList())) {
            //????????????????????????????????????????????????
            throw new MtException("WMS_WIP_STOCKTAKE_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WIP_STOCKTAKE_016", "WMS"));
        }
        //??????
        List<String> prodLineList = dto.getProdLineIdList().stream().distinct().collect(Collectors.toList());
        dto.setProdLineIdList(prodLineList);
        //???????????????????????????????????????COS????????????COS??????
        List<String> cosFlagList = new ArrayList<>();
        for (String prodLineId:dto.getProdLineIdList()) {
            //????????????ID???????????????COS_FLAG
            String cosFlag = null;
            MtExtendVO mtExtendVO = new MtExtendVO();
            mtExtendVO.setTableName("mt_mod_production_line_attr");
            mtExtendVO.setKeyId(prodLineId);
            mtExtendVO.setAttrName("COS_FLAG");
            List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
            if(isNotEmpty(extendAttrList) && HmeConstants.ConstantValue.YES.equals(extendAttrList.get(0).getAttrValue())){
                cosFlag = HmeConstants.ConstantValue.YES;
            }else{
                cosFlag = HmeConstants.ConstantValue.NO;
            }
            cosFlagList.add(cosFlag);
        }
        cosFlagList = cosFlagList.stream().distinct().collect(Collectors.toList());
        if(cosFlagList.size() > 1){
            throw new MtException("WMS_WIP_STOCKTAKE_017", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WIP_STOCKTAKE_017", "WMS"));
        }
        if (isEmpty(dto.getWorkcellIdList()) && isNotEmpty(dto.getProdLineIdList())) {
            //???????????????????????????????????????????????????????????????????????????????????????
            List<String> workcellIdList = hmeWipStocktakeDocMapper.getWorkcellByProdLineList(tenantId, dto.getProdLineIdList());
            dto.setWorkcellIdList(workcellIdList);
        }else{
            //????????????
            List<String> workcellList = dto.getWorkcellIdList().stream().distinct().collect(Collectors.toList());
            dto.setWorkcellIdList(workcellList);
        }
        if(CollectionUtils.isNotEmpty(dto.getMaterialIdList())){
            //????????????
            List<String> materialList = dto.getMaterialIdList().stream().distinct().collect(Collectors.toList());
            dto.setMaterialIdList(materialList);
        }
        //???????????????????????????????????????/????????????????????????
        Long count = hmeWipStocktakeDocMapper.stocktakeCountByProdLineQuery(tenantId, dto.getProdLineIdList());
        if(count > 0){
            //??????????????????????????????????????????????????????
            if(CollectionUtils.isNotEmpty(dto.getMaterialIdList())){
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????+???????????????????????????/???????????????????????????
                //?????????????????????????????????????????????NEW/RELEASED??????????????????
                String stocktakeNum = hmeWipStocktakeDocMapper.stocktakeNumByWorkcellQuery(tenantId, dto.getWorkcellIdList());
                if(StringUtils.isNotBlank(stocktakeNum)){
                    //???????????????????????????????????????${1}??????????????????????????????????????????????????????,?????????????????????,?????????!
                    throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_019", "WMS", stocktakeNum));
                }
                //????????????+???????????????????????????/??????????????????????????????????????????
                List<String> stocktakeIdWorkcellList = hmeWipStocktakeDocMapper.stocktakeIdWorkcellQuery(tenantId, dto.getWorkcellIdList());
                if (isNotEmpty(stocktakeIdWorkcellList)) {
                    List<String> totalStocktakeIdMaterialList = new ArrayList<>();
                    List<List<String>> list = WmsCommonUtils.splitSqlList(dto.getMaterialIdList(), 500);
                    for (List<String> materialIdList : list) {
                        List<String> stocktakeIdMaterialList = hmeWipStocktakeDocMapper.stocktakeIdMaterialQuery(tenantId, materialIdList);
                        totalStocktakeIdMaterialList.addAll(stocktakeIdMaterialList);
                    }
                    if (isNotEmpty(totalStocktakeIdMaterialList)) {
                        totalStocktakeIdMaterialList = totalStocktakeIdMaterialList.stream().distinct().collect(Collectors.toList());
                        //????????????????????????????????????
                        stocktakeIdWorkcellList.retainAll(totalStocktakeIdMaterialList);
                        if (isNotEmpty(stocktakeIdWorkcellList)) {
                            HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(stocktakeIdWorkcellList.get(0));
                            throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_WIP_STOCKTAKE_019", "WMS", hmeWipStocktakeDoc.getStocktakeNum()));
                        }
                    }
                }
                //???????????????????????????????????????
                hmeWipStocktakeDocRepository.createStocktakeDoc(tenantId, dto, cosFlagList.get(0));
            }else{
                //???????????????????????????????????????????????????????????????????????????????????????????????????/???????????????????????????
                HmeWipStocktakeDocDTO8 hmeWipStocktakeDocDTO8 = hmeWipStocktakeDocMapper.errorMeageQuery(tenantId, dto.getWorkcellIdList());
                if(Objects.nonNull(hmeWipStocktakeDocDTO8)){
                    throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_019", "WMS", hmeWipStocktakeDocDTO8.getStocktakeNum()));
                }
                //???????????????????????????????????????
                hmeWipStocktakeDocRepository.createStocktakeDoc(tenantId, dto, cosFlagList.get(0));
            }
        }else{
            //????????????????????????????????????????????????
            hmeWipStocktakeDocRepository.createStocktakeDoc(tenantId, dto, cosFlagList.get(0));
        }

        //?????????????????????????????????????????????????????????????????????????????????
//        if (isEmpty(dto.getMaterialIdList())) {
//            //????????????HME.COS_ITEM_GROUP????????????
//            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
//            List<String> itemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
//            List<String> totalMaterialList = hmeWipStocktakeDocMapper.getMaterialIdByDepartment(tenantId, dto.getSiteId(), dto.getAreaId(), itemGroupList);
//            dto.setMaterialIdList(totalMaterialList);
//        }
        return dto;
    }

    @Override
    public HmeWipStocktakeDoc updateStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO9 dto) {
        if (StringUtils.isEmpty(dto.getStocktakeId())) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????ID"));
        }
        HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(dto.getStocktakeId());
        if (!"NEW".equals(hmeWipStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("WMS_WIP_STOCKTAKE_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WIP_STOCKTAKE_004", "WMS", hmeWipStocktakeDoc.getStocktakeNum()));
        }
        return hmeWipStocktakeDocRepository.updateStocktakeDoc(tenantId, dto);
    }

    @Override
    public HmeWipStocktakeDocDTO10 deleteStocktakeRange(Long tenantId, HmeWipStocktakeDocDTO10 dto) {
        rangeCreateValid(tenantId, dto.getStocktakeId(), dto.getRangeObjectType());
        if ("PL".equals(dto.getRangeObjectType())) {
            //??????????????????????????????????????????,?????????????????????????????????????????????
            List<String> prodLineIdList = hmeWipStocktakeDocMapper.rangeObjectIdByStocktake(tenantId, dto.getStocktakeId(), "PL");
            List<String> delProdLineIdList = dto.getDeleteList().stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).collect(Collectors.toList());
            prodLineIdList.removeIf(item -> delProdLineIdList.contains(item));
            if (CollectionUtils.isEmpty(prodLineIdList)) {
                throw new MtException("WMS_WIP_STOCKTAKE_016", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_016", "WMS"));
            }
            //????????????????????????
            List<String> stocktakeRangeIdList = dto.getDeleteList().stream().map(HmeWipStocktakeDocVO4::getStocktakeRangeId).collect(Collectors.toList());
            hmeWipStocktakeDocRepository.deleteStocktakeRange(tenantId, stocktakeRangeIdList);
            //??????????????????????????????????????????????????????????????????????????????????????????
            for (String prodLineId : delProdLineIdList) {
                List<String> stocktakeRangeWpIdList = hmeWipStocktakeDocMapper.getStocktakeRangeByProdLine(tenantId, dto.getStocktakeId(), prodLineId);
                if (isNotEmpty(stocktakeRangeWpIdList)) {
                    hmeWipStocktakeDocRepository.deleteStocktakeRange(tenantId, stocktakeRangeWpIdList);
                }
            }

        }else if ("WP".equals(dto.getRangeObjectType())) {
            //????????????????????????
            List<String> stocktakeRangeIdList = dto.getDeleteList().stream().map(HmeWipStocktakeDocVO4::getStocktakeRangeId).collect(Collectors.toList());
            hmeWipStocktakeDocRepository.deleteStocktakeRange(tenantId, stocktakeRangeIdList);
        }else if("MATERIAL".equals(dto.getRangeObjectType())){
            //??????????????????????????????????????????
            List<String> materialIdList = hmeWipStocktakeDocMapper.rangeObjectIdByStocktake(tenantId, dto.getStocktakeId(), "MATERIAL");
            List<String> delMaterialIdList = dto.getDeleteList().stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).collect(Collectors.toList());
            materialIdList.removeIf(item -> delMaterialIdList.contains(item));
            if (CollectionUtils.isEmpty(materialIdList)) {
                //??????????????????????????????????????????????????????????????????????????????????????????????????????
                List<String> workcellIdList = hmeWipStocktakeDocMapper.rangeObjectIdByStocktake(tenantId, dto.getStocktakeId(), "WP");
                if(CollectionUtils.isEmpty(workcellIdList)){
                    throw new MtException("WMS_WIP_STOCKTAKE_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_023", "WMS"));
                }
                //???????????????????????????????????????????????????????????????/?????????????????????????????????????????????????????????
                HmeWipStocktakeDocDTO8 hmeWipStocktakeDocDTO8 = hmeWipStocktakeDocMapper.errorMeageQuery(tenantId, workcellIdList);
                if(Objects.nonNull(hmeWipStocktakeDocDTO8)){
                    throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_019", "WMS", hmeWipStocktakeDocDTO8.getStocktakeNum()));
                }
                //????????????????????????
                List<String> stocktakeRangeIdList = dto.getDeleteList().stream().map(HmeWipStocktakeDocVO4::getStocktakeRangeId).collect(Collectors.toList());
                hmeWipStocktakeDocRepository.deleteStocktakeRange(tenantId, stocktakeRangeIdList);
            }else{
                //???????????????????????????????????????,?????????????????????????????????
                List<String> stocktakeRangeIdList = dto.getDeleteList().stream().map(HmeWipStocktakeDocVO4::getStocktakeRangeId).collect(Collectors.toList());
                hmeWipStocktakeDocRepository.deleteStocktakeRange(tenantId, stocktakeRangeIdList);
            }
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public HmeWipStocktakeDocDTO11 addStocktakeRange(Long tenantId, HmeWipStocktakeDocDTO11 dto) {
        rangeCreateValid(tenantId, dto.getStocktakeId(), dto.getRangeObjectType());
        return rangeCreate(tenantId, dto);
    }

    private void rangeCreateValid(Long tenantId, String stocktakeId, String rangeObjectType) {
        if (StringUtils.isEmpty(stocktakeId)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "?????????ID"));
        }
        if (StringUtils.isEmpty(rangeObjectType)) {
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "??????????????????"));
        }
        HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(stocktakeId);
        if (!"NEW".equals(hmeWipStocktakeDoc.getStocktakeStatus())) {
            throw new MtException("WMS_WIP_STOCKTAKE_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_WIP_STOCKTAKE_004", "WMS", hmeWipStocktakeDoc.getStocktakeNum()));
        }
    }

    @Override
    public HmeWipStocktakeDocDTO11 rangeCreate(Long tenantId, HmeWipStocktakeDocDTO11 dto) {
        // ??????????????????????????????????????????????????????????????????????????????
        WmsStocktakeRangeQueryDTO wmsStocktakeRangeQueryDTO = new WmsStocktakeRangeQueryDTO();
        wmsStocktakeRangeQueryDTO.setStocktakeId(dto.getStocktakeId());
        wmsStocktakeRangeQueryDTO.setRangeObjectType(dto.getRangeObjectType());
        List<HmeWipStocktakeDocVO4> stocktakeRangeList = hmeWipStocktakeDocMapper.stocktakeRangePageQuery(tenantId, wmsStocktakeRangeQueryDTO);
        Set<String> rangeObjectIdList = stocktakeRangeList.stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).collect(Collectors.toSet());
        dto.getAddList().removeIf(rec -> rangeObjectIdList.contains(rec.getRangeObjectId()));

        HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(dto.getStocktakeId());
        if("PL".equals(dto.getRangeObjectType())){
            //?????????????????????????????????????????????????????????ID???COS_FLAG???????????????????????????????????????
            List<String> prodLineIdList = dto.getAddList().stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).distinct().collect(Collectors.toList());
            for (String prodLineId:prodLineIdList) {
                //????????????ID???????????????COS_FLAG
                String cosFlag = null;
                MtExtendVO mtExtendVO = new MtExtendVO();
                mtExtendVO.setTableName("mt_mod_production_line_attr");
                mtExtendVO.setKeyId(prodLineId);
                mtExtendVO.setAttrName("COS_FLAG");
                List<MtExtendAttrVO> extendAttrList = mtExtendSettingsRepository.attrPropertyQuery(tenantId, mtExtendVO);
                if(isNotEmpty(extendAttrList) && HmeConstants.ConstantValue.YES.equals(extendAttrList.get(0).getAttrValue())){
                    cosFlag = HmeConstants.ConstantValue.YES;
                }else{
                    cosFlag = HmeConstants.ConstantValue.NO;
                }
                if(!cosFlag.equals(hmeWipStocktakeDoc.getAttribute1())){
                    throw new MtException("WMS_WIP_STOCKTAKE_022", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_022", "WMS"));
                }
            }
        }else if ("WP".equals(dto.getRangeObjectType())) {
            int materialCount = hmeWipStocktakeRangeRepository.selectCount(new HmeWipStocktakeRange() {{
                setTenantId(tenantId);
                setStocktakeId(dto.getStocktakeId());
                setRangeObjectType("MATERIAL");
            }});
            List<String> workcellIdList = dto.getAddList().stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).distinct().collect(Collectors.toList());
            if(materialCount > 0){
                //?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????+???????????????????????????/???????????????????????????
                //?????????????????????????????????????????????NEW/RELEASED??????????????????
                String stocktakeNum = hmeWipStocktakeDocMapper.stocktakeNumByWorkcellQuery(tenantId, workcellIdList);
                if(StringUtils.isNotBlank(stocktakeNum)){
                    //???????????????????????????????????????${1}??????????????????????????????????????????????????????,?????????????????????,?????????!
                    throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_019", "WMS", stocktakeNum));
                }
                //????????????+???????????????????????????/??????????????????????????????????????????
                List<String> stocktakeIdWorkcellList = hmeWipStocktakeDocMapper.stocktakeIdWorkcellQuery(tenantId, workcellIdList);
                if (isNotEmpty(stocktakeIdWorkcellList)) {
                    WmsStocktakeRangeQueryDTO wmsStocktakeRangeQueryDTO2 = new WmsStocktakeRangeQueryDTO();
                    wmsStocktakeRangeQueryDTO2.setStocktakeId(dto.getStocktakeId());
                    wmsStocktakeRangeQueryDTO2.setRangeObjectType("MATERIAL");
                    List<HmeWipStocktakeDocVO4> stocktakeRangeList2 = hmeWipStocktakeDocMapper.stocktakeRangePageQuery(tenantId, wmsStocktakeRangeQueryDTO2);
                    List<String> rangeMaterialIdList = stocktakeRangeList2.stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).distinct().collect(Collectors.toList());

                    List<String> totalStocktakeIdMaterialList = new ArrayList<>();
                    List<List<String>> list = WmsCommonUtils.splitSqlList(rangeMaterialIdList, 500);
                    for (List<String> materialIdList : list) {
                        List<String> stocktakeIdMaterialList = hmeWipStocktakeDocMapper.stocktakeIdMaterialQuery(tenantId, materialIdList);
                        totalStocktakeIdMaterialList.addAll(stocktakeIdMaterialList);
                    }
                    if (isNotEmpty(totalStocktakeIdMaterialList)) {
                        totalStocktakeIdMaterialList = totalStocktakeIdMaterialList.stream().distinct().collect(Collectors.toList());
                        //????????????????????????????????????
                        stocktakeIdWorkcellList.retainAll(totalStocktakeIdMaterialList);
                        if (isNotEmpty(stocktakeIdWorkcellList)) {
                            HmeWipStocktakeDoc hmeWipStocktakeDoc2 = hmeWipStocktakeDocRepository.selectByPrimaryKey(stocktakeIdWorkcellList.get(0));
                            throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "WMS_WIP_STOCKTAKE_019", "WMS", hmeWipStocktakeDoc2.getStocktakeNum()));
                        }
                    }
                }
            }else{
                //?????????????????????????????????????????????????????????????????????????????????????????????/???????????????????????????
                HmeWipStocktakeDocDTO8 hmeWipStocktakeDocDTO8 = hmeWipStocktakeDocMapper.errorMeageQuery(tenantId, workcellIdList);
                if(Objects.nonNull(hmeWipStocktakeDocDTO8)){
                    throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_WIP_STOCKTAKE_019", "WMS", hmeWipStocktakeDocDTO8.getStocktakeNum()));
                }
            }
        } else if ("MATERIAL".equals(dto.getRangeObjectType())) {
            //???????????????????????????????????????????????????????????????????????????????????????????????????
            List<String> workcellIdList = hmeWipStocktakeDocMapper.rangeObjectIdByStocktake(tenantId, dto.getStocktakeId(), "WP");
            if(CollectionUtils.isEmpty(workcellIdList)){
                throw new MtException("WMS_WIP_STOCKTAKE_023", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_023", "WMS"));
            }
            //??????????????????+?????????????????????????????????????????????????????????????????????NEW/RELEASED???????????????????????????
            List<String> totalMaterialIdList = dto.getAddList().stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).distinct().collect(Collectors.toList());
            List<String> totalStocktakeIdMaterialList = new ArrayList<>();
            List<List<String>> list = WmsCommonUtils.splitSqlList(totalMaterialIdList, 500);
            for (List<String> materialIdList : list) {
                List<String> stocktakeIdMaterialList = hmeWipStocktakeDocMapper.stocktakeIdMaterialQuery(tenantId, materialIdList);
                totalStocktakeIdMaterialList.addAll(stocktakeIdMaterialList);
            }
            if (isNotEmpty(totalStocktakeIdMaterialList)) {
                totalStocktakeIdMaterialList = totalStocktakeIdMaterialList.stream().distinct().collect(Collectors.toList());

                WmsStocktakeRangeQueryDTO wmsStocktakeRangeQueryDTO2 = new WmsStocktakeRangeQueryDTO();
                wmsStocktakeRangeQueryDTO2.setStocktakeId(dto.getStocktakeId());
                wmsStocktakeRangeQueryDTO2.setRangeObjectType("WP");
                List<HmeWipStocktakeDocVO4> stocktakeRangeList2 = hmeWipStocktakeDocMapper.stocktakeRangePageQuery(tenantId, wmsStocktakeRangeQueryDTO2);
                List<String> rangeWorkcellIdList = stocktakeRangeList2.stream().map(HmeWipStocktakeDocVO4::getRangeObjectId).distinct().collect(Collectors.toList());

                List<String> stocktakeIdWorkcellList = hmeWipStocktakeDocMapper.stocktakeIdWorkcellQuery(tenantId, rangeWorkcellIdList);
                if (isNotEmpty(stocktakeIdWorkcellList)) {
                    //????????????????????????????????????
                    stocktakeIdWorkcellList.retainAll(totalStocktakeIdMaterialList);
                    if (isNotEmpty(stocktakeIdWorkcellList)) {
                        HmeWipStocktakeDoc hmeWipStocktakeDoc2 = hmeWipStocktakeDocRepository.selectByPrimaryKey(stocktakeIdWorkcellList.get(0));
                        throw new MtException("WMS_WIP_STOCKTAKE_019", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_WIP_STOCKTAKE_019", "WMS", hmeWipStocktakeDoc2.getStocktakeNum()));
                    }
                }
            }
        }
        return hmeWipStocktakeDocRepository.addStocktakeRange(tenantId, dto);
    }

    @Override
    public List<String> plStocktakeRangeQuery(Long tenantId, String stocktakeId) {
        return hmeWipStocktakeDocMapper.plStocktakeRangeQuery(tenantId, stocktakeId);
    }

    @Override
    public HmeWipStocktakeDocDTO12 releasedWipStocktake(Long tenantId, HmeWipStocktakeDocDTO12 hmeWipStocktakeDocDTO12) {
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(dto.getStocktakeId());
            if (!"NEW".equals(hmeWipStocktakeDoc.getStocktakeStatus())) {
                //??????????????????????????????NEW???????????????
                String statusMeaning = lovAdapter.queryLovMeaning("WMS.STOCKTAKE_STATUS", tenantId, hmeWipStocktakeDoc.getStocktakeStatus());
                throw new MtException("WMS_WIP_STOCKTAKE_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_001", "WMS", hmeWipStocktakeDoc.getStocktakeNum(), statusMeaning));
            }
            //??????????????????????????????????????????
            List<String> workcellIdList = hmeWipStocktakeDocMapper.rangeObjectIdByStocktake(tenantId, dto.getStocktakeId(), "WP");
            if(CollectionUtils.isEmpty(workcellIdList)){
                throw new MtException("WMS_WIP_STOCKTAKE_024", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_024", "WMS", hmeWipStocktakeDoc.getStocktakeNum()));
            }
            //?????????????????????????????????????????????????????????????????????
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            MtUserOrganization mtUserOrganization = mtUserOrganizationRepository.selectOne(new MtUserOrganization() {{
                setTenantId(tenantId);
                setUserId(userId);
                setOrganizationType("AREA");
                setOrganizationId(hmeWipStocktakeDoc.getAreaId());
            }});
            if (Objects.isNull(mtUserOrganization)) {
                MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(hmeWipStocktakeDoc.getAreaId());
                throw new MtException("WMS_WIP_COST_CENTER_0067", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_COST_CENTER_0067", "WMS", mtModArea.getAreaCode()));
            }
        }
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            hmeWipStocktakeDocRepository.releasedWipStocktake(tenantId, dto);
        }
        return hmeWipStocktakeDocDTO12;
    }

    @Override
    public HmeWipStocktakeDocVO6 completedWipStocktakeValidate(Long tenantId, HmeWipStocktakeDocDTO12 hmeWipStocktakeDocDTO12) {
        HmeWipStocktakeDocVO6 result = new HmeWipStocktakeDocVO6();
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(dto.getStocktakeId());
            if (!"RELEASED".equals(hmeWipStocktakeDoc.getStocktakeStatus())) {
                //??????????????????????????????RELEASED???????????????
                throw new MtException("WMS_WIP_STOCKTAKE_011", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_011", "WMS"));
            }
        }
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            //??????????????????????????????????????????
            List<String> firstcountQuantityNullData = hmeWipStocktakeDocMapper.getFirstcountQuantityNullData(tenantId, dto.getStocktakeId());
            if (isNotEmpty(firstcountQuantityNullData)) {
                result.setFlag(false);
                return result;
            }
        }
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            hmeWipStocktakeDocRepository.completedWipStocktake(tenantId, dto);
        }
        result.setFlag(true);
        return result;
    }

    @Override
    public HmeWipStocktakeDocDTO12 completedWipStocktake(Long tenantId, HmeWipStocktakeDocDTO12 hmeWipStocktakeDocDTO12) {
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            hmeWipStocktakeDocRepository.completedWipStocktake(tenantId, dto);
        }
        return hmeWipStocktakeDocDTO12;
    }

    @Override
    public HmeWipStocktakeDocVO6 closedWipStocktakeValidate(Long tenantId, HmeWipStocktakeDocDTO12 hmeWipStocktakeDocDTO12) {
        HmeWipStocktakeDocVO6 result = new HmeWipStocktakeDocVO6();
        boolean flag = true;
        long startDate = System.currentTimeMillis();
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(dto.getStocktakeId());
            if (!"RELEASED".equals(hmeWipStocktakeDoc.getStocktakeStatus()) && !"NEW".equals(hmeWipStocktakeDoc.getStocktakeStatus())) {
                //??????????????????????????????RELEASED???NEW???????????????
                throw new MtException("WMS_WIP_STOCKTAKE_012", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_012", "WMS"));
            }
            if ("RELEASED".equals(hmeWipStocktakeDoc.getStocktakeStatus())) {
                //???????????????????????????RELEASED, ???????????????????????????
                flag = false;
            }
        }
        long endDate = System.currentTimeMillis();
        log.info("<====????????????????????? ?????????????????????{}??????", (endDate - startDate));
        if (!flag) {
            result.setFlag(flag);
            return result;
        }
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            hmeWipStocktakeDocRepository.closedWipStocktake(tenantId, dto);
        }
        result.setFlag(true);
        return result;
    }

    @Override
    public HmeWipStocktakeDocDTO12 closedWipStocktake(Long tenantId, HmeWipStocktakeDocDTO12 hmeWipStocktakeDocDTO12) {
        for (HmeWipStocktakeDocVO dto : hmeWipStocktakeDocDTO12.getHmeWipStocktakeDocList()) {
            hmeWipStocktakeDocRepository.closedWipStocktake(tenantId, dto);
        }
        return hmeWipStocktakeDocDTO12;
    }

    @Override
    public Page<MtModWorkcell> processQueryByProdLineIdList(Long tenantId, HmeWipStocktakeDocDTO13 dto, PageRequest pageRequest) {
        Page<MtModWorkcell> resultPage = new Page<>();
        if (isNotEmpty(dto.getProdLineIdList())) {
            resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.processQueryByProdLineIdList(tenantId, dto));
        }
        return resultPage;
    }

    @Override
    public Page<HmeWipStocktakeDocVO7> releaseDetailPageQuery(Long tenantId, HmeWipStocktakeDocDTO15 dto, PageRequest pageRequest) {
        Page<HmeWipStocktakeDocVO7> resultPage = new Page<>();
        List<String> stocktakeIdList = dto.getStocktakeIdList();
        if (isEmpty(stocktakeIdList)) {
            return resultPage;
        }
        for (String stocktakeId:stocktakeIdList) {
            HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(stocktakeId);
            //??????????????????COS?????????????????????
            if(!HmeConstants.ConstantValue.NO.equals(hmeWipStocktakeDoc.getAttribute1())){
                throw new MtException("WMS_WIP_STOCKTAKE_025", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_WIP_STOCKTAKE_025", "WMS", hmeWipStocktakeDoc.getStocktakeNum()));
            }
        }
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.releaseDetailPageQueryNoCos(tenantId, stocktakeIdList, cosItemGroupList, dto));
        for (HmeWipStocktakeDocVO7 result : resultPage) {
            //????????????
            result.setReleaseQty(result.getReleaseQty().subtract(result.getScrapQty()));
        }
        return resultPage;
    }

    @Override
    public Page<MtModProductionLine> prodLinePageQuery(Long tenantId, HmeWipStocktakeDocDTO14 dto, PageRequest pageRequest) {
        Page<MtModProductionLine> resultPage = new Page<>();
        if (isEmpty(dto.getProdLineIdList())) {
            return resultPage;
        }
        resultPage = PageHelper.doPage(pageRequest, () -> hmeWipStocktakeDocMapper.prodLinePageQuery(tenantId, dto));
        return resultPage;
    }

    @Override
    public List<HmeWipStocktakeDocVO8> cosInventoryExport(Long tenantId, String stocktakeNum) {
        List<HmeWipStocktakeDocVO8> hmeWipStocktakeDocList = hmeWipStocktakeDocMapper.cosInventoryExport(tenantId, stocktakeNum);
        for (HmeWipStocktakeDocVO8 docVO8 : hmeWipStocktakeDocList) {
            // ???????????? 1,1 -> A1
            if (docVO8.getLoadRow() != null && docVO8.getLoadColumn() != null) {
                docVO8.setPosition((char) (64 + Integer.parseInt(docVO8.getLoadRow().toString())) + docVO8.getLoadColumn().toString());
            }
            // ??????????????????????????? ????????????????????? ????????????
            if (docVO8.getFirstcountQuantity() != null && docVO8.getRecountQuantity() != null) {
                docVO8.setDiffQty(docVO8.getRecountQuantity().subtract(docVO8.getFirstcountQuantity()));
            }
        }
        return hmeWipStocktakeDocList;
    }

    @Override
    public List<HmeWipStocktakeDocVO9> wipStocktakeDetailExport(Long tenantId, HmeWipStocktakeDocDTO5 dto) {
        List<HmeWipStocktakeDocVO9> resultList = hmeWipStocktakeDocMapper.wipStocktakeDetailExport(tenantId, dto);
        if (isNotEmpty(resultList)) {
//            List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
//            List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
            List<LovValueDTO> mfFlagLov = lovAdapter.queryLovValue("WMS.MF_FLAG", tenantId);
            for (HmeWipStocktakeDocVO9 hmeWipStocktakeDocVO9 : resultList) {
                //????????????????????????
                if(StringUtils.isNotBlank(hmeWipStocktakeDocVO9.getReworkFlag())){
                    List<LovValueDTO> mfFlagMeaning = mfFlagLov.stream().filter(item -> hmeWipStocktakeDocVO9.getReworkFlag().equals(item.getValue())).collect(Collectors.toList());
                    if(CollectionUtils.isNotEmpty(mfFlagMeaning)){
                        hmeWipStocktakeDocVO9.setReworkFlagMeaning(mfFlagMeaning.get(0).getMeaning());
                    }
                }
//                if (cosItemGroupList.contains(hmeWipStocktakeDocVO9.getItemGroup())) {
//                    //?????????COS?????????
//                    MtBom mtBom = hmeWipStocktakeDocMapper.cosItemGroupQuery(tenantId, hmeWipStocktakeDocVO9.getMaterialLotId());
//                    if (Objects.nonNull(mtBom)) {
//                        hmeWipStocktakeDocVO9.setBomName(mtBom.getBomName());
//                        hmeWipStocktakeDocVO9.setDescription(mtBom.getDescription());
//                    }
//                } else {
//                    //????????????COS?????????
//                    MtBom mtBom = hmeWipStocktakeDocMapper.noCosItemGroupQuery(tenantId, hmeWipStocktakeDocVO9.getMaterialLotCode());
//                    if (Objects.nonNull(mtBom)) {
//                        hmeWipStocktakeDocVO9.setBomName(mtBom.getBomName());
//                        hmeWipStocktakeDocVO9.setDescription(mtBom.getDescription());
//                    }
//                }
                if (Objects.nonNull(hmeWipStocktakeDocVO9.getFirstcountBy())) {
                    MtUserInfo mtUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeWipStocktakeDocVO9.getFirstcountBy());
                    hmeWipStocktakeDocVO9.setFirstcountByName(mtUserInfo.getRealName());
                }
                if (Objects.nonNull(hmeWipStocktakeDocVO9.getRecountBy())) {
                    MtUserInfo reCountUserInfo = mtUserRepository.userPropertyGet(tenantId, hmeWipStocktakeDocVO9.getRecountBy());
                    hmeWipStocktakeDocVO9.setRecountByName(reCountUserInfo.getRealName());
                }
            }
        }
        return resultList;
    }

    @Override
    public List<HmeWipStocktakeDocVO10> wipStocktakeSumExport(Long tenantId, HmeWipStocktakeDocDTO7 dto) {
        List<HmeWipStocktakeDocVO10> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(dto.getFirstcountConsistentFlag()) && StringUtils.isEmpty(dto.getRecountConsistentFlag())) {
            resultList = hmeWipStocktakeDocMapper.wipStocktakeSumExport(tenantId, dto);
            for (HmeWipStocktakeDocVO10 hmeWipStocktakeDocVO3 : resultList) {
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getMaterialId());
                hmeWipStocktakeDocVO3.setMaterialCode(mtMaterial.getMaterialCode());
                hmeWipStocktakeDocVO3.setMaterialName(mtMaterial.getMaterialName());
                MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                hmeWipStocktakeDocVO3.setUomId(mtMaterial.getPrimaryUomId());
                hmeWipStocktakeDocVO3.setUomCode(mtUom.getUomCode());
                MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getProdLineId());
                hmeWipStocktakeDocVO3.setProdLineCode(mtModProductionLine.getProdLineCode());
                hmeWipStocktakeDocVO3.setProdLineName(mtModProductionLine.getProdLineName());
                MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getWorkcellId());
                hmeWipStocktakeDocVO3.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                hmeWipStocktakeDocVO3.setWorkcellName(mtModWorkcell.getWorkcellName());
                //????????????
                BigDecimal currentQuantity = hmeWipStocktakeDocMapper.currentQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                        hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                hmeWipStocktakeDocVO3.setCurrentQuantity(currentQuantity);
                //????????????
                BigDecimal firstcountQuantity = hmeWipStocktakeDocMapper.firstcountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                        hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                hmeWipStocktakeDocVO3.setFirstcountQuantity(firstcountQuantity);
                //????????????
                BigDecimal recountQuantity = hmeWipStocktakeDocMapper.recountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                        hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                hmeWipStocktakeDocVO3.setRecountQuantity(recountQuantity);
                //????????????
                hmeWipStocktakeDocVO3.setFirstcountDiff(firstcountQuantity.subtract(currentQuantity));
                //????????????
                hmeWipStocktakeDocVO3.setRecountDiff(recountQuantity.subtract(currentQuantity));
            }
        } else {
            resultList = hmeWipStocktakeDocMapper.wipStocktakeSumExport(tenantId, dto);
            if (isNotEmpty(resultList)) {
                for (HmeWipStocktakeDocVO10 hmeWipStocktakeDocVO3 : resultList) {
                    //????????????
                    BigDecimal currentQuantity = hmeWipStocktakeDocMapper.currentQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                            hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                    hmeWipStocktakeDocVO3.setCurrentQuantity(currentQuantity);
                    //????????????
                    BigDecimal firstcountQuantity = hmeWipStocktakeDocMapper.firstcountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                            hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                    hmeWipStocktakeDocVO3.setFirstcountQuantity(firstcountQuantity);
                    //????????????
                    BigDecimal recountQuantity = hmeWipStocktakeDocMapper.recountQuantityQuery(tenantId, hmeWipStocktakeDocVO3.getMaterialId(),
                            hmeWipStocktakeDocVO3.getProdLineId(), hmeWipStocktakeDocVO3.getWorkcellId(), dto.getStocktakeIdList());
                    hmeWipStocktakeDocVO3.setRecountQuantity(recountQuantity);
                    //????????????
                    hmeWipStocktakeDocVO3.setFirstcountDiff(firstcountQuantity.subtract(currentQuantity));
                    //????????????
                    hmeWipStocktakeDocVO3.setRecountDiff(recountQuantity.subtract(currentQuantity));
                }
                if (StringUtils.isNotBlank(dto.getFirstcountConsistentFlag())) {
                    if ("Y".equals(dto.getFirstcountConsistentFlag())) {
                        //?????????????????????0
                        resultList = resultList.stream().filter(data ->
                                data.getFirstcountDiff().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
                    } else {
                        //??????????????????0
                        resultList = resultList.stream().filter(data ->
                                data.getFirstcountDiff().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
                    }
                }
                if (StringUtils.isNotBlank(dto.getRecountConsistentFlag())) {
                    if ("Y".equals(dto.getRecountConsistentFlag())) {
                        //?????????????????????0
                        resultList = resultList.stream().filter(data ->
                                data.getRecountDiff().compareTo(BigDecimal.ZERO) == 0).collect(Collectors.toList());
                    } else {
                        //??????????????????0
                        resultList = resultList.stream().filter(data ->
                                data.getRecountDiff().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());
                    }
                }
                if (isNotEmpty(resultList)) {
                    for (HmeWipStocktakeDocVO10 hmeWipStocktakeDocVO3 : resultList) {
                        MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getMaterialId());
                        hmeWipStocktakeDocVO3.setMaterialCode(mtMaterial.getMaterialCode());
                        hmeWipStocktakeDocVO3.setMaterialName(mtMaterial.getMaterialName());
                        MtUom mtUom = mtUomRepository.selectByPrimaryKey(mtMaterial.getPrimaryUomId());
                        hmeWipStocktakeDocVO3.setUomId(mtMaterial.getPrimaryUomId());
                        hmeWipStocktakeDocVO3.setUomCode(mtUom.getUomCode());
                        MtModProductionLine mtModProductionLine = mtModProductionLineRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getProdLineId());
                        hmeWipStocktakeDocVO3.setProdLineCode(mtModProductionLine.getProdLineCode());
                        hmeWipStocktakeDocVO3.setProdLineName(mtModProductionLine.getProdLineName());
                        MtModWorkcell mtModWorkcell = mtModWorkcellRepository.selectByPrimaryKey(hmeWipStocktakeDocVO3.getWorkcellId());
                        hmeWipStocktakeDocVO3.setWorkcellCode(mtModWorkcell.getWorkcellCode());
                        hmeWipStocktakeDocVO3.setWorkcellName(mtModWorkcell.getWorkcellName());
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public List<HmeWipStocktakeDocVO11> releaseDetailExport(Long tenantId, HmeWipStocktakeDocDTO15 dto) {
        List<HmeWipStocktakeDocVO11> resultList = new ArrayList<>();
        List<String> stocktakeIdList = dto.getStocktakeIdList();
        if (isEmpty(stocktakeIdList)) {
            return resultList;
        }
        for (String stocktakeId:stocktakeIdList) {
            HmeWipStocktakeDoc hmeWipStocktakeDoc = hmeWipStocktakeDocRepository.selectByPrimaryKey(stocktakeId);
            //??????????????????COS??????????????????????????????
            if(!HmeConstants.ConstantValue.NO.equals(hmeWipStocktakeDoc.getAttribute1())){
                return resultList;
            }
        }
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue("HME.COS_ITEM_GROUP", tenantId);
        List<String> cosItemGroupList = lovValueDTOS.stream().map(LovValueDTO::getValue).distinct().collect(Collectors.toList());
        resultList = hmeWipStocktakeDocMapper.releaseDetailExport(tenantId, stocktakeIdList, cosItemGroupList, dto);
        for (HmeWipStocktakeDocVO11 result : resultList) {
            //????????????
            result.setReleaseQty(result.getReleaseQty().subtract(result.getScrapQty()));
        }
        return resultList;
    }
}
