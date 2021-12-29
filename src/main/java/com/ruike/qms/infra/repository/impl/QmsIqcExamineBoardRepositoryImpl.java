package com.ruike.qms.infra.repository.impl;

import com.ruike.qms.api.dto.QmsIqcExamineBoardDTO;
import com.ruike.qms.domain.repository.QmsIqcExamineBoardRepository;
import com.ruike.qms.infra.mapper.QmsIqcExamineBoardMapper;
import io.tarzan.common.domain.sys.MtUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description
 *
 * @author jiangling.zheng@hand-china.com 2020-05-19 11:18
 */
@Component
public class QmsIqcExamineBoardRepositoryImpl implements QmsIqcExamineBoardRepository {

    @Autowired
    private QmsIqcExamineBoardMapper qmsIqcExamineBoardMapper;

    @Autowired
    private MtUserClient userClient;

    @Override
    public List<QmsIqcExamineBoardDTO> selectIqcExamineBoardForUi(Long tenantId) {
        List<QmsIqcExamineBoardDTO> list = qmsIqcExamineBoardMapper.selectIqcExamineBoard(tenantId);
        list.forEach(dto -> {
            dto.setQcByName(userClient.userInfoGet(tenantId, dto.getQcBy()).getRealName());
        });
        return list;
    }
}
