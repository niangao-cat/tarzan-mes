package tarzan.modeling.infra.repository.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.entity.MtGenType;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtGenTypeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtGenTypeVO2;
import tarzan.modeling.domain.entity.MtModSiteManufacturing;
import tarzan.modeling.domain.repository.MtModSiteManufacturingRepository;
import tarzan.modeling.domain.vo.MtModSiteManufacturingVO;
import tarzan.modeling.infra.mapper.MtModSiteManufacturingMapper;

/**
 * 站点生产属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:58
 */
@Component
public class MtModSiteManufacturingRepositoryImpl extends BaseRepositoryImpl<MtModSiteManufacturing>
                implements MtModSiteManufacturingRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModSiteManufacturingMapper mtModSiteManufacturingMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Override
    public MtModSiteManufacturing siteManufacturingPropertyGet(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteManufacturingPropertyGet】"));
        }

        MtModSiteManufacturing s = new MtModSiteManufacturing();
        s.setTenantId(tenantId);
        s.setSiteId(siteId);
        return mtModSiteManufacturingMapper.selectOne(s);
    }

    @Override
    public List<MtModSiteManufacturing> siteManufacturingPropertyBatchGet(Long tenantId, List<String> siteId) {
        if (CollectionUtils.isEmpty(siteId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteManufacturingPropertyBatchGet】"));
        }
        return mtModSiteManufacturingMapper.selectByIdsCustom(tenantId, siteId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void siteManufacturingPropertyUpdate(Long tenantId, MtModSiteManufacturingVO dto, String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "siteId", "【API:siteManufacturingPropertyUpdate】"));
        }

        // 1.1 验证 attritionCalculateStrategy 有效性
        if (StringUtils.isNotEmpty(dto.getAttritionCalculateStrategy())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("ATTRITION_STRATEGY");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getAttritionCalculateStrategy())) {
                throw new MtException("MT_MODELING_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                "MODELING", "attritionCalculateStrategy",
                                                "【API:siteManufacturingPropertyUpdate】"));
            }
        }

        // 2. 数据处理
        MtModSiteManufacturing oldData = new MtModSiteManufacturing();
        oldData.setTenantId(tenantId);
        oldData.setSiteId(dto.getSiteId());
        oldData = mtModSiteManufacturingMapper.selectOne(oldData);

        if (oldData == null) {
            // 新增
            MtModSiteManufacturing newData = new MtModSiteManufacturing();
            newData.setTenantId(tenantId);
            newData.setSiteId(dto.getSiteId());
            newData.setAttritionCalculateStrategy(dto.getAttritionCalculateStrategy());
            self().insertSelective(newData);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                BeanUtils.copyProperties(dto, oldData);
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKey(oldData);
            }
            else {
                // 更新
                if (null != dto.getAttritionCalculateStrategy()) {
                    oldData.setAttritionCalculateStrategy(dto.getAttritionCalculateStrategy());
                }
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(oldData);
            }
        }
    }

}
