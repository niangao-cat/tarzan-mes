package tarzan.inventory.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import tarzan.inventory.api.dto.MtInvOnhandQuantityDTO;
import tarzan.inventory.app.service.MtInvOnhandHoldService;
import tarzan.inventory.domain.entity.MtInvOnhandHold;
import tarzan.inventory.domain.repository.MtInvOnhandHoldRepository;
import tarzan.inventory.domain.vo.MtInvOnhandHoldVO3;
import tarzan.order.domain.entity.MtEo;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtEoRepository;
import tarzan.order.domain.repository.MtWorkOrderRepository;

/**
 * 库存保留量应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:04:53
 */
@Service
public class MtInvOnhandHoldServiceImpl implements MtInvOnhandHoldService {

    @Autowired
    private MtInvOnhandHoldRepository mtInvOnhandHoldRepository;

    @Autowired
    private MtEoRepository mtEoRepository;
    @Autowired
    private MtWorkOrderRepository mtWorkOrderRepository;

    @Override
    public Page<MtInvOnhandQuantityDTO> queryInventoryHoldQuantityForUi(Long tenantId, MtInvOnhandHoldVO3 holdVO,
                    PageRequest pageRequest) {
        Page<String> holdIdList = PageHelper.doPage(pageRequest,
                        () -> mtInvOnhandHoldRepository.propertyLimitOnhandReserveQuery(tenantId, holdVO));

        List<MtInvOnhandQuantityDTO> result = new ArrayList<>();
        MtInvOnhandQuantityDTO mtInvOnhandQuantityDTO;
        MtInvOnhandHold mtInvOnhandHold;
        for (String holdId : holdIdList) {
            mtInvOnhandHold = mtInvOnhandHoldRepository.onhandReserveGet(tenantId, holdId);
            switch (mtInvOnhandHold.getOrderType()) {
                case "EO":
                    break;
                case "WO":
                    break;
                default:
                    break;
            }

            mtInvOnhandQuantityDTO = new MtInvOnhandQuantityDTO();
            BeanUtils.copyProperties(mtInvOnhandHold, mtInvOnhandQuantityDTO);
            result.add(mtInvOnhandQuantityDTO);
        }

        List<MtInvOnhandQuantityDTO> eoList =
                        result.stream().filter(r -> "EO".equals(r.getOrderType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(eoList)) {
            List<MtEo> originEoList = mtEoRepository.eoPropertyBatchGet(tenantId,
                            eoList.stream().map(MtInvOnhandQuantityDTO::getOrderId).collect(Collectors.toList()));
            for (MtInvOnhandQuantityDTO eo : eoList) {
                originEoList.stream().filter(e -> eo.getOrderId().equals(e.getEoId())).findAny()
                                .ifPresent(t -> eo.setOrderId(t.getEoNum()));
            }
        }
        List<MtInvOnhandQuantityDTO> woList =
                        result.stream().filter(r -> "WO".equals(r.getOrderType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(woList)) {
            List<MtWorkOrder> originWoList = mtWorkOrderRepository.woPropertyBatchGet(tenantId,
                            woList.stream().map(MtInvOnhandQuantityDTO::getOrderId).collect(Collectors.toList()));
            for (MtInvOnhandQuantityDTO wo : woList) {
                originWoList.stream().filter(e -> wo.getOrderId().equals(e.getWorkOrderId())).findAny()
                                .ifPresent(t -> wo.setOrderId(t.getWorkOrderNum()));
            }
        }

        Page<MtInvOnhandQuantityDTO> page = new Page<MtInvOnhandQuantityDTO>();
        page.setContent(result);
        page.setNumber(holdIdList.getNumber());
        page.setSize(holdIdList.getSize());
        page.setTotalElements(holdIdList.getTotalElements());
        page.setTotalPages(holdIdList.getTotalPages());
        page.setNumberOfElements(holdIdList.getNumberOfElements());

        return page;
    }
}
