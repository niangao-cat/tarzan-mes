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
 * @Description PDA首序SN升级
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
     * @Description 基座条码扫描
     *
     * @author yuchao.wang
     * @date 2020/9/3 11:16
     * @param tenantId 租户ID
     * @param barcode 条码
     * @return com.ruike.hme.api.dto.HmeEoJobFirstProcessUpgradeResponseDTO
     *
     */
    @Override
    public HmeEoJobFirstProcessUpgradeResponseDTO baseBarcodeScan(Long tenantId, String barcode) {
        //非空校验
        if(StringUtils.isBlank(barcode)){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "底座条码"));
        }

        //查询物料批
        MtMaterialLot materialLot = new MtMaterialLot();
        materialLot.setTenantId(tenantId);
        materialLot.setMaterialLotCode(barcode);
        materialLot = mtMaterialLotRepository.selectOne(materialLot);
        if(Objects.isNull(materialLot) || StringUtils.isBlank(materialLot.getMaterialLotId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
        }

        //校验物料批是否有效
        if(!HmeConstants.ConstantValue.YES.equals(materialLot.getEnableFlag())){
            throw new MtException("QMS_CHECK_SCRAP_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_CHECK_SCRAP_0004", "QMS", barcode));
        }

        //查询扩展属性
        MtMaterialLotAttrVO2 mtMaterialLotAttrVO2 = new MtMaterialLotAttrVO2();
        mtMaterialLotAttrVO2.setMaterialLotId(materialLot.getMaterialLotId());
        mtMaterialLotAttrVO2.setAttrName("FIRST_STEP_IN_FLAG");
        List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, mtMaterialLotAttrVO2);
        if(CollectionUtils.isEmpty(materialLotAttrVOList) || Objects.isNull(materialLotAttrVOList.get(0))
                || !HmeConstants.ConstantValue.YES.equals(materialLotAttrVOList.get(0).getAttrValue())){
            throw new MtException("HME_EO_JOB_SN_063", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_063", "HME", barcode));
        }

        //校验EO首序信息
        HmeEoVO2 hmeEoVO2 = hmeEoJobSnRepository.queryEoActualByEoId(tenantId, materialLot.getEoId());
        if(Objects.isNull(hmeEoVO2) || StringUtils.isBlank(hmeEoVO2.getEoId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "EO信息"));
        }
        if(!HmeConstants.ConstantValue.YES.equals(hmeEoVO2.getEntryStepFlag())){
            throw new MtException("HME_EO_JOB_SN_064", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_063", "HME", barcode));
        }
        if(!HmeConstants.EoStatus.COMPLETED.equals(hmeEoVO2.getStatus())){
            throw new MtException("HME_EO_JOB_SN_065", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_EO_JOB_SN_064", "HME", barcode));
        }

        //查询物料信息
        MtMaterialVO mtMaterialVO = mtMaterialRepository.materialPropertyGet(tenantId, materialLot.getMaterialId());
        if(Objects.isNull(mtMaterialVO) || StringUtils.isEmpty(mtMaterialVO.getMaterialId())){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "物料信息"));
        }

        //构造返回参数
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
     * @Description SN升级
     *
     * @author yuchao.wang
     * @date 2020/9/3 16:48
     * @param tenantId 租户ID
     * @param upgradeSnDTO 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void snUpgrade(Long tenantId, HmeEoJobFirstProcessUpgradeSnDTO upgradeSnDTO) {
        //非空校验
        if(StringUtils.isBlank(upgradeSnDTO.getSnNum())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "SN条码"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getBaseMaterialLotId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "底座条码ID"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getEoId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "EO ID"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getRouterStepId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线步骤ID"));
        }
        if(StringUtils.isBlank(upgradeSnDTO.getWorkcellId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位ID"));
        }

        //获取事件ID
        String eventId = mtEventRepository.eventCreate(tenantId, new MtEventCreateVO() {{
            setEventTypeCode("FIRST_STEP_SN_UPDATE");
        }});

        //出站条码ID
        String outSiteMaterialLotId = "";

        //查询SN条码ID
        MtMaterialLot snMaterialLot = new MtMaterialLot();
        snMaterialLot.setTenantId(tenantId);
        snMaterialLot.setMaterialLotCode(upgradeSnDTO.getSnNum());
        snMaterialLot = mtMaterialLotRepository.selectOne(snMaterialLot);
        if(Objects.isNull(snMaterialLot) || StringUtils.isBlank(snMaterialLot.getMaterialLotId())){
            //如果SN条码不存在，直接更新原有底座条码
            MtMaterialLot baseMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, upgradeSnDTO.getBaseMaterialLotId());
            if (Objects.isNull(baseMaterialLot) || StringUtils.isEmpty(baseMaterialLot.getMaterialLotId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "底座物料批信息"));
            }
            outSiteMaterialLotId = baseMaterialLot.getMaterialLotId();

            //更新物料批条码
            MtMaterialLot update = new MtMaterialLot();
            update.setMaterialLotId(baseMaterialLot.getMaterialLotId());
            update.setMaterialLotCode(upgradeSnDTO.getSnNum());
            update.setIdentification(upgradeSnDTO.getSnNum());
            mtMaterialLotMapper.updateByPrimaryKeySelective(update);

            //记录物料批历史
            MtMaterialLotHis mtMaterialLotHis = new MtMaterialLotHis();
            BeanUtils.copyProperties(baseMaterialLot, mtMaterialLotHis);
            mtMaterialLotHis.setEventId(eventId);
            mtMaterialLotHis.setMaterialLotCode(update.getMaterialLotCode());
            mtMaterialLotHis.setIdentification(update.getIdentification());
            mtMaterialLotHisRepository.insertSelective(mtMaterialLotHis);
        } else {
            //判断Sn上是否有EO 有报错[SN已承载其他EO,请重新扫描]
            if (StringUtils.isNotBlank(snMaterialLot.getEoId())) {
                throw new MtException("HME_EO_JOB_SN_107", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_EO_JOB_SN_107", "HME"));
            }
            outSiteMaterialLotId = snMaterialLot.getMaterialLotId();

            //查询底座条码及扩展属性
            MtMaterialLot baseMaterialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, upgradeSnDTO.getBaseMaterialLotId());
            if (Objects.isNull(baseMaterialLot) || StringUtils.isEmpty(baseMaterialLot.getMaterialLotId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "底座物料批信息"));
            }

            //查询扩展属性
            List<MtExtendAttrVO> materialLotAttrVOList = mtMaterialLotRepository.materialLotLimitAttrQuery(tenantId, new MtMaterialLotAttrVO2() {{
                setMaterialLotId(upgradeSnDTO.getBaseMaterialLotId());
            }});

            //判断底座条码是否已经装载
            MtContainerLoadDetail containerLoadDetail = new MtContainerLoadDetail();
            containerLoadDetail.setTenantId(tenantId);
            containerLoadDetail.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
            containerLoadDetail.setLoadObjectId(baseMaterialLot.getMaterialLotId());
            containerLoadDetail = mtContainerLoadDetailRepository.selectOne(containerLoadDetail);

            //如果底座条码已经装载到容器，则需要卸载底座条码并装载sn条码
            if (!Objects.isNull(containerLoadDetail) && StringUtils.isNotBlank(containerLoadDetail.getContainerLoadDetailId())) {
                //卸载底座条码
                MtContainerVO25 mtContainerVO25 = new MtContainerVO25();
                mtContainerVO25.setContainerId(containerLoadDetail.getContainerId());
                mtContainerVO25.setLoadObjectId(baseMaterialLot.getMaterialLotId());
                mtContainerVO25.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerVO25.setParentEventId(eventId);
                mtContainerRepository.containerUnload(tenantId, mtContainerVO25);
            }

            //更新底座条码
            mtMaterialLotRepository.materialLotUpdate(tenantId, new MtMaterialLotVO2() {{
                setEventId(eventId);
                setMaterialLotId(upgradeSnDTO.getBaseMaterialLotId());
                setEnableFlag(HmeConstants.ConstantValue.NO);
            }}, "N");

            //更新SN物料批
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

            //更新SN物料批扩展属性
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

            //如果底座条码已经装载到容器，则需要卸载底座条码并装载sn条码
            if(!Objects.isNull(containerLoadDetail) && StringUtils.isNotBlank(containerLoadDetail.getContainerLoadDetailId())) {
                //装载SN条码
                MtContainerVO24 mtContainerVo24 = new MtContainerVO24();
                mtContainerVo24.setContainerId(containerLoadDetail.getContainerId());
                mtContainerVo24.setLoadObjectType(HmeConstants.LoadTypeCode.MATERIAL_LOT);
                mtContainerVo24.setLoadObjectId(snMaterialLot.getMaterialLotId());
                mtContainerVo24.setParentEventId(eventId);
                mtContainerRepository.containerLoad(tenantId, mtContainerVo24);
            }
        }

        //更新EO
        MtEoVO mtEoVO = new MtEoVO();
        mtEoVO.setEventId(eventId);
        mtEoVO.setTenantId(tenantId);
        mtEoVO.setEoId(upgradeSnDTO.getEoId());
        mtEoVO.setIdentification(upgradeSnDTO.getSnNum());
        mtEoRepository.eoUpdate(tenantId, mtEoVO, HmeConstants.ConstantValue.NO);

        //查询要更新的EoJobSn
        String jobId = hmeEoJobSnRepository.queryJobIdFirstProcessSnUpgrade(tenantId, upgradeSnDTO);
        if(StringUtils.isBlank(jobId)){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "作业记录信息"));
        }

        //更新SN出站
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