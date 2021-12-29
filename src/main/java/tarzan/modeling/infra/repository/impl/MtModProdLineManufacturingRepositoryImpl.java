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
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModProdLineDispatchOper;
import tarzan.modeling.domain.entity.MtModProdLineManufacturing;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModProdLineDispatchOperRepository;
import tarzan.modeling.domain.repository.MtModProdLineManufacturingRepository;
import tarzan.modeling.domain.vo.MtModProdLineManufacturingVO;
import tarzan.modeling.infra.mapper.MtModProdLineManufacturingMapper;

/**
 * 生产线生产属性 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:30:57
 */
@Component
public class MtModProdLineManufacturingRepositoryImpl extends BaseRepositoryImpl<MtModProdLineManufacturing>
                implements MtModProdLineManufacturingRepository {


    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtModProdLineManufacturingMapper mtModProdLineManufacturingMapper;

    @Autowired
    private MtGenTypeRepository mtGenTypeRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtModProdLineDispatchOperRepository mtModProdLineDispatchOperRepository;

    @Override
    public MtModProdLineManufacturing prodLineManufacturingPropertyGet(Long tenantId, String prodLineId) {
        if (StringUtils.isEmpty(prodLineId)) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineManufacturingPropertyGet】"));
        }

        MtModProdLineManufacturing mtModProdLineManufacturing = new MtModProdLineManufacturing();
        mtModProdLineManufacturing.setTenantId(tenantId);
        mtModProdLineManufacturing.setProdLineId(prodLineId);
        return this.mtModProdLineManufacturingMapper.selectOne(mtModProdLineManufacturing);
    }

    @Override
    public List<MtModProdLineManufacturing> prodLineManufacturingPropertyBatchGet(Long tenantId,
                    List<String> prodLineIds) {
        if (CollectionUtils.isEmpty(prodLineIds)) {
            throw new MtException("MT_MODELING_0001",
                            mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0001", "MODELING",
                                            "prodLineId", "【API:prodLineManufacturingPropertyBatchGet】"));
        }
        return mtModProdLineManufacturingMapper.selectByIdsCustom(tenantId, prodLineIds);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void prodLineManufacturingPropertyUpdate(Long tenantId, MtModProdLineManufacturingVO dto,
                    String fullUpdate) {

        // 1. 验证参数有效性
        if (StringUtils.isEmpty(dto.getProdLineId())) {
            throw new MtException("MT_MODELING_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0001", "MODELING", "prodLineId", "【API:prodLineManufacturingPropertyUpdate】"));
        }

        // 1.1 验证DispatchMethod有效性
        if (StringUtils.isNotEmpty(dto.getDispatchMethod())) {
            MtGenTypeVO2 mtGenTypeVO2 = new MtGenTypeVO2();
            mtGenTypeVO2.setModule("MODELING");
            mtGenTypeVO2.setTypeGroup("DISPATCH_METHOD");
            List<MtGenType> genTypes = mtGenTypeRepository.groupLimitTypeQuery(tenantId, mtGenTypeVO2);

            // 所有类型汇总
            List<String> types = genTypes.stream().map(MtGenType::getTypeCode).collect(Collectors.toList());
            if (!types.contains(dto.getDispatchMethod())) {
                throw new MtException("MT_MODELING_0002",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0002",
                                                "MODELING", "dispatchMethod",
                                                "【API:prodLineManufacturingPropertyUpdate】"));
            }
        }

        // 1.2验证库位有效性
        if (StringUtils.isNotEmpty(dto.getIssuedLocatorId())) {
            MtModLocator mtModLocator =
                            mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getIssuedLocatorId());
            if (mtModLocator == null || !"Y".equals(mtModLocator.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                                "MODELING", "issuedLocatorId",
                                                "【API:prodLineManufacturingPropertyUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getCompletionLocatorId())) {
            MtModLocator mtModLocator =
                            mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getCompletionLocatorId());
            if (mtModLocator == null || !"Y".equals(mtModLocator.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                                "MODELING", "completionLocatorId",
                                                "【API:prodLineManufacturingPropertyUpdate】"));
            }
        }
        if (StringUtils.isNotEmpty(dto.getInventoryLocatorId())) {
            MtModLocator mtModLocator =
                            mtModLocatorRepository.locatorBasicPropertyGet(tenantId, dto.getInventoryLocatorId());
            if (mtModLocator == null || !"Y".equals(mtModLocator.getEnableFlag())) {
                throw new MtException("MT_MODELING_0005",
                                mtErrorMessageRepository.getErrorMessageWithModule(tenantId, "MT_MODELING_0005",
                                                "MODELING", "inventoryLocatorId",
                                                "【API:prodLineManufacturingPropertyUpdate】"));
            }
        }

        if ("SPECIAL_OPERATION".equals(dto.getDispatchMethod()) && CollectionUtils.isEmpty(dto.getOperationId())) {
            throw new MtException("MT_MODELING_0039", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_MODELING_0039", "MODELING", "【API:prodLineManufacturingPropertyUpdate】"));
        }

        if (StringUtils.isNotEmpty(dto.getDispatchMethod()) && !"SPECIAL_OPERATION".equals(dto.getDispatchMethod())) {
            dto.setOperationId(null);
        }


        // 2. 数据处理
        MtModProdLineManufacturing oldData = new MtModProdLineManufacturing();
        oldData.setTenantId(tenantId);
        oldData.setProdLineId(dto.getProdLineId());
        oldData = mtModProdLineManufacturingMapper.selectOne(oldData);

        if (oldData == null) {
            // 新增
            MtModProdLineManufacturing newData = new MtModProdLineManufacturing();
            newData.setTenantId(tenantId);
            newData.setProdLineId(dto.getProdLineId());
            newData.setCompletionLocatorId(dto.getCompletionLocatorId());
            newData.setInventoryLocatorId(dto.getInventoryLocatorId());
            newData.setIssuedLocatorId(dto.getIssuedLocatorId());
            newData.setDispatchMethod(dto.getDispatchMethod());
            self().insertSelective(newData);

            if (CollectionUtils.isNotEmpty(dto.getOperationId())) {
                MtModProdLineDispatchOper mtModProdLineDispatchOper = null;
                for (String operationId : dto.getOperationId()) {
                    mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
                    mtModProdLineDispatchOper.setTenantId(tenantId);
                    mtModProdLineDispatchOper.setProdLineId(dto.getProdLineId());
                    mtModProdLineDispatchOper.setOperationId(operationId);
                    mtModProdLineDispatchOperRepository.insertSelective(mtModProdLineDispatchOper);
                }
            }
        } else {
            if ("Y".equalsIgnoreCase(fullUpdate)) {
                BeanUtils.copyProperties(dto, oldData);
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKey(oldData);
            } else {
                // 更新
                if (null != dto.getInventoryLocatorId()) {
                    oldData.setInventoryLocatorId(dto.getInventoryLocatorId());
                }
                if (null != dto.getCompletionLocatorId()) {
                    oldData.setCompletionLocatorId(dto.getCompletionLocatorId());
                }
                if (null != dto.getIssuedLocatorId()) {
                    oldData.setIssuedLocatorId(dto.getIssuedLocatorId());
                }
                if (null != dto.getDispatchMethod()) {
                    oldData.setDispatchMethod(dto.getDispatchMethod());
                }
                oldData.setTenantId(tenantId);
                self().updateByPrimaryKeySelective(oldData);
            }

            if (CollectionUtils.isNotEmpty(dto.getOperationId())) {
                MtModProdLineDispatchOper mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
                mtModProdLineDispatchOper.setTenantId(tenantId);
                mtModProdLineDispatchOper.setProdLineId(dto.getProdLineId());
                this.mtModProdLineDispatchOperRepository.delete(mtModProdLineDispatchOper);

                for (String operationId : dto.getOperationId()) {
                    mtModProdLineDispatchOper = new MtModProdLineDispatchOper();
                    mtModProdLineDispatchOper.setTenantId(tenantId);
                    mtModProdLineDispatchOper.setProdLineId(dto.getProdLineId());
                    mtModProdLineDispatchOper.setOperationId(operationId);
                    mtModProdLineDispatchOperRepository.insertSelective(mtModProdLineDispatchOper);
                }
            }
        }
    }

}
