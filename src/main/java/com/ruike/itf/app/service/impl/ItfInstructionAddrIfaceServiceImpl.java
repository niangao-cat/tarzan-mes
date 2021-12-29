package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.ItfInstructionAddrIfaceDTO;
import com.ruike.itf.app.service.ItfInstructionAddrIfaceService;
import com.ruike.itf.domain.entity.ItfInstructionAddrIface;
import com.ruike.itf.domain.vo.ItfInstructionAddrVO;
import com.ruike.itf.infra.mapper.ItfInstructionAddrIfaceMapper;
import com.ruike.itf.utils.GetDeclaredFields;
import com.ruike.wms.domain.entity.WmsPoDeliveryRel;
import com.ruike.wms.infra.mapper.WmsPoDeliveryRelMapper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 送货单的文件地址表应用服务默认实现
 *
 * @author kejin.liu@hand-china.com 2020-10-27 15:24:39
 */
@Service
public class ItfInstructionAddrIfaceServiceImpl implements ItfInstructionAddrIfaceService {


    private final ItfInstructionAddrIfaceMapper itfInstructionAddrIfaceMapper;
    private final WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper;
    private final MtCustomDbRepository customDbRepository;

    @Value("${hwms.system.tenantId}")
    private Long tenantId;

    public ItfInstructionAddrIfaceServiceImpl(ItfInstructionAddrIfaceMapper itfInstructionAddrIfaceMapper, WmsPoDeliveryRelMapper wmsPoDeliveryRelMapper, MtCustomDbRepository customDbRepository) {
        this.itfInstructionAddrIfaceMapper = itfInstructionAddrIfaceMapper;
        this.wmsPoDeliveryRelMapper = wmsPoDeliveryRelMapper;
        this.customDbRepository = customDbRepository;
    }

    @Override
    public List<ItfInstructionAddrIfaceDTO> srmInstructionAddrCreate(List<ItfInstructionAddrIfaceDTO> ifaceSyncDTOS) {

        if (CollectionUtils.isEmpty(ifaceSyncDTOS)) {
            return new ArrayList<>();
        }
        List<String> addrIfaceKey = customDbRepository.getNextKeys("itf_instruction_addr_iface_s", ifaceSyncDTOS.size());
        // 校验必输字段
        List<ItfInstructionAddrIfaceDTO> ifaceList = isNullInstructionAddr(ifaceSyncDTOS);
        List<ItfInstructionAddrIfaceDTO> errorData = ifaceList.stream().filter(a -> "E".equals(a.getStatus())).collect(Collectors.toList());
        List<ItfInstructionAddrIfaceDTO> instructionAddrList = ifaceList.stream().filter(a -> !"E".equals(a.getStatus())).collect(Collectors.toList());
        // 校验单号是否存在
        List<ItfInstructionAddrIfaceDTO> docNumAndSrmLineNums = isDocNumExist(instructionAddrList);
        // 错误数据
        List<ItfInstructionAddrIfaceDTO> docNumIsNull = docNumAndSrmLineNums.stream().filter(a -> !"S".equals(a.getStatus())).collect(Collectors.toList());
        // 成功数据
        List<ItfInstructionAddrIfaceDTO> newData = docNumAndSrmLineNums.stream().filter(a -> "S".equals(a.getStatus())).collect(Collectors.toList());
        for (ItfInstructionAddrIfaceDTO dto : newData) {
            WmsPoDeliveryRel rel = new WmsPoDeliveryRel();
            rel.setPoDeliveryRelId(dto.getMessage());
            rel.setAttribute1(dto.getAttribute1());
            wmsPoDeliveryRelMapper.updateByPrimaryKeySelective(rel);
        }

        errorData.addAll(docNumIsNull);
        errorData.addAll(newData);

        for (ItfInstructionAddrIfaceDTO dto : errorData) {
            ItfInstructionAddrIface itfInstructionAddrIface = new ItfInstructionAddrIface();
            BeanUtils.copyProperties(dto, itfInstructionAddrIface);
            itfInstructionAddrIface.setTenantId(tenantId);
            itfInstructionAddrIface.setCid(-1L);
            itfInstructionAddrIface.setObjectVersionNumber(1L);
            itfInstructionAddrIface.setCreatedBy(-1L);
            itfInstructionAddrIface.setLastUpdatedBy(-1L);
            itfInstructionAddrIface.setIfaceId(addrIfaceKey.remove(0));
            itfInstructionAddrIfaceMapper.insertSelective(itfInstructionAddrIface);
        }
        return errorData;
    }

    /**
     * 校验送货单是否存在
     *
     * @param instructionAddrList
     * @return
     */
    private List<ItfInstructionAddrIfaceDTO> isDocNumExist(List<ItfInstructionAddrIfaceDTO> instructionAddrList) {
        if (CollectionUtils.isEmpty(instructionAddrList)) {
            return new ArrayList<>();
        }
        List<String> docNums = instructionAddrList.stream().map(ItfInstructionAddrIfaceDTO::getInstructionDocNum).distinct().collect(Collectors.toList());
        List<ItfInstructionAddrVO> addrVOList = itfInstructionAddrIfaceMapper.selectDocNumAndSrmLineNumByDocNum(tenantId, docNums);
        Map<String, ItfInstructionAddrVO> addrVOMap = addrVOList.stream().collect(Collectors.toMap(k -> k.getDocNum() + "-" + k.getSrmLineNum(), dto -> dto));
        instructionAddrList.forEach(addr -> {
            String mapKey = addr.getInstructionDocNum() + "-" + addr.getSrmLineNum();
            ItfInstructionAddrVO vo = addrVOMap.get(mapKey);
            if (Objects.isNull(vo)) {
                addr.setStatus("E");
                addr.setMessage("送货单或者行号不存在");
            } else {
                addr.setStatus("S");
                addr.setMessage(vo.getPoDeliveryRelId());// 暂时用message代替PoDeliveryRelId主键字段
            }
        });
        return instructionAddrList;
    }

    /**
     * 校验是否为空
     *
     * @param ifaceSyncDTOS
     * @return
     */
    private List<ItfInstructionAddrIfaceDTO> isNullInstructionAddr(List<ItfInstructionAddrIfaceDTO> ifaceSyncDTOS) {
        GetDeclaredFields<ItfInstructionAddrIfaceDTO> getDeclaredFields = new GetDeclaredFields();
        String[] fields = {"instructionDocNum", "srmLineNum", "attribute1"};
        ifaceSyncDTOS.forEach(sync -> {
            List<String> declaredFields = getDeclaredFields.getDeclaredFields(sync, fields);
            if (CollectionUtils.isNotEmpty(declaredFields)) {
                sync.setStatus("E");
                sync.setMessage(declaredFields.toString() + "不可为空！");
            }
        });
        return ifaceSyncDTOS;
    }
}
