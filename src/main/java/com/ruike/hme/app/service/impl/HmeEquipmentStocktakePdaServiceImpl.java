package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO;
import com.ruike.hme.api.dto.HmeEquipmentStocktakePdaDTO2;
import com.ruike.hme.app.service.HmeEquipmentStocktakePdaService;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeActual;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeActualRepository;
import com.ruike.hme.domain.repository.HmeEquipmentStocktakeDocRepository;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakePdaVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEquipmentMapper;
import com.ruike.hme.infra.mapper.HmeEquipmentStocktakeActualMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.repository.MtModAreaRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * HmeEquipmentStocktakePdaServiceImpl
 * 设备盘点PDA应用服务默认实现
 * @author: chaonan.hu@hand-china.com 2021/04/01 15:33:21
 **/
@Service
public class HmeEquipmentStocktakePdaServiceImpl implements HmeEquipmentStocktakePdaService {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private HmeEquipmentStocktakeDocRepository hmeEquipmentStocktakeDocRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;
    @Autowired
    private HmeEquipmentStocktakeActualRepository hmeEquipmentStocktakeActualRepository;
    @Autowired
    private HmeEquipmentMapper hmeEquipmentMapper;
    @Autowired
    private HmeEquipmentStocktakeActualMapper hmeEquipmentStocktakeActualMapper;

    @Override
    public List<LovValueDTO> equipmentStatusLovQuery(Long tenantId, String lovCode) {
        List<LovValueDTO> lovValueDTOS = lovAdapter.queryLovValue(lovCode, tenantId);
        return lovValueDTOS;
    }

    @Override
    public HmeEquipmentStocktakePdaVO scanStocktakeNum(Long tenantId, String stocktakeNum) {
        HmeEquipmentStocktakeDoc hmeEquipmentStocktakeDoc = hmeEquipmentStocktakeDocRepository.selectOne(new HmeEquipmentStocktakeDoc() {{
            setTenantId(tenantId);
            setStocktakeNum(stocktakeNum);
        }});
        if(Objects.isNull(hmeEquipmentStocktakeDoc)){
            //若单据不存在，则报错
            throw new MtException("HME_EQ_STOCKTAKE_006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQ_STOCKTAKE_006", "HME"));
        }
        if("CANCELLED".equals(hmeEquipmentStocktakeDoc.getStocktakeStatus())){
            //若单据状态为取消，则报错
            throw new MtException("HME_EQ_STOCKTAKE_004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQ_STOCKTAKE_004", "HME"));
        }
        if("DONE".equals(hmeEquipmentStocktakeDoc.getStocktakeStatus())){
            //若单据状态为完成，则报错
            throw new MtException("HME_EQ_STOCKTAKE_005", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQ_STOCKTAKE_005", "HME"));
        }
        HmeEquipmentStocktakePdaVO result = new HmeEquipmentStocktakePdaVO();
        BeanUtils.copyProperties(hmeEquipmentStocktakeDoc, result);
        if(StringUtils.isNotBlank(result.getBusinessId())){
            MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(result.getBusinessId());
            if(Objects.nonNull(mtModArea)){
                result.setBusinessName(mtModArea.getAreaName());
            }
        }
        String stocktakeTypeMeaning = lovAdapter.queryLovMeaning("HME_STOCKTAKE_TYPE", tenantId, result.getStocktakeType());
        result.setStocktakeTypeMeaning(stocktakeTypeMeaning);
        return result;
    }

    @Override
    public HmeEquipmentStocktakePdaVO2 scanEquipment(Long tenantId, HmeEquipmentStocktakePdaDTO dto) {
        if(StringUtils.isEmpty(dto.getStocktakeId())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "盘点单ID"));
        }
        if(StringUtils.isEmpty(dto.getAssetEncoding())){
            throw new MtException("HME_NC_0006", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_NC_0006", "HME", "资产编码"));
        }
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(dto.getAssetEncoding());
        }});
        if(Objects.isNull(hmeEquipment)){
            //若设备不存在，则报错
            throw new MtException("HME_EQ_STOCKTAKE_007", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EQ_STOCKTAKE_007", "HME"));
        }
        HmeEquipmentStocktakePdaVO2 result = new HmeEquipmentStocktakePdaVO2();
        BeanUtils.copyProperties(hmeEquipment, result);
        if(StringUtils.isNotBlank(hmeEquipment.getEquipmentStatus())){
            String equipmentStatusMeaning = lovAdapter.queryLovMeaning("HME.EQUIPMENT_STATUS", tenantId, hmeEquipment.getEquipmentStatus());
            result.setEquipmentStatusMeaning(equipmentStatusMeaning);
        }
        if(StringUtils.isNotBlank(hmeEquipment.getBusinessId())){
            MtModArea mtModArea = mtModAreaRepository.selectByPrimaryKey(hmeEquipment.getBusinessId());
            if(Objects.nonNull(mtModArea)){
                result.setBusinessName(mtModArea.getAreaName());
            }
        }
        HmeEquipmentStocktakeActual hmeEquipmentStocktakeActual = hmeEquipmentStocktakeActualRepository.selectOne(new HmeEquipmentStocktakeActual() {{
            setTenantId(tenantId);
            setStocktakeId(dto.getStocktakeId());
            setEquipmentId(hmeEquipment.getEquipmentId());
        }});
        if(Objects.nonNull(hmeEquipmentStocktakeActual)){
            result.setStocktakeActualId(hmeEquipmentStocktakeActual.getStocktakeActualId());
            if(HmeConstants.ConstantValue.YES.equals(hmeEquipmentStocktakeActual.getStocktakeFlag())){
                result.setResult("重复盘点");
                result.setResultColor("YELLOW");
                result.setSubmitButtonColor("GREY");
            }else{
                result.setResult("已盘");
                result.setResultColor("GREEN");
                result.setSubmitButtonColor("BLUE");
                result.setStocktakeDate(new Date());
            }
        }else{
            result.setResult("盘点清单外");
            result.setResultColor("RED");
            result.setSubmitButtonColor("GREY");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HmeEquipmentStocktakePdaDTO2 submit(Long tenantId, HmeEquipmentStocktakePdaDTO2 dto) {
        //更新设备状态
        HmeEquipment hmeEquipment = hmeEquipmentRepository.selectByPrimaryKey(dto.getEquipmentId());
        if(Objects.nonNull(hmeEquipment) && !dto.getEquipmentStatus().equals(hmeEquipment.getEquipmentStatus())){
            hmeEquipment.setEquipmentStatus(dto.getEquipmentStatus());
            hmeEquipmentMapper.updateByPrimaryKeySelective(hmeEquipment);
        }
        //更新盘点实绩
        HmeEquipmentStocktakeActual hmeEquipmentStocktakeActual = hmeEquipmentStocktakeActualRepository.selectByPrimaryKey(dto.getStocktakeActualId());
        hmeEquipmentStocktakeActual.setStocktakeFlag("Y");
        hmeEquipmentStocktakeActual.setRemark(dto.getRemark());
        hmeEquipmentStocktakeActual.setStocktakeDate(DateUtil.string2Date(dto.getStocktakeDate(), "yyyy-MM-dd HH:mm:ss"));
        hmeEquipmentStocktakeActualMapper.updateByPrimaryKey(hmeEquipmentStocktakeActual);
        return dto;
    }
}
