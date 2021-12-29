package com.ruike.qms.app.service.impl;

import com.ruike.qms.api.dto.QmsSampleSizeCodeLetterDTO;
import com.ruike.qms.app.service.QmsSampleSizeCodeLetterService;
import com.ruike.qms.domain.entity.QmsSampleSizeCodeLetter;
import com.ruike.qms.domain.repository.QmsSampleSizeCodeLetterRepository;
import com.ruike.qms.infra.constant.QmsConstants;
import com.ruike.qms.infra.mapper.QmsSampleSizeCodeLetterMapper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;

/**
 * 样本量字码表应用服务默认实现
 *
 * @author jiangling.zheng@hand-china.com 2020-04-30 15:05:11
 */
@Service
public class QmsSampleSizeCodeLetterServiceImpl extends BaseServiceImpl<QmsSampleSizeCodeLetter> implements QmsSampleSizeCodeLetterService {

    @Autowired
    private QmsSampleSizeCodeLetterMapper qmsSampleSizeCodeLetterMapper;
    @Autowired
    private QmsSampleSizeCodeLetterRepository qmsSampleSizeCodeLetterRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    @ProcessLovValue
    public Page<QmsSampleSizeCodeLetterDTO> listSampleSizeCodeLetterForUi(Long tenantId, Long lotSize, PageRequest pageRequest) {
        return PageHelper.doPage(pageRequest, () -> qmsSampleSizeCodeLetterMapper.selectByConditionForUi(tenantId, lotSize));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSampleSizeCodeLetterForUi(Long tenantId, List<QmsSampleSizeCodeLetterDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        for (QmsSampleSizeCodeLetterDTO dto : dtoList) {
            // 校验是否存在上下限交叉情况
            int fromCount = qmsSampleSizeCodeLetterMapper.selectByConditionCount(tenantId, dto.getLetterId(), dto.getLotSizeFrom());
            int toCount = qmsSampleSizeCodeLetterMapper.selectByConditionCount(tenantId, dto.getLetterId(), dto.getLotSizeTo());
            if (fromCount > 0 || toCount > 0) {
                throw new MtException(QmsConstants.ErrorCode.QMS_SAMPLE_LETTER_0001, mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        QmsConstants.ErrorCode.QMS_SAMPLE_LETTER_0001, QmsConstants.ConstantValue.QMS));
            }
            QmsSampleSizeCodeLetter letter = new QmsSampleSizeCodeLetter();
            if (StringUtils.isNotEmpty(dto.getLetterId())) {
                QmsSampleSizeCodeLetter oldLetter = qmsSampleSizeCodeLetterRepository.selectByPrimaryKey(dto.getLetterId());
                BeanUtils.copyProperties(oldLetter, letter);
            }
            BeanUtils.copyProperties(dto, letter);
            letter.setTenantId(tenantId);
            if (StringUtils.isNotEmpty(letter.getLetterId())) {
                qmsSampleSizeCodeLetterMapper.updateByPrimaryKey(letter);
            } else {
                insertSelective(letter);
            }
        }
    }
}
