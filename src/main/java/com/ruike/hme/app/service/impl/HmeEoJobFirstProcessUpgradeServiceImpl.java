package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeResponseDTO;
import com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeSnDTO;
import com.ruike.hme.app.service.HmeEoJobFirstProcessUpgradeService;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.HmeEoVO2;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendAttrVO;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.entity.MtMaterialLotHis;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotHisRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO24;
import tarzan.inventory.domain.vo.MtContainerVO25;
import tarzan.inventory.domain.vo.MtMaterialLotAttrVO2;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.inventory.infra.mapper.MtMaterialLotMapper;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.vo.MtMaterialVO;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.vo.MtEoVO;

import java.text.*;
import java.util.*;

/**
 * @Classname HmeEoJobFirstProcessUpgradeServiceImpl
 * @Description PDA??????SN??????
 * @Date 2020/9/3 10:59
 * @Author yuchao.wang
 */
@Service
public class HmeEoJobFirstProcessUpgradeServiceImpl implements HmeEoJobFirstProcessUpgradeService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtEventRepository mtEventRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtEoRepository mtEoRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private MtMaterialLotMapper mtMaterialLotMapper;

    @Autowired
    private MtMaterialLotHisRepository mtMaterialLotHisRepository;

    /**
     *
     * @Description ??????????????????
     *
     * @author yuchao.wang
     * @date 2020/9/3 11:16
     * @param tenantId ??????ID
     * @param barcode ??????
     * @return com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeResponseDTO
     *
     */
    @Override
    public HmeEoJobFirstProcessUpgradeResponseDTO baseBarcodeScan(Long tenantId, String barcode) {
        //????????????
        if(StringUtils.isBlank(barcode)){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????"));
        }

        //???????????????
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotCode(barcode);
        materialLot = mtMaterialLotRepository.selectOne(materialLot);
        if(Objects.isNull(materialLot) || StringUtils.isBlank(materialLot.getMaterialLotId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //???????????????????????????
        if(!HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag())){
            throw new MtException("QMS_CHECK_SCRAP_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0004", "QMS", barcode));
        }

        //??????????????????
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("FIRST_STEP_IN_FLAG");
        List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isEmpty(materialLotAttrVOList) || Objects.isNull(materialLotAttrVOList.get(0))
                || !HmeConstants.ConstantValue.YES.equals(materialLotAttrVOList.get(0).getAttrValue())){
            throw new MtException("HME_EO_JOB_SN_063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_063", "HME", barcode));
        }

        //??????EO????????????
        HmeEoVO2 hmeEoVO2 = hmeEoJobSnRepository.queryEoActualByEoId(tenantId, materialLot.getEoId());
        if(Objects.isNull(hmeEoVO2) || StringUtils.isBlank(hmeEoVO2.getEoId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO??????"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(hmeEoVO2.getEntryStepFlag())){
            throw new MtException("HME_EO_JOB_SN_064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_063", "HME", barcode));
        }
        if(!HmeConstants.EoStatus.COMPLETED.equals(hmeEoVO2.getStatus())){
            throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_064", "HME", barcode));
        }

        //??????????????????
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, materialLot.getMaterialId());
        if(Objects.isNull(mtMaterialVO) || StringUtils.isEmpty(mtMaterialVO.getMaterialId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "????????????"));
        }

        //??????????????????
        HmeEoJobFirstProcessUpgradeResponseDTO responseDTO = new HmeEoJobFirstProcessUpgradeResponseDTO();
        BeanUtils.copyProperties(hmeEoVO2, responseDTO);
        responseDTO.setMaterialLotId(materialLot.getMaterialLotId());
        responseDTO.setMaterialLotCode(materialLot.getMaterialLotCode());
        responseDTO.setMaterialId(materialLot.getMaterialId());
        responseDTO.setMaterialCode(mtMaterialVO.getMaterialCode());
        responseDTO.setMaterialName(mtMaterialVO.getMaterialName());
        return responseDTO;
    }

    /**
     *
     * @Description SN??????
     *
     * @author yuchao.wang
     * @date 2020/9/3 16:48
     * @param tenantId ??????ID
     * @param upgradeSnDTO ??????
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void snUpgrade(Long tenantId, HmeEoJobFirstProcessUpgradeSnDTO upgradeSnDTO) {
        //????????????
        if(StringUtils.isBlank(upgradeSnDTO.getSnNum())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "SN??????"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getBaseMaterialLotId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "????????????ID"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getEoId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "EO ID"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getRouterStepId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????????????????ID"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getWorkcellId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "??????ID"));
        }

        //????????????ID
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("FIRST_STEP_SN_UPDATE");
        }});

        //????????????ID
        String outSiteMaterialLotId = "";

        //??????SN??????ID
        MtMaterialLot snMaterialLot = new MtMaterialLot();
        snMaterialLot.setTenantId(tenantId);
        snMaterialLot.setMaterialLotCode(upgradeSnDTO.getSnNum());
        snMaterialLot = mtMaterialLotRepository.selectOne(snMaterialLot);
        if(Objects.isNull(snMaterialLot) || StringUtils.isBlank(snMaterialLot.getMaterialLotId())){
            //??????SN????????????????????????????????????????????????
            MtMaterialLot baseMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, upgradeSnDTO.getBaseMaterialLotId());
            if (Objects.isNull(baseMaterialLot) || StringUtils.isEmpty(baseMaterialLot.getMaterialLotId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "?????????????????????"));
            }
            outSiteMaterialLotId = baseMaterialLot.getMaterialLotId();

            //?????????????????????
            MtMaterialLot update = new MtMaterialLot();
            update.setMaterialLotId(baseMaterialLot.getMaterialLotId());
            update.setMaterialLotCode(upgradeSnDTO.getSnNum());
            update.setIdentification(upgradeSnDTO.getSnNum());
            mtMaterialLotMapper.updateByPrimaryKeySelective(update);

            //?????????????????????
            MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
            BeanUtils.copyProperties(baseMaterialLot, mtMaterialLotHis);
            mtMaterialLotHis.setEventId(eventId);
            mtMaterialLotHis.setMaterialLotCode(update.getMaterialLotCode());
            mtMaterialLotHis.setIdentification(update.getIdentification());
            mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
        } else {
            //??????Sn????????????EO ?????????[SN???????????????EO,???????????????]
            if (StringUtils.isNotBlank(snMaterialLot.getEoId())) {
                throw new MtException("HME_EO_JOB_SN_107", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_107", "HME"));
            }
            outSiteMaterialLotId = snMaterialLot.getMaterialLotId();

            //?????????????????????????????????
            MtMaterialLot baseMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, upgradeSnDTO.getBaseMaterialLotId());
            if (Objects.isNull(baseMaterialLot) || StringUtils.isEmpty(baseMaterialLot.getMaterialLotId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "?????????????????????"));
            }

            //??????????????????
            List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(upgradeSnDTO.getBaseMaterialLotId());
            }});

            //????????????????????????????????????
            MtContainerLoadDetail containerLoadDetail = new MtContainerLoadDetail();
            containerLoadDetail.setTenantId(tenantId);
            containerLoadDetail.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            containerLoadDetail.setLoadObjectId(baseMaterialLot.getMaterialLotId());
            containerLoadDetail = mtContainerLoadDetailRepository.selectOne(containerLoadDetail);

            //??????????????????????????????????????????????????????????????????????????????sn??????
            if (!Objects.isNull(containerLoadDetail) && StringUtils.isNotBlank(containerLoadDetail.getContainerLoadDetailId())) {
                //??????????????????
                MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                mtContainerVO25.setContainerId(containerLoadDetail.getContainerId());
                mtContainerVO25.setLoadObjectId(baseMaterialLot.getMaterialLotId());
                mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerVO25.setParentEventId(eventId);
                mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
            }

            //??????????????????
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setEventId(eventId);
                setMaterialLotId(upgradeSnDTO.getBaseMaterialLotId());
                setEnableFlag(HmeConstants.ConstantValue.NO);
            }}, "N");

            //??????SN?????????
            SimpleDateFormat fm = new SimpleDateFormat("yyMMddHH");
            Date now = new Date();
            MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
            BeanUtils.copyProperties(baseMaterialLot, mtMaterialLotVO2);
            mtMaterialLotVO2.setEventId(eventId);
            mtMaterialLotVO2.setMaterialLotId(snMaterialLot.getMaterialLotId());
            mtMaterialLotVO2.setMaterialLotCode(snMaterialLot.getMaterialLotCode());
            mtMaterialLotVO2.setIdentification(snMaterialLot.getIdentification());
            mtMaterialLotVO2.setLot(fm.format(now));
            mtMaterialLotVO2.setLoadTime(now);
            mtMaterialLotVO2.setUnloadTime(null);
            mtMaterialLotRepository.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");

            //??????SN?????????????????????
            List<MtExtendVO5> mtExtendVO5List = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(materialLotAttrVOList)) {
                materialLotAttrVOList.forEach(item -> {
                    MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
                    mtExtendVO5.setAttrName(item.getAttrName());
                    mtExtendVO5.setAttrValue(item.getAttrValue());
                    mtExtendVO5List.add(mtExtendVO5);
                });
            }
            mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_material_lot_attr", snMaterialLot.getMaterialLotId(), eventId, mtExtendVO5List);

            //??????????????????????????????????????????????????????????????????????????????sn??????
            if(!Objects.isNull(containerLoadDetail) && StringUtils.isNotBlank(containerLoadDetail.getContainerLoadDetailId())) {
                //??????SN??????
                MtContainerVO24 mtContainerVo24 = new MtContainerVO24();
                mtContainerVo24.setContainerId(containerLoadDetail.getContainerId());
                mtContainerVo24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerVo24.setLoadObjectId(snMaterialLot.getMaterialLotId());
                mtContainerVo24.setParentEventId(eventId);
                mtContainerRepository.containerLoad(tenantId, mtContainerVo24);
            }
        }

        //??????EO
        MtEoVO mtEoVO = new MtEoVO();
        mtEoVO.setEventId(eventId);
        mtEoVO.setTenantId(tenantId);
        mtEoVO.setEoId(upgradeSnDTO.getEoId());
        mtEoVO.setIdentification(upgradeSnDTO.getSnNum());
        mtEoRepository.eoUpdate(tenantId, mtEoVO, HmeConstants.ConstantValue.NO);

        //??????????????????EoJobSn
        String jobId = hmeEoJobSnRepository.queryJobIdFirstProcessSnUpgrade(tenantId, upgradeSnDTO);
        if(StringUtils.isBlank(jobId)){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "??????????????????"));
        }

        //??????SN??????
        CustomUserDetails userDetails = DetailsHelper.getUserDetails();
        Long userId = Objects.isNull(userDetails) ? -1L : userDetails.getUserId();
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setJobId(jobId);
        hmeEoJobSn.setSiteOutBy(userId);
        hmeEoJobSn.setSiteOutDate(new Date());
        hmeEoJobSn.setMaterialLotId(outSiteMaterialLotId);
        hmeEoJobSnMapper.updateByPrimaryKeySelective(hmeEoJobSn);
    }
}