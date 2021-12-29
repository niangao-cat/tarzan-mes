package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeWoInputRecordDTO;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO2;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO3;
import com.ruike.hme.api.dto.HmeWoInputRecordDTO5;
import com.ruike.hme.app.service.HmeWoInputRecordService;
import com.ruike.hme.domain.repository.HmeWoInputRecordRepository;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.mapper.HmeWoInputRecordMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 工单投料记录表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-10-27 17:41:58
 */
@Service
public class HmeWoInputRecordServiceImpl implements HmeWoInputRecordService {

    @Autowired
    private com.ruike.hme.domain.repository.HmeWoInputRecordRepository hmeWoInputRecordRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public HmeWoInputRecordDTO workOrderForUi(Long tenantId, String workOrderNum) {
        return hmeWoInputRecordRepository.workOrderGet(tenantId, workOrderNum);
    }

    @Override
    public Page<HmeWoInputRecordDTO2> woInputRecordForUi(Long tenantId, HmeWoInputRecordDTO3 dto, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> hmeWoInputRecordRepository.woInputRecordQuery(tenantId, dto));
    }

    @Override
    public HmeWoInputRecordDTO2 materialLotScanForUi(Long tenantId, HmeWoInputRecordDTO3 dto) {
        return hmeWoInputRecordRepository.materialLotGet(tenantId, dto);
    }

    @Override
    public HmeWoInputRecordDTO5 woInputForUi(Long tenantId, HmeWoInputRecordDTO5 dto) {
        return hmeWoInputRecordRepository.woInputRecordUpdate(tenantId, dto);
    }

    @Override
    public HmeWoInputRecordDTO5 woOutputForUi(Long tenantId, HmeWoInputRecordDTO5 dto) {
        return hmeWoInputRecordRepository.woOutputRecordUpdate(tenantId, dto);
    }
}
