package com.ruike.wms.infra.repository.impl;

import com.ruike.wms.api.dto.*;
import com.ruike.wms.domain.entity.WmsInstructionDocs;
import com.ruike.wms.domain.repository.WmsInstructionDocsRepository;
import com.ruike.wms.domain.repository.WmsMaterialRepository;
import com.ruike.wms.domain.vo.WmsMaterialVO;
import com.ruike.wms.infra.mapper.WmsInstructionDocsMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tarzan.general.api.dto.MtUserOrganizationDTO;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.vo.MtModSiteVO6;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指令单据头表 资源库实现
 *
 * @author taowen.wang@hand-china.com 2021-07-07 17:18:05
 */
@Component
public class WmsInstructionDocsRepositoryImpl extends BaseRepositoryImpl<WmsInstructionDocs> implements WmsInstructionDocsRepository {
    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private WmsMaterialRepository wmsMaterialRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private WmsInstructionDocsMapper wmsInstructionDocsMapper;
    @Override
    public List<String> factoryPermissionQuery(Long tenantId, MtUserOrganizationDTO dto) {
        MtUserOrganization param = new MtUserOrganization();
        BeanUtils.copyProperties(dto, param);
        // 调用API : userOrganizationPermissionQuery
        List<MtUserOrganization> mtUserOrganizations = mtUserOrganizationRepository.userOrganizationPermissionQuery(tenantId, param);
        MtModSiteVO6 mtModSiteVO6 = new MtModSiteVO6();
        mtModSiteVO6.setSiteId(mtUserOrganizations.get(0).getOrganizationId());
        // 调用API : propertyLimitSitePropertyQuery
        List<MtModSiteVO6> mtModSiteVO6s = mtModSiteRepository.propertyLimitSitePropertyQuery(tenantId, mtModSiteVO6);
        ArrayList<String> siteCode = new ArrayList<>();
        for (MtModSiteVO6 item:mtModSiteVO6s) {
            siteCode.add(item.getSiteCode());
        }
        return siteCode;
    }

    @Override
    public List<WmsMaterialDTO> materialPermissionQuery(Long tenantId, String organizationId) {
        WmsMaterialVO wmsMaterialVO = new WmsMaterialVO();
        wmsMaterialVO.setSiteId(organizationId);
        // 调用 API : siteLimitMaterialQuery
        List<WmsMaterialVO> wmsMaterialVOS = wmsMaterialRepository.siteLimitMaterialQuery(tenantId, wmsMaterialVO);
        ArrayList<String> materialIds = new ArrayList<>();
        for (WmsMaterialVO item:wmsMaterialVOS) {
            materialIds.add(item.getMaterialId());
        }
        // 调用 API : materialPropertyBatchGet
        List<MtMaterialVO> mtMaterialVOS = mtMaterialRepository.materialPropertyBatchGet(tenantId, materialIds);
        List<WmsMaterialDTO> wmsMaterialDTOS = new ArrayList<>();
        for (MtMaterialVO item:mtMaterialVOS) {
            WmsMaterialDTO wmsMaterialDTO = new WmsMaterialDTO();
            wmsMaterialDTO.setMaterialName(item.getMaterialName());
            wmsMaterialDTO.setMaterialCode(item.getMaterialCode());
            wmsMaterialDTOS.add(wmsMaterialDTO);
        }
        return wmsMaterialDTOS;
    }

    @Override
    @ProcessLovValue
    public Page<WmsInstructionDocsDTO> queryHendList(Long tenantId, WmsInstructionDocDTO dto, PageRequest pageRequest) {
        // 单据类型的值集
        List<LovValueDTO> ioDmType = lovAdapter.queryLovValue("WX.WMS.WO_IO_DM_TYPE", tenantId);
        List<String> dmType = ioDmType.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        // WX.WMS.DOC_EXE 领料单
        List<LovValueDTO> docExe = lovAdapter.queryLovValue("WX.WMS.DOC_EXE", tenantId);
        List<String> locatorExe = docExe.stream().map(LovValueDTO::getValue).collect(Collectors.toList());
        // WX.WMS.DOC_RE 退料单
        List<LovValueDTO> docRe = lovAdapter.queryLovValue("WX.WMS.DOC_RE", tenantId);
        List<String> locatorRe = docRe.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        Page<WmsInstructionDocsDTO> wmsInstructionDocsDTO = PageHelper.doPage(pageRequest, () -> wmsInstructionDocsMapper.selectByInstructionDocId(tenantId, dto ,locatorExe ,locatorRe ,dmType));
        return wmsInstructionDocsDTO;
    }

    @Override
    @ProcessLovValue
    public Page<WmsInstructionIdLineDTO> queryLineList(Long tenantId, String instructionDocIds, PageRequest pageRequest) {
        // WX.WMS.DOC_EXE 领料单（退料单值集 : WX.WMS.DOC_RE）
        List<LovValueDTO> docExe = lovAdapter.queryLovValue("WX.WMS.DOC_EXE", tenantId);
        List<String> locatorExe = docExe.stream().map(LovValueDTO::getValue).collect(Collectors.toList());

        Page<WmsInstructionIdLineDTO> wmsInstructionIdLineDTO = PageHelper.doPage(pageRequest, () -> wmsInstructionDocsMapper.selectByInstructionLinkDoc(tenantId, instructionDocIds));
        // 判断单据类型是否为领料单
        for (WmsInstructionIdLineDTO item:wmsInstructionIdLineDTO) {
            if(locatorExe.contains(item.getInstructionDocType())){
                item.setToLocatorId(item.getFromLocatorId());
            }
            if(StringUtils.isNotBlank(item.getActualQty())&&item.getActualQty().indexOf(".") > 0){
                //去掉多余的0
                item.setActualQty(item.getActualQty().replaceAll("0+?$", ""));
                //如最后一位是.则去掉
                item.setActualQty(item.getActualQty().replaceAll("[.]$", ""));
            }
            if(StringUtils.isNotBlank(item.getQuantity())&&item.getQuantity().indexOf(".") > 0){
                item.setQuantity(item.getQuantity().replaceAll("0+?$", ""));
                item.setQuantity(item.getQuantity().replaceAll("[.]$", ""));
            }
        }
        return wmsInstructionIdLineDTO;
    }

    @Override
    @ProcessLovValue
    public Page<WmsInstructionIdAttrDTO> queryLineAttrList(Long tenantId, String instructionIds, PageRequest pageRequest) {
        Page<WmsInstructionIdAttrDTO> wmsInstructionIdAttrDTOS =  PageHelper.doPage(pageRequest,() ->wmsInstructionDocsMapper.selectByInstructionAttrDoc(tenantId, instructionIds));
        for(WmsInstructionIdAttrDTO wmsInstructionIdAttrDTO:wmsInstructionIdAttrDTOS){
            if(StringUtils.isNotBlank(wmsInstructionIdAttrDTO.getPrimaryUomQty())&&wmsInstructionIdAttrDTO.getPrimaryUomQty().indexOf(".") > 0){
                wmsInstructionIdAttrDTO.setPrimaryUomQty(wmsInstructionIdAttrDTO.getPrimaryUomQty().replaceAll("0+?$", ""));
                wmsInstructionIdAttrDTO.setPrimaryUomQty(wmsInstructionIdAttrDTO.getPrimaryUomQty().replaceAll("[.]$", ""));
            }
        }
        return wmsInstructionIdAttrDTOS;
    }


}
