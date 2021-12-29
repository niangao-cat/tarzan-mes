package tarzan.actual.domain.trans.impl;

import org.springframework.stereotype.Component;

import tarzan.actual.domain.trans.MtEoStepWipTransMapper;
import tarzan.actual.domain.vo.MtEoStepActualVO47;
import tarzan.actual.domain.vo.MtEoStepWipVO14;
import tarzan.order.domain.entity.MtEo;


@Component
public class MtEoStepWipTranMapperImpl implements MtEoStepWipTransMapper {

    @Override
    public MtEoStepWipVO14 eoStepActualVO44ToEoStepWipVO14(MtEoStepActualVO47 dto) {
        if (dto == null) {
            return null;
        }

        MtEoStepWipVO14 mtEoStepWipVO14 = new MtEoStepWipVO14();

        mtEoStepWipVO14.setEoStepActualId(dto.getEoStepActualId());
        mtEoStepWipVO14.setQueueQty(dto.getQueueQty());
        mtEoStepWipVO14.setWorkingQty(dto.getWorkingQty());
        mtEoStepWipVO14.setCompletePendingQty(dto.getCompletePendingQty());
        mtEoStepWipVO14.setCompletedQty(dto.getCompletedQty());
        mtEoStepWipVO14.setScrappedQty(dto.getScrappedQty());

        return mtEoStepWipVO14;
    }
}
