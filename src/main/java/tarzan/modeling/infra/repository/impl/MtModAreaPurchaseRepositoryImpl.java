package tarzan.modeling.infra.repository.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.modeling.domain.entity.MtModAreaPurchase;
import tarzan.modeling.domain.repository.MtModAreaPurchaseRepository;
import tarzan.modeling.domain.vo.MtModAreaPurchaseVO;
import tarzan.modeling.infra.mapper.MtModAreaPurchaseMapper;

/**
 * 区域采购属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModAreaPurchaseRepositoryImpl extends BaseRepositoryImpl<MtModAreaPurchase>
                implements MtModAreaPurchaseRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModAreaPurchaseMapper mtModAreaPurchaseMapper;

    @Override
    public MtModAreaPurchase areaPurchasePropertyGet(Long tenantId, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaPurchasePropertyGet】"));
        }

        MtModAreaPurchase area = new MtModAreaPurchase();
        area.setTenantId(tenantId);
        area.setAreaId(areaId);
        return this.mtModAreaPurchaseMapper.selectOne(area);
    }

    @Override
    public List<MtModAreaPurchase> areaPurchasePropertyBatchGet(Long tenantId, List<String> areaIds) {
        if (CollectionUtils.isEmpty(areaIds)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaPurchasePropertyBatchGet】"));
        }
        return this.mtModAreaPurchaseMapper.selectByIdsCustom(tenantId, areaIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void areaPurchasePropertyUpdate(Long tenantId, MtModAreaPurchaseVO dto, String fullUpdate) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getAreaId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "areaId", "【API:areaPurchasePropertyUpdate】"));
        }
        if (StringUtils.isNotEmpty(dto.getInsideFlag()) && !Arrays.asList("Y", "N").contains(dto.getInsideFlag())) {
            throw new MtException("MT_MODELING_0035", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0035", "MODELING", "insideFlag", "【API:areaPurchasePropertyUpdate】"));
        }

        // 2. 数据处理
        MtModAreaPurchase oldData = new MtModAreaPurchase();
        oldData.setTenantId(tenantId);
        oldData.setAreaId(dto.getAreaId());
        oldData = mtModAreaPurchaseMapper.selectOne(oldData);

        if (oldData == null) {
            // 新增
            MtModAreaPurchase newData = new MtModAreaPurchase();
            newData.setTenantId(tenantId);
            newData.setAreaId(dto.getAreaId());
            newData.setInsideFlag(dto.getInsideFlag());
            newData.setSupplierId(dto.getSupplierId());
            newData.setSupplierSiteId(dto.getSupplierSiteId());
            self().insertSelective(newData);
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                oldData.setInsideFlag(dto.getInsideFlag());
                oldData.setSupplierId(dto.getSupplierId());
                oldData.setSupplierSiteId(dto.getSupplierSiteId());
                self().updateByPrimaryKey(oldData);
            } else {
                // 更新
                if (null != dto.getInsideFlag()) {
                    oldData.setInsideFlag(dto.getInsideFlag());
                }
                if (null != dto.getSupplierId()) {
                    oldData.setSupplierId(dto.getSupplierId());
                }
                if (null != dto.getSupplierSiteId()) {
                    oldData.setSupplierSiteId(dto.getSupplierSiteId());
                }
                self().updateByPrimaryKeySelective(oldData);
            }
        }
    }

}
