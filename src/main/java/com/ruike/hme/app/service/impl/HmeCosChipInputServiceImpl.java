package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosChipInputScanBarcodeDTO;
import com.ruike.hme.app.service.HmeCosChipInputService;
import com.ruike.hme.app.service.HmeCosCommonService;
import com.ruike.hme.app.service.HmeEoJobEquipmentService;
import com.ruike.hme.domain.entity.HmeCosOperationRecord;
import com.ruike.hme.domain.entity.HmeEoJobSn;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.entity.HmeWoJobSn;
import com.ruike.hme.domain.repository.HmeCosOperationRecordRepository;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.repository.HmeMaterialLotLoadRepository;
import com.ruike.hme.domain.repository.HmeWoJobSnRepository;
import com.ruike.hme.domain.vo.HmeCosChipInputVO;
import com.ruike.hme.domain.vo.HmeCosEoJobSnSiteOutVO;
import com.ruike.hme.domain.vo.HmeCosMaterialLotVO;
import com.ruike.hme.domain.vo.HmeMaterialLotLoadVO3;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeEoJobSnMapper;
import com.ruike.hme.infra.mapper.HmeMaterialLotLoadMapper;
import com.ruike.hme.infra.mapper.HmeWoJobSnMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;

import java.util.*;
import java.util.stream.*;

/**
 * @Classname HmeCosChipInputServiceImpl
 * @Description COS芯片录入
 * @Date 2020/8/27 21:55
 * @Author yuchao.wang
 */
@Slf4j
@Service
public class HmeCosChipInputServiceImpl implements HmeCosChipInputService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private HmeCosCommonService hmeCosCommonService;

    @Autowired
    private HmeCosOperationRecordRepository hmeCosOperationRecordRepository;

    @Autowired
    private HmeMaterialLotLoadRepository hmeMaterialLotLoadRepository;

    @Autowired
    private HmeWoJobSnRepository hmeWoJobSnRepository;

    @Autowired
    private HmeWoJobSnMapper hmeWoJobSnMapper;

    @Autowired
    private HmeEoJobSnMapper hmeEoJobSnMapper;

    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;

    @Autowired
    private HmeMaterialLotLoadMapper hmeMaterialLotLoadMapper;

    @Autowired
    private HmeEoJobEquipmentService hmeEoJobEquipmentService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeCosChipInputVO> scanBarcode(Long tenantId, HmeCosChipInputScanBarcodeDTO dto) {
        //非空校验
        /*if(StringUtils.isEmpty(dto.getBarcode())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "条码"));
        }*/
        if(StringUtils.isEmpty(dto.getOperationId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工艺路线"));
        }
        if(StringUtils.isEmpty(dto.getWorkcellId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位"));
        }
        if(StringUtils.isEmpty(dto.getWkcShiftId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "班组"));
        }
        List<HmeCosChipInputVO> returnList = new ArrayList<>();

        if(StringUtils.isNotEmpty(dto.getBarcode())) {

            //根据物料批条码获取物料批ID
            MtMaterialLotVO3 param = new MtMaterialLotVO3();
            param.setMaterialLotCode(dto.getBarcode());
            List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, param);
            if (CollectionUtils.isEmpty(materialLotIds) || StringUtils.isEmpty(materialLotIds.get(0))) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "条码信息"));
            }
            String materialLotId = materialLotIds.get(0);

            //调用API获取物料批相关信息
            HmeCosMaterialLotVO hmeCosMaterialLotVO = hmeCosCommonService.materialLotPropertyAndAttrsGet(tenantId, materialLotId, true, dto.getBarcode());
            Map<String, String> materialLotAttrMap = hmeCosMaterialLotVO.getMaterialLotAttrMap();

            HmeCosOperationRecord cosOperationRecord = new HmeCosOperationRecord();
            cosOperationRecord.setOperationRecordId(materialLotAttrMap.get("COS_RECORD"));
            cosOperationRecord = hmeCosOperationRecordRepository.selectOne(cosOperationRecord);
            if (Objects.isNull(cosOperationRecord) || StringUtils.isEmpty(cosOperationRecord.getOperationRecordId())) {
                throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "HME_CHIP_TRANSFER_013", "HME", "工单工艺工位在制记录信息"));
            }
            // 校验条码盘点/冻结标识
            MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectByPrimaryKey(materialLotId);
            if (mtMaterialLot != null) {
                if (HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getFreezeFlag()) || HmeConstants.ConstantValue.YES.equals(mtMaterialLot.getStocktakeFlag())) {
                    throw new MtException("HME_COS_BARCODE_RETEST_003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_COS_BARCODE_RETEST_003", "HME", mtMaterialLot.getMaterialLotCode()));
                }
            }

            //TODO 查询装载信息 并按照行号分组排序
        /*List<HmeMaterialLotLoadVO3> materialLotLoadVO3List = hmeMaterialLotLoadRepository.queryLoadHotSinkByMaterialLotId(tenantId, materialLotId);
        if(CollectionUtils.isEmpty(materialLotIds) || StringUtils.isEmpty(materialLotIds.get(0))){
            throw new MtException("HME_CHIP_TRANSFER_013", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_CHIP_TRANSFER_013", "HME", "装载信息"));
        }*/
            //校验条码在job_sn表内存在出站时间为空的行记录
            hmeCosCommonService.verifyMaterialLotSiteOut(tenantId, materialLotId);

            //如果当前是第一次进站，则记录进站信息：
            if (!hmeEoJobSnRepository.checkChipEnterFlag(tenantId, materialLotId, dto.getOperationId())) {
//                // 获取最近的条码历史
//                String recentHisId = hmeCosCommonService.queryMaterialLotRecentHisId(tenantId, materialLotId);
                //新增EOJobSn
                HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
                hmeEoJobSn.setShiftId(dto.getWkcShiftId());
                hmeEoJobSn.setWorkcellId(dto.getWorkcellId());
                hmeEoJobSn.setWorkOrderId(cosOperationRecord.getWorkOrderId());
                hmeEoJobSn.setOperationId(dto.getOperationId());
                hmeEoJobSn.setSnMaterialId(hmeCosMaterialLotVO.getMaterialId());
                hmeEoJobSn.setMaterialLotId(materialLotId);
                hmeEoJobSn.setJobType("CHIP_NUM_ENTERING");
                hmeEoJobSn.setAttribute3(materialLotAttrMap.get("COS_TYPE"));
                hmeEoJobSn.setAttribute5(materialLotAttrMap.get("WAFER_NUM"));
                hmeEoJobSn.setSourceJobId(cosOperationRecord.getOperationRecordId());
                hmeCosCommonService.eoJobSnSiteIn(tenantId, hmeEoJobSn);
                hmeEoJobEquipmentService.binndHmeEoJobEquipment(tenantId,Collections.singletonList(hmeEoJobSn.getJobId()),dto.getWorkcellId());


                //创建/更新工单工艺在制记录
                HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
                hmeWoJobSn.setTenantId(tenantId);
                hmeWoJobSn.setWorkOrderId(cosOperationRecord.getWorkOrderId());
                hmeWoJobSn.setOperationId(dto.getOperationId());
                hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
                if (Objects.isNull(hmeWoJobSn) || StringUtils.isEmpty(hmeWoJobSn.getWoJobSnId())) {
                    hmeWoJobSn = new HmeWoJobSn();
                    hmeWoJobSn.setTenantId(tenantId);
                    hmeWoJobSn.setWorkOrderId(cosOperationRecord.getWorkOrderId());
                    hmeWoJobSn.setOperationId(dto.getOperationId());
                    hmeWoJobSn.setSiteId(hmeCosMaterialLotVO.getSiteId());
                    hmeWoJobSn.setSiteInNum(1L);
                    hmeWoJobSnRepository.insertSelective(hmeWoJobSn);
                } else {
                    hmeWoJobSn.setSiteId(hmeCosMaterialLotVO.getSiteId());
                    hmeWoJobSn.setSiteInNum(hmeWoJobSn.getSiteInNum() + 1L);
                    hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
                }
            }

            //设置返回值
            returnList = hmeEoJobSnMapper.prepareCosInputQuery(tenantId, dto.getWorkcellId(), materialLotId, dto.getOperationId());

        }else{
            //设置返回值
            returnList = hmeEoJobSnMapper.prepareCosInputQuery(tenantId, dto.getWorkcellId(), null, dto.getOperationId());
        }
        return returnList;
    }

    /**
     *
     * @Description COS芯片录入-热沉号查询
     *
     * @author yifan.xiong
     * @date 2020-9-1 11:29:59
     * @param tenantId 租户ID
     * @param materialLotId 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HmeMaterialLotLoadVO3> queryHotsink(Long tenantId, String materialLotId) {
        List<HmeMaterialLotLoadVO3> materialLotLoadVO3List = hmeMaterialLotLoadRepository.queryLoadHotSinkByMaterialLotId(tenantId, materialLotId);
        return materialLotLoadVO3List;
    }

    /**
     *
     * @Description COS芯片录入-出站
     *
     * @author yuchao.wang
     * @date 2020/8/27 21:45
     * @param tenantId 租户ID
     * @param hmeCosEoJobSnSiteOutVO 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void siteOut(Long tenantId, HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO) {
        if(StringUtils.isEmpty(hmeCosEoJobSnSiteOutVO.getEoJobSnId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "eoSN作业记录ID"));
        }
        if(StringUtils.isEmpty(hmeCosEoJobSnSiteOutVO.getWorkcellId())){
            throw new MtException("QMS_MATERIAL_INSP_0020", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "QMS_MATERIAL_INSP_0020", "QMS", "工位ID"));
        }

        //出站
        hmeCosCommonService.eoJobSnSiteOut(tenantId, hmeCosEoJobSnSiteOutVO);

        //更新工单工艺在制记录
        HmeEoJobSn hmeEoJobSn = new HmeEoJobSn();
        hmeEoJobSn.setJobId(hmeCosEoJobSnSiteOutVO.getEoJobSnId());
        hmeEoJobSn = hmeEoJobSnRepository.selectOne(hmeEoJobSn);

        HmeWoJobSn hmeWoJobSn = new HmeWoJobSn();
        hmeWoJobSn.setTenantId(tenantId);
        hmeWoJobSn.setWorkOrderId(hmeEoJobSn.getWorkOrderId());
        hmeWoJobSn.setOperationId(hmeEoJobSn.getOperationId());
        hmeWoJobSn = hmeWoJobSnRepository.selectOne(hmeWoJobSn);
        hmeWoJobSn.setProcessedNum(hmeWoJobSn.getProcessedNum() + 1L);
        hmeWoJobSnMapper.updateByPrimaryKeySelective(hmeWoJobSn);
    }

    /**
     *
     * @Description COS芯片录入-热沉号保存
     *
     * @author yifan.xiong
     * @date 2020-9-1 11:29:08
     * @param tenantId 租户ID
     * @param dto 参数
     * @return void
     *
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveHotsink(Long tenantId, HmeMaterialLotLoadVO3 dto) {
        HmeMaterialLotLoad hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setHotSinkCode(dto.getHotSinkCode());
        hmeMaterialLotLoad.setTenantId(tenantId);
        int count = hmeMaterialLotLoadRepository.selectCount(hmeMaterialLotLoad);
        if(count > 0){
            throw new MtException("HME_COS_008", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_008", "HME"));
        }
        hmeMaterialLotLoad = new HmeMaterialLotLoad();
        hmeMaterialLotLoad.setMaterialLotLoadId(dto.getMaterialLotLoadId());
        hmeMaterialLotLoad = hmeMaterialLotLoadRepository.selectByPrimaryKey(hmeMaterialLotLoad);
        hmeMaterialLotLoad.setHotSinkCode(dto.getHotSinkCode());
        hmeMaterialLotLoadMapper.updateByPrimaryKeySelective(hmeMaterialLotLoad);
    }
}