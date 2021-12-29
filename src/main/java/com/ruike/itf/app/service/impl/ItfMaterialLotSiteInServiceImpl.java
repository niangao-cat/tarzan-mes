package com.ruike.itf.app.service.impl;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeCosPoorInspectionService;
import com.ruike.hme.app.service.HmeVisualInspectionService;
import com.ruike.hme.app.service.HmeWorkcellEquipmentSwitchService;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.repository.HmeEoJobSnRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeSignInOutRecordMapper;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO;
import com.ruike.itf.api.dto.ItfMaterialLotSiteDTO2;
import com.ruike.itf.app.service.ItfMaterialLotSiteInService;
import com.ruike.itf.domain.repository.ItfMaterialLotSiteInRepository;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfMaterialLotSiteInMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ItfMaterialLotSiteInServiceImpl
 * 盒子进站
 * @author: chaonan.hu@hand-china.com 2021-10-11 17:04:12
 **/
@Service
@Slf4j
public class ItfMaterialLotSiteInServiceImpl implements ItfMaterialLotSiteInService {

    @Autowired
    private ItfMaterialLotSiteInRepository itfMaterialLotSiteInRepository;
    @Autowired
    private HmeEoJobSnRepository hmeEoJobSnRepository;
    @Autowired
    private HmeWorkcellEquipmentSwitchService hmeWorkcellEquipmentSwitchService;
    @Autowired
    private HmeVisualInspectionService hmeVisualInspectionService;
    @Autowired
    private HmeSignInOutRecordMapper hmeSignInOutRecordMapper;
    @Autowired
    private ItfMaterialLotSiteInMapper itfMaterialLotSiteInMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private HmeCosPoorInspectionService hmeCosPoorInspectionService;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public ItfMaterialLotSiteInVO materialLotSiteIn(Long tenantId, ItfMaterialLotSiteDTO dto) {
        //数据初步校验
        itfMaterialLotSiteInRepository.materialLotSiteInVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("COS_TEST");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return handleMaterialLotSiteIn(tenantId, dto, hmeEoJobSnVO4);
    }

    @Override
    public ItfMaterialLotSiteInVO3 materialLotSiteOut(Long tenantId, ItfMaterialLotSiteDTO2 dto) {
        //出站数据初步校验
        itfMaterialLotSiteInRepository.materialLotSiteOutVerify(tenantId, dto);
        //工位扫描
        HmeEoJobSnDTO hmeEoJobSnDTO = new HmeEoJobSnDTO();
        hmeEoJobSnDTO.setSiteId(dto.getDefaultSiteId());
        hmeEoJobSnDTO.setWorkcellCode(dto.getWorkcellCode());
        hmeEoJobSnDTO.setJobType("COS_TEST");
        HmeEoJobSnVO4 hmeEoJobSnVO4 = hmeEoJobSnRepository.workcellScan(tenantId, hmeEoJobSnDTO);
        return handleMaterialLotSiteOut(tenantId, dto, hmeEoJobSnVO4);
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfMaterialLotSiteInVO handleMaterialLotSiteIn(Long tenantId, ItfMaterialLotSiteDTO dto, HmeEoJobSnVO4 hmeEoJobSnVO4){
        if(StringUtils.isNotBlank(dto.getScanAssetEncoding())){
            //查询设备类型
            List<String> equipmentCategoryList = itfMaterialLotSiteInRepository.getEquipmentCategory(tenantId, dto.getWorkcellCode(), dto.getDefaultSiteId());
            //工位绑定设备
            itfMaterialLotSiteInRepository.bindEquipment(tenantId, dto.getHmeEquipmentList(), dto.getWorkcellId(), dto.getDefaultSiteId(), equipmentCategoryList);
        }
        //查询工位最新的绑定设备信息以及状态
        HmeWkcEquSwitchDTO hmeWkcEquSwitchDTO = new HmeWkcEquSwitchDTO();
        hmeWkcEquSwitchDTO.setSiteId(dto.getDefaultSiteId());
        hmeWkcEquSwitchDTO.setWorkcellCode(dto.getWorkcellCode());
        HmeWkcEquSwitchVO2 hmeWkcEquSwitchVO2 = hmeWorkcellEquipmentSwitchService.getEquCategoryAndAssetEncoding(tenantId, hmeWkcEquSwitchDTO);
        List<HmeWkcEquSwitchVO> hmeWkcEquSwitchVOS = hmeWkcEquSwitchVO2.getHmeWkcEquSwitchVOS();
        //条码扫描
        HmeVisualInspectionDTO2 hmeVisualInspectionDTO2 = new HmeVisualInspectionDTO2();
        hmeVisualInspectionDTO2.setSiteId(dto.getDefaultSiteId());
        hmeVisualInspectionDTO2.setWorkcellId(dto.getWorkcellId());
        hmeVisualInspectionDTO2.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
        hmeVisualInspectionDTO2.setOperationId(hmeEoJobSnVO4.getOperationId());
        hmeVisualInspectionDTO2.setMaterialLotCode(dto.getMaterialLotCode());
        itfMaterialLotSiteInRepository.scanMaterialLotVerify(tenantId, hmeVisualInspectionDTO2);
        itfMaterialLotSiteInRepository.scanMaterialLot(tenantId, hmeVisualInspectionDTO2, hmeWkcEquSwitchVOS);
        //条码查询
        HmeVisualInspectionDTO hmeVisualInspectionDTO = new HmeVisualInspectionDTO();
        hmeVisualInspectionDTO.setWorkcellId(dto.getWorkcellId());
        hmeVisualInspectionDTO.setOperationId(hmeEoJobSnVO4.getOperationId());
        List<HmeVisualInspectionVO> hmeVisualInspectionVOList = hmeVisualInspectionService.materialLotQuery(tenantId, hmeVisualInspectionDTO, "COS_TEST");
        //封装返回结果
        ItfMaterialLotSiteInVO itfMaterialLotSiteInVO = new ItfMaterialLotSiteInVO();
        //因设备工位的特殊性，上一步查询工位未出站的所有条码实际上只会有一个未出站条码，即为我们刚扫描进去的条码
        hmeVisualInspectionVOList = hmeVisualInspectionVOList.stream().filter(item -> dto.getMaterialLotCode().equals(item.getMaterialLotCode())).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(hmeVisualInspectionVOList)){
            HmeVisualInspectionVO hmeVisualInspectionVO = hmeVisualInspectionVOList.get(0);
            itfMaterialLotSiteInVO.setMaterialLotCode(hmeVisualInspectionVO.getMaterialLotCode());
            itfMaterialLotSiteInVO.setSnQty(hmeVisualInspectionVO.getSnQty().toString());
            itfMaterialLotSiteInVO.setWorkOrderNum(hmeVisualInspectionVO.getWorkOrderNum());
            itfMaterialLotSiteInVO.setWaferNum(hmeVisualInspectionVO.getWaferNum());
            itfMaterialLotSiteInVO.setCosType(hmeVisualInspectionVO.getCosType());
            itfMaterialLotSiteInVO.setEoStepNum(hmeVisualInspectionVO.getEoStepNum());
            //根据工位查询工序
            MtModWorkcell process = hmeSignInOutRecordMapper.getProcessByWorkcell(tenantId, dto.getWorkcellId());
            if(Objects.nonNull(process)){
                itfMaterialLotSiteInVO.setCurrentStepName(process.getWorkcellCode());
                itfMaterialLotSiteInVO.setCurrentStepDescription(process.getWorkcellName());
            }
            //根据工单ID查询工单完工数量，工单数量，生产版本等信息
            ItfMaterialLotSiteInVO woInfo = itfMaterialLotSiteInMapper.woInfoQuery(tenantId, hmeVisualInspectionVO.getWorkOrderId());
            itfMaterialLotSiteInVO.setWoQuantityOut(HmeConstants.ConstantValue.STRING_ZERO);
            itfMaterialLotSiteInVO.setWoQuantity(HmeConstants.ConstantValue.STRING_ZERO);
            if (woInfo != null) {
                itfMaterialLotSiteInVO.setWoQuantityOut(woInfo.getWoQuantityOut());
                itfMaterialLotSiteInVO.setWoQuantity(woInfo.getWoQuantity());
                itfMaterialLotSiteInVO.setProductionVersion(woInfo.getProductionVersion());
            }
            //根据物料批ID查询SAP料号，物料描述，实验代码，实验代码备注等信息
            ItfMaterialLotSiteInVO materialLotInfo = itfMaterialLotSiteInMapper.materialLotInfoQuery(tenantId, hmeVisualInspectionVO.getMaterialLotId());
            itfMaterialLotSiteInVO.setSnMaterialCode(materialLotInfo.getSnMaterialCode());
            itfMaterialLotSiteInVO.setSnMaterialName(materialLotInfo.getSnMaterialName());
            itfMaterialLotSiteInVO.setLabCode(materialLotInfo.getLabCode());
            itfMaterialLotSiteInVO.setLabCodeRemark(materialLotInfo.getLabCodeRemark());
        }
        return itfMaterialLotSiteInVO;
    }

    @Transactional(rollbackFor = Exception.class)
    public ItfMaterialLotSiteInVO3 handleMaterialLotSiteOut(Long tenantId, ItfMaterialLotSiteDTO2 dto, HmeEoJobSnVO4 hmeEoJobSnVO4){
        //条码查询
        HmeVisualInspectionDTO hmeVisualInspectionDTO = new HmeVisualInspectionDTO();
        hmeVisualInspectionDTO.setWorkcellId(dto.getWorkcellId());
        hmeVisualInspectionDTO.setOperationId(hmeEoJobSnVO4.getOperationId());
        // 20211203 add by sanfeng.zhang for peng.zhao 查询条码最近进站数据
        List<HmeVisualInspectionVO> hmeVisualInspectionVOList = hmeVisualInspectionService.materialLotQuery2(tenantId, hmeVisualInspectionDTO, "COS_TEST");

        //因设备工位的特殊性，上一步查询工位未出站的所有条码实际上只会有一个未出站条码，一般即为我们本次要出站的条码
        hmeVisualInspectionVOList = hmeVisualInspectionVOList.stream().filter(item -> dto.getMaterialLotCode().equals(item.getMaterialLotCode())).collect(Collectors.toList());
        // 排序 取最新的进站记录  如果未出站 则出站 已出站 则对芯片不良进出站 不进行报错
        List<HmeVisualInspectionVO> sortHmeVisualInspectionVOList = hmeVisualInspectionVOList.stream().sorted(Comparator.comparing(HmeVisualInspectionVO :: getSiteInDate).reversed()).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(sortHmeVisualInspectionVOList)){
            //如果没有，则报错 该条码【${1}】未进站,请检查!
            throw new MtException("HME_COS_TEST_009", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_TEST_009", "HME", dto.getMaterialLotCode()));
        }
        HmeVisualInspectionVO hmeVisualInspectionVO = sortHmeVisualInspectionVOList.get(0);
        if (hmeVisualInspectionVO.getSiteOutDate() == null) {
            //条码出站
            HmeVisualInspectionDTO3 hmeVisualInspectionDTO3 = new HmeVisualInspectionDTO3();
            hmeVisualInspectionDTO3.setSiteId(dto.getDefaultSiteId());
            hmeVisualInspectionDTO3.setWorkcellId(dto.getWorkcellId());
            hmeVisualInspectionDTO3.setOperationId(hmeEoJobSnVO4.getOperationId());
            hmeVisualInspectionDTO3.setMaterialLotList(hmeVisualInspectionVOList);
            itfMaterialLotSiteInRepository.materialLotCompleteVerify(tenantId, hmeVisualInspectionDTO3);
            itfMaterialLotSiteInRepository.materialLotComplete(tenantId, hmeVisualInspectionDTO3);
        }

        //根据物料批ID查询是否有需要报废的芯片
        List<ItfMaterialLotSiteInVO4> scrapMaterialLotLoadList = itfMaterialLotSiteInMapper.scrapMaterialLotLoadIdQuery(tenantId, dto.getMaterialLotId());
        if(CollectionUtils.isNotEmpty(scrapMaterialLotLoadList)){
            //芯片不良进站
            HmeCosGetChipScanBarcodeDTO hmeCosGetChipScanBarcodeDTO = new HmeCosGetChipScanBarcodeDTO();
            hmeCosGetChipScanBarcodeDTO.setBarcode(dto.getMaterialLotCode());
            hmeCosGetChipScanBarcodeDTO.setOperationId(hmeEoJobSnVO4.getOperationId());
            hmeCosGetChipScanBarcodeDTO.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
            hmeCosGetChipScanBarcodeDTO.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
            HmeCosPoorInspectionScanBarcodeResponseDTO hmeCosPoorInspectionScanBarcodeResponseDTO = hmeCosPoorInspectionService.siteIn(tenantId, hmeCosGetChipScanBarcodeDTO);
            //芯片不良提交 根据A24分组，每个A24调用一次芯片不良提交接口
            Map<String, List<ItfMaterialLotSiteInVO4>> materialLotLoadMap = scrapMaterialLotLoadList.stream().collect(Collectors.groupingBy(ItfMaterialLotSiteInVO4::getA24));
            for (Map.Entry<String, List<ItfMaterialLotSiteInVO4>> entry:materialLotLoadMap.entrySet()) {
                String a24 = entry.getKey();
                List<ItfMaterialLotSiteInVO4> value = entry.getValue();
                List<String> scrapLoadSequenceList = value.stream().map(ItfMaterialLotSiteInVO4::getLoadSequence)
                        .distinct().collect(Collectors.toList());
                HmeCosPoorInspectionNcRecordDTO hmeCosPoorInspectionNcRecordDTO = new HmeCosPoorInspectionNcRecordDTO();
                hmeCosPoorInspectionNcRecordDTO.setLoadSequenceList(scrapLoadSequenceList);
                hmeCosPoorInspectionNcRecordDTO.setWorkcellId(dto.getWorkcellId());
                hmeCosPoorInspectionNcRecordDTO.setOperationId(hmeEoJobSnVO4.getOperationId());
                List<String> ncCodeList = new ArrayList<>(1);
                ncCodeList.add(a24);
                hmeCosPoorInspectionNcRecordDTO.setNcCodeList(ncCodeList);
                hmeCosPoorInspectionService.ncRecordConfirm(tenantId, hmeCosPoorInspectionNcRecordDTO);
            }
            //芯片报废
            List<String> scrapMaterialLotLoadIdList = scrapMaterialLotLoadList.stream().map(ItfMaterialLotSiteInVO4::getMaterialLotLoadId)
                    .distinct().collect(Collectors.toList());
            HmeCosPoorInspectionScrappedDTO hmeCosPoorInspectionScrappedDTO = new HmeCosPoorInspectionScrappedDTO();
            hmeCosPoorInspectionScrappedDTO.setJobId(hmeCosPoorInspectionScanBarcodeResponseDTO.getEoJobSnId());
            hmeCosPoorInspectionScrappedDTO.setMaterialLotId(dto.getMaterialLotId());
            hmeCosPoorInspectionScrappedDTO.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
            hmeCosPoorInspectionScrappedDTO.setOperationId(hmeEoJobSnVO4.getOperationId());
            hmeCosPoorInspectionScrappedDTO.setWkcShiftId(hmeEoJobSnVO4.getWkcShiftId());
            hmeCosPoorInspectionScrappedDTO.setMaterialLotLoadIdList(scrapMaterialLotLoadIdList);
            hmeCosPoorInspectionService.scrapped(tenantId, hmeCosPoorInspectionScrappedDTO);
            //芯片不良出站
            HmeCosEoJobSnSiteOutVO hmeCosEoJobSnSiteOutVO = new HmeCosEoJobSnSiteOutVO();
            hmeCosEoJobSnSiteOutVO.setWorkcellId(hmeEoJobSnVO4.getWorkcellId());
            hmeCosEoJobSnSiteOutVO.setEoJobSnId(hmeCosPoorInspectionScanBarcodeResponseDTO.getEoJobSnId());
            hmeCosPoorInspectionService.siteOut(tenantId, hmeCosEoJobSnSiteOutVO);
        }
        ItfMaterialLotSiteInVO3 result = new ItfMaterialLotSiteInVO3();
        result.setProcessStatus(ItfConstant.ProcessStatus.SUCCESS);
        result.setProcessMessage("成功");
        return result;
    }
}
