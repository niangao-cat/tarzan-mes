package tarzan.general.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.general.api.dto.MtTagApiDTO;
import tarzan.general.app.service.MtTagApiService;
import tarzan.general.domain.entity.MtTagApi;
import tarzan.general.domain.repository.MtTagApiRepository;
import tarzan.general.infra.mapper.MtTagApiMapper;

/**
 * API转化表应用服务默认实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:59:19
 */
@Service
public class MtTagApiServiceImpl implements MtTagApiService {

    @Autowired
    private MtTagApiMapper mtTagApiMapper;

    @Autowired
    private MtTagApiRepository mtTagApiRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Page<MtTagApiDTO> listTahApiForUi(Long tenantId, MtTagApiDTO condition, PageRequest pageRequest) {
        MtTagApi mtTagApi = new MtTagApi();
        BeanUtils.copyProperties(condition, mtTagApi);
        mtTagApi.setTenantId(tenantId);
        Criteria criteria = new Criteria(condition);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        whereFields.add(new WhereField(MtTagApi.FIELD_TENANT_ID, Comparison.EQUAL));
        whereFields.add(new WhereField(MtTagApi.FIELD_API_NAME, Comparison.LIKE));

        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        Page<MtTagApi> base =
                        PageHelper.doPageAndSort(pageRequest, () -> mtTagApiMapper.selectOptional(mtTagApi, criteria));

        Page<MtTagApiDTO> result = new Page<MtTagApiDTO>();
        result.setTotalPages(base.getTotalPages());
        result.setTotalElements(base.getTotalElements());
        result.setNumberOfElements(base.getNumberOfElements());
        result.setSize(base.getSize());
        result.setNumber(base.getNumber());
        MtTagApiDTO dto;
        List<MtTagApiDTO> voList = new ArrayList<>();
        for (MtTagApi ever : base) {
            dto = new MtTagApiDTO();
            BeanUtils.copyProperties(ever, dto);
            voList.add(dto);
        }
        result.setContent(voList);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MtTagApiDTO saveTahApiForUi(Long tenantId, MtTagApiDTO condition) {
        MtTagApi mtTagApi = new MtTagApi();
        mtTagApi.setTenantId(tenantId);
        mtTagApi.setApiClass(condition.getApiClass());
        mtTagApi.setApiFunction(condition.getApiFunction());
        mtTagApi = mtTagApiMapper.selectOne(mtTagApi);
        if (null != mtTagApi && (StringUtils.isEmpty(condition.getApiId())
                        || !condition.getApiId().equals(mtTagApi.getApiId()))) {
            throw new MtException("MT_GENERAL_0048",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_GENERAL_0048", "GENERAL"));
        }
        MtTagApi newMtTagApi = new MtTagApi();
        BeanUtils.copyProperties(condition, newMtTagApi);
        newMtTagApi.setTenantId(tenantId);
        if (StringUtils.isNotEmpty(newMtTagApi.getApiId())) {
            mtTagApiRepository.updateByPrimaryKey(newMtTagApi);
        } else {
            mtTagApiRepository.insertSelective(newMtTagApi);
        }
        MtTagApiDTO mtTagApiDTO = new MtTagApiDTO();
        BeanUtils.copyProperties(newMtTagApi, mtTagApiDTO);
        return mtTagApiDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteTahApiForUi(Long tenantId, List<String> condition) {
        if (CollectionUtils.isEmpty(condition)) {
            return 0;
        }

        int delCount = 0;
        MtTagApi mtTagApi;
        for (String id : condition) {
            mtTagApi = new MtTagApi();
            mtTagApi.setTenantId(tenantId);
            mtTagApi.setApiId(id);
            delCount += this.mtTagApiRepository.delete(mtTagApi);
        }

        if (delCount != condition.size()) {
            throw new MtException("数据删除失败.");
        }

        return delCount;
    }
}
