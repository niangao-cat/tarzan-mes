package tarzan.modeling.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import tarzan.modeling.api.dto.*;
import tarzan.modeling.app.service.MtModAreaService;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;
import tarzan.modeling.domain.vo.MtModAreaPurchaseVO;
import tarzan.modeling.domain.vo.MtModAreaScheduleVO;
import tarzan.modeling.infra.mapper.MtModAreaMapper;

/**
 * 区域应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Service
public class MtModAreaServiceImpl extends BaseServiceImpl<MtModArea> implements MtModAreaService {

    private static final String MT_MOD_AREA_ATTR = "mt_mod_area_attr";

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtModAreaRepository mtModAreaRepository;

    @Autowired
    private MtModAreaPurchaseRepository mtModAreaPurchaseRepository;

    @Autowired
    private MtModAreaScheduleRepository mtModAreaScheduleRepository;

    @Autowired
    private MtSupplierRepository mtSupplierRepository;

    @Autowired
    private MtSupplierSiteRepository mtSupplierSiteRepository;

    @Autowired
    private MtModAreaMapper mtModAreaMapper;

    @Override
    public List<MtModArea> queryModAreaForUi(Long tenantId, MtModAreaDTO dto, PageRequest pageRequest) {
        MtModArea queryModArea = new MtModArea();
        queryModArea.setTenantId(tenantId);
        queryModArea.setAreaCode(dto.getAreaCode());
        queryModArea.setAreaName(dto.getAreaName());
        queryModArea.setDescription(dto.getDescription());
        queryModArea.setEnableFlag(dto.getEnableFlag());

        Criteria criteria = new Criteria(queryModArea);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtModArea.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtModArea.FIELD_AREA_CODE, Comparison.LIKE));
        whereFields.add(new WhereField(MtModArea.FIELD_AREA_NAME, Comparison.LIKE));
        whereFields.add(new WhereField(MtModArea.FIELD_DESCRIPTION, Comparison.LIKE));
        whereFields.add(new WhereField(MtModArea.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));

        return PageHelper.doPageAndSort(pageRequest, () -> mtModAreaMapper.selectOptional(queryModArea, criteria));
    }

    @Override
    public MtModAreaDTO2 queryModAreaDetailForUi(Long tenantId, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            return null;
        }

        MtModAreaDTO2 result = new MtModAreaDTO2();
        MtModArea mtModArea = mtModAreaRepository.areaBasicPropertyGet(tenantId, areaId);
        if (null == mtModArea) {
            return result;
        }

        BeanUtils.copyProperties(mtModArea, result);

        MtModAreaPurchase mtModAreaPurchase = mtModAreaPurchaseRepository.areaPurchasePropertyGet(tenantId, areaId);
        if (null != mtModAreaPurchase) {
            MtModAreaPurchaseDTO mtModAreaPurchaseDTO = new MtModAreaPurchaseDTO();
            BeanUtils.copyProperties(mtModAreaPurchase, mtModAreaPurchaseDTO);

            if (StringUtils.isNotEmpty(mtModAreaPurchase.getSupplierId())) {
                MtSupplier mtSupplier = new MtSupplier();
                mtSupplier.setTenantId(tenantId);
                mtSupplier.setSupplierId(mtModAreaPurchase.getSupplierId());
                mtSupplier = mtSupplierRepository.selectOne(mtSupplier);
                if (mtSupplier != null) {
                    mtModAreaPurchaseDTO.setSupplierCode(mtSupplier.getSupplierCode());
                    mtModAreaPurchaseDTO.setSupplierName(mtSupplier.getSupplierName());
                }
            }

            if (StringUtils.isNotEmpty(mtModAreaPurchase.getSupplierSiteId())) {
                MtSupplierSite mtSupplierSite = new MtSupplierSite();
                mtSupplierSite.setTenantId(tenantId);
                mtSupplierSite.setSupplierSiteId(mtModAreaPurchase.getSupplierSiteId());
                mtSupplierSite = mtSupplierSiteRepository.selectOne(mtSupplierSite);
                if (mtSupplierSite != null) {
                    mtModAreaPurchaseDTO.setSupplierSiteCode(mtSupplierSite.getSupplierSiteCode());
                    mtModAreaPurchaseDTO.setSupplierSiteName(mtSupplierSite.getSupplierSiteName());
                }
            }

            result.setMtModAreaPurchaseDTO(mtModAreaPurchaseDTO);
        }

        MtModAreaSchedule mtModAreaSchedule = mtModAreaScheduleRepository.areaSchedulePropertyGet(tenantId, areaId);
        if (null != mtModAreaSchedule) {
            MtModAreaScheduleDTO mtModAreaScheduleDTO = new MtModAreaScheduleDTO();
            BeanUtils.copyProperties(mtModAreaSchedule, mtModAreaScheduleDTO);
            result.setMtModAreaScheduleDTO(mtModAreaScheduleDTO);

            if (StringUtils.isNotEmpty(mtModAreaSchedule.getFollowAreaId())) {
                MtModArea followArea =
                                mtModAreaRepository.areaBasicPropertyGet(tenantId, mtModAreaSchedule.getFollowAreaId());
                if (followArea != null) {
                    mtModAreaScheduleDTO.setFollowAreaCode(followArea.getAreaCode());
                    mtModAreaScheduleDTO.setFollowAreaName(followArea.getAreaName());
                }
            }
        }

        List<MtExtendAttrDTO> areaAttrList = mtExtendSettingsService.attrQuery(tenantId, areaId, MT_MOD_AREA_ATTR);
        result.setAreaAttrList(areaAttrList);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MtModAreaDTO3 saveModAreaForUi(Long tenantId, MtModAreaDTO3 dto) {
        MtModArea mtModArea = new MtModArea();
        BeanUtils.copyProperties(dto, mtModArea);
        String areaId = mtModAreaRepository.areaBasicPropertyUpdate(tenantId, mtModArea, "Y");
        mtModArea.setAreaId(areaId);

        MtModAreaPurchaseVO mtModAreaPurchaseVO = new MtModAreaPurchaseVO();
        BeanUtils.copyProperties(dto.getMtModAreaPurchaseDTO(), mtModAreaPurchaseVO);
        mtModAreaPurchaseVO.setAreaId(areaId);
        mtModAreaPurchaseRepository.areaPurchasePropertyUpdate(tenantId, mtModAreaPurchaseVO, "Y");

        MtModAreaScheduleVO mtModAreaScheduleVO = new MtModAreaScheduleVO();
        BeanUtils.copyProperties(dto.getMtModAreaScheduleDTO(), mtModAreaScheduleVO);
        mtModAreaScheduleVO.setAreaId(areaId);
        mtModAreaScheduleRepository.areaSchedulePropertyUpdate(tenantId, mtModAreaScheduleVO, "Y");

        if (CollectionUtils.isNotEmpty(dto.getAreaAttrList())) {
            mtExtendSettingsService.attrSave(tenantId, MT_MOD_AREA_ATTR, areaId, null, dto.getAreaAttrList());
        }

        dto.setAreaId(mtModArea.getAreaId());
        return dto;
    }
}
