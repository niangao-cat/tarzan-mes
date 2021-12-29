package com.ruike.itf.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruike.hme.domain.entity.HmeEquipment;
import com.ruike.hme.domain.entity.HmeEquipmentWkcRel;
import com.ruike.hme.domain.repository.HmeEquipmentRepository;
import com.ruike.hme.domain.repository.HmeEquipmentWkcRelRepository;
import com.ruike.itf.api.dto.*;
import com.ruike.itf.app.service.ItfSnQueryCollectIfaceService;
import com.ruike.itf.infra.mapper.ItfSnQueryCollectIfaceMapper;
import com.ruike.wms.infra.constant.WmsConstant;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.repository.MtEoRepository;

import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName ItfSnQueryCollectIfaceServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/24 9:41
 * @Version 1.0
 **/
@Service
@Slf4j
public class ItfSnQueryCollectIfaceServiceImpl implements ItfSnQueryCollectIfaceService {
    @Autowired
    private HmeEquipmentRepository hmeEquipmentRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private HmeEquipmentWkcRelRepository hmeEquipmentWkcRelRepository;

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private ItfSnQueryCollectIfaceMapper itfSnQueryCollectIfaceMapper;

    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtMaterialRepository  mtMaterialRepository;
    @Autowired
    private LovAdapter lovAdapter;


    @Override
    public SnQueryCollectItfReturnDTO invoke(Long tenantId, SnQueryCollectItfDTO snQueryCollectItfDTO) {

        SnQueryCollectItfReturnDTO snQueryCollectItfReturnDTO = new SnQueryCollectItfReturnDTO();

        log.info("ItfRmcCollectIfaceServiceImpl.select.start");
        HmeEquipment hmeEquipmentFirst = hmeEquipmentRepository.selectOne(new HmeEquipment() {{
            setTenantId(tenantId);
            setAssetEncoding(snQueryCollectItfDTO.getAssetEncoding());
        }});
        log.info("ItfRmcCollectIfaceServiceImpl.hmeEquipmentFirst :" + hmeEquipmentFirst);
        if (ObjectUtil.isEmpty(hmeEquipmentFirst)) {
            snQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            snQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0011", "ITF", snQueryCollectItfDTO.getAssetEncoding()));
            return snQueryCollectItfReturnDTO;
        }

        List<HmeEquipmentWkcRel> select = hmeEquipmentWkcRelRepository.select(new HmeEquipmentWkcRel() {{
            setEquipmentId(hmeEquipmentFirst.getEquipmentId());
        }});
        if (CollectionUtils.isEmpty(select)) {
            snQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            snQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0012", "ITF", snQueryCollectItfDTO.getAssetEncoding()));
            return snQueryCollectItfReturnDTO;
        }
        String wkcId = select.get(0).getStationId();
        log.info("ItfRmcCollectIfaceServiceImpl.wkcId :" + wkcId);
        String operationId = null;
        if (Strings.isNotBlank(snQueryCollectItfDTO.getOperationName())) {
            List<MtOperation> mtOperations = mtOperationRepository.selectByOperationName(tenantId, Collections.singletonList(snQueryCollectItfDTO.getOperationName()));
            if (CollectionUtils.isEmpty(mtOperations)) {
                snQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
                snQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "ITF_DATA_COLLECT_0016", "ITF", snQueryCollectItfDTO.getOperationName()));
                return snQueryCollectItfReturnDTO;
            }
            operationId =mtOperations.get(0).getOperationId();
        }
        SnQueryCollectItfDTO1 snQueryCollectItfDTO1 = itfSnQueryCollectIfaceMapper.getEoJobId(tenantId, wkcId, operationId);
        if (ObjectUtil.isEmpty(snQueryCollectItfDTO1)) {
            snQueryCollectItfReturnDTO.setProcessStatus(WmsConstant.KEY_IFACE_STATUS_ERROR);
            snQueryCollectItfReturnDTO.setProcessMessage(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "ITF_DATA_COLLECT_0017","ITF", snQueryCollectItfDTO.getAssetEncoding()));
            return snQueryCollectItfReturnDTO;
        }
        MtEo mtEo = mtEoRepository.selectByPrimaryKey(snQueryCollectItfDTO1.getEoId());
        snQueryCollectItfReturnDTO.setAssetEncoding(snQueryCollectItfDTO.getAssetEncoding());
        snQueryCollectItfReturnDTO.setSn(mtEo.getIdentification());
        if (Strings.isNotBlank(mtEo.getMaterialId())) {
            MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(mtEo.getMaterialId());
            snQueryCollectItfReturnDTO.setMaterialCode(mtMaterial.getMaterialCode());
        }
        String facMaterialCode = itfSnQueryCollectIfaceMapper.getfacMaterialCode(tenantId, snQueryCollectItfDTO1.getJobId());
        snQueryCollectItfReturnDTO.setFacMaterialCode(facMaterialCode);
        List<SnQueryCollectItfReturnDTO1> snQueryCollectItfReturnDTO1List = itfSnQueryCollectIfaceMapper.getResultList(tenantId, snQueryCollectItfDTO1.getJobId(), snQueryCollectItfDTO.getTagTypes());
        List<LovValueDTO> tagList  = lovAdapter.queryLovValue("HME.TAG_TYPE", tenantId);
        List<SnQueryCollectItfReturnDTO2> snQueryCollectItfReturnDTO2List =new ArrayList<>();
        for (String tagTypes:
        snQueryCollectItfDTO.getTagTypes()) {
            SnQueryCollectItfReturnDTO2 snQueryCollectItfReturnDTO21 = new SnQueryCollectItfReturnDTO2();
            snQueryCollectItfReturnDTO21.setAttrValue(tagTypes);
            Optional<LovValueDTO> tag = tagList.stream().filter(dto -> StringUtils.equals(dto.getValue(), tagTypes)).findFirst();
            if(tag.isPresent()) {
                snQueryCollectItfReturnDTO21.setTagType(tag.get().getMeaning());
            }
            List<SnQueryCollectItfReturnDTO1> collect = snQueryCollectItfReturnDTO1List.stream().filter(t -> t.getAttrValue().equals(tagTypes)).collect(Collectors.toList());
            for (SnQueryCollectItfReturnDTO1 temp :
                    collect) {
                String[] camels = temp.getTagCode().split("-");
                List<String> current = Arrays.stream(camels).filter(t -> t.endsWith("A")).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(current))
                {
                    temp.setCurrent(current.get(0));
                }
                List<String> duration = Arrays.stream(camels).filter(t -> t.endsWith("H")).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(duration))
                {
                    temp.setDuration(duration.get(0));
                }
                List<String> cosPos = Arrays.stream(camels).filter(t -> t.startsWith("COS")).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(cosPos))
                {
                    temp.setCosPos(cosPos.get(0));
                }
            }
            snQueryCollectItfReturnDTO21.setResultList(collect);
            snQueryCollectItfReturnDTO2List.add(snQueryCollectItfReturnDTO21);
        }
        String cosNum = itfSnQueryCollectIfaceMapper.selectCosNum(tenantId,snQueryCollectItfDTO1.getEoId());
        if(Strings.isBlank(cosNum))
        {
            snQueryCollectItfReturnDTO.setCosNum(0L);
        }else
        {
            snQueryCollectItfReturnDTO.setCosNum(Long.valueOf(cosNum));
        }
        snQueryCollectItfReturnDTO.setResultList(snQueryCollectItfReturnDTO2List);
        return snQueryCollectItfReturnDTO;
    }

}
