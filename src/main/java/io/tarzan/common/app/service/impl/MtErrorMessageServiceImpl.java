package io.tarzan.common.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.service.BaseServiceImpl;
import io.tarzan.common.api.dto.MtErrorMessageDTO3;
import io.tarzan.common.api.dto.MtErrorMessageDTO4;
import io.tarzan.common.app.service.MtErrorMessageService;
import io.tarzan.common.domain.entity.MtErrorMessage;
import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.vo.MtErrorMessageVO;
import io.tarzan.common.domain.vo.MtErrorMessageVO2;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import io.tarzan.common.infra.mapper.MtErrorMessageMapper;

/**
 * 应用服务默认实现
 *
 * @author MrZ 2019-05-21 17:09:05
 */
@Service
public class MtErrorMessageServiceImpl extends BaseServiceImpl<MtErrorMessage> implements MtErrorMessageService {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtErrorMessageMapper mtErrorMessageMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public Page<MtErrorMessageVO> listErrorMessageForUi(Long tenantId, MtErrorMessageDTO3 condition,
                                                        PageRequest pageRequest) {

        MtGenTypeVO2 queryType = new MtGenTypeVO2();
        queryType.setModule("GENERAL");
        queryType.setTypeGroup("SERVICE_PACKAGE");
        List<MtGenType> types = mtGenTypeRepository.groupLimitTypeQuery(tenantId, queryType);

        MtErrorMessage mtErrorMessage = new MtErrorMessage();
        BeanUtils.copyProperties(condition, mtErrorMessage);
        mtErrorMessage.setTenantId(tenantId);

        Criteria criteria = new Criteria(mtErrorMessage);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtErrorMessage.FIELD_TENANT_ID, Comparison.EQUAL));

        if (StringUtils.isNotEmpty(mtErrorMessage.getMessageCode())) {
            whereFields.add(new WhereField(MtErrorMessage.FIELD_MESSAGE_CODE, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(mtErrorMessage.getMessage())) {
            whereFields.add(new WhereField(MtErrorMessage.FIELD_MESSAGE, Comparison.LIKE));
        }

        if (StringUtils.isNotEmpty(mtErrorMessage.getModule())) {
            whereFields.add(new WhereField(MtErrorMessage.FIELD_MODULE, Comparison.EQUAL));
        }

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtErrorMessage> base = PageHelper.doPageAndSort(pageRequest,
                        () -> mtErrorMessageMapper.selectOptional(mtErrorMessage, criteria));


        Page<MtErrorMessageVO> result = new Page<MtErrorMessageVO>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());

        List<MtErrorMessageVO> voList = new ArrayList<MtErrorMessageVO>();
        for (MtErrorMessage message : base) {
            MtErrorMessageVO vo = new MtErrorMessageVO();
            BeanUtils.copyProperties(message, vo);
            Optional<MtGenType> statusOp =
                            types.stream().filter(t -> t.getTypeCode().equals(message.getModule())).findFirst();
            statusOp.ifPresent(t -> vo.setModuleDesc(t.getDescription()));
            voList.add(vo);
        }
        result.setContent(voList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveErrorMessageForUi(Long tenantId, MtErrorMessageDTO4 dto) {
        MtErrorMessage mtErrorMessage = new MtErrorMessage();
        BeanUtils.copyProperties(dto, mtErrorMessage);
        mtErrorMessage.setTenantId(tenantId);

        return this.mtErrorMessageRepository.messageBasicPropertyUpdate(tenantId, mtErrorMessage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteErrorMessageForUi(Long tenantId, List<MtErrorMessageVO2> list) {
        this.mtErrorMessageRepository.removeMessage(tenantId, list);
    }

}
