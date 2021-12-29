package com.ruike.hme.app.upload.validator;

import com.ruike.hme.api.dto.HmeMesTransformDTO;
import com.ruike.hme.domain.repository.HmeWorkOrderManagementRepository;
import com.ruike.hme.infra.util.JsonUtils;
import com.ruike.wms.domain.repository.WmsMaterialLotRepository;
import com.ruike.wms.domain.vo.WmsMaterialLotAttrVO;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.domain.entity.ImportData;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtBomComponent;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.order.domain.entity.MtWorkOrder;
import tarzan.order.domain.repository.MtWorkOrderRepository;

import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.OK;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.StatusCode.CANRELEASE;
import static com.ruike.hme.infra.constant.HmeConstants.StatusCode.EORELEASED;
import static com.ruike.hme.infra.constant.HmeConstants.WorkOrderStatus.RELEASED;

/**
 * <p>
 * MES转编码导入验证
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/4 10:03
 */
@ImportValidators({
        @ImportValidator(templateCode = "HME.MES_TRANSFORM")
})
public class HmeMesTransferImportValidator extends ValidatorHandler {

    private final MtModSiteRepository siteRepository;
    private final MtWorkOrderRepository workOrderRepository;
    private final WmsMaterialLotRepository wmsMaterialLotRepository;
    private final MtModLocatorRepository locatorRepository;
    private final MtMaterialRepository materialRepository;
    private final MtContainerRepository containerRepository;
    private final HmeWorkOrderManagementRepository hmeWorkOrderManagementRepository;

    public HmeMesTransferImportValidator(MtModSiteRepository siteRepository, MtWorkOrderRepository workOrderRepository, WmsMaterialLotRepository wmsMaterialLotRepository, MtModLocatorRepository locatorRepository, MtMaterialRepository materialRepository, MtContainerRepository containerRepository, HmeWorkOrderManagementRepository hmeWorkOrderManagementRepository) {
        this.siteRepository = siteRepository;
        this.workOrderRepository = workOrderRepository;
        this.wmsMaterialLotRepository = wmsMaterialLotRepository;
        this.locatorRepository = locatorRepository;
        this.materialRepository = materialRepository;
        this.containerRepository = containerRepository;
        this.hmeWorkOrderManagementRepository = hmeWorkOrderManagementRepository;
    }

    @Override
    public boolean validate(String data) {
        CustomUserDetails details = DetailsHelper.getUserDetails();
        // 获取上下文，验证消息
        ImportData context = this.getContext();
        HmeMesTransformDTO transform;
        transform = JsonUtils.jsonToObject(data, HmeMesTransformDTO.class);
        if (Objects.isNull(transform)) {
            context.addErrorMsg("这条数据无法读取");
            return false;
        }

        // 执行验证逻辑
        this.processValidation(details, context, transform);

        // 有错误消息验证失败
        if (StringUtils.isNotBlank(context.getErrorMsg())) {
            return false;
        }

        return true;
    }

    private void processValidation(CustomUserDetails details, ImportData context, HmeMesTransformDTO transform) {
        // 验证站点
        List<MtModSite> siteList = siteRepository.selectByCondition(Condition.builder(MtModSite.class).andWhere(
                Sqls.custom().andEqualTo(MtModSite.FIELD_SITE_CODE, transform.getSiteCode())
                        .andEqualTo(MtModSite.FIELD_ENABLE_FLAG, YES)
        ).build());
        this.addMessage(CollectionUtils.isEmpty(siteList), "导入站点不存在或已失效", context);
        MtModSite site = CollectionUtils.isNotEmpty(siteList) ? siteList.get(0) : new MtModSite();

        // 验证工单
        MtWorkOrder workOrder = workOrderRepository.selectOne(new MtWorkOrder() {{
            setWorkOrderNum(transform.getWorkOrderNum());
        }});
        this.addMessage(Objects.isNull(workOrder), "工单不存在", context);
        if (Objects.nonNull(workOrder)) {
            this.addMessage(!StringCommonUtils.contains(workOrder.getStatus(), RELEASED, EORELEASED), "工单状态不为下达或可下达状态", context);
            this.addMessage(!StringCommonUtils.equalsIgnoreBlank(workOrder.getSiteId(), site.getSiteId()), "工单站点与导入站点不匹配", context);
        }

        // 验证条码
        WmsMaterialLotAttrVO materialLot = wmsMaterialLotRepository.selectWithAttrByCode(details.getTenantId(), transform.getMaterialLotCode());
        this.addMessage(Objects.isNull(materialLot), "条码不存在", context);
        if (Objects.nonNull(materialLot)) {
            // 条码属性
            this.addMessage(!YES.equals(materialLot.getEnableFlag()), "条码已失效", context);
            this.addMessage(YES.equals(materialLot.getMfFlag()), "条码为在制品", context);
            this.addMessage(YES.equals(materialLot.getFreezeFlag()), "条码已冻结", context);
            this.addMessage(!OK.equals(materialLot.getQualityStatus()), "条码质量不合格", context);
            this.addMessage(YES.equals(materialLot.getStocktakeFlag()), "条码正在盘点中", context);
            this.addMessage(!StringCommonUtils.equalsIgnoreBlank(materialLot.getPlantId(), site.getSiteId()), "工单站点与导入站点不匹配", context);
            // 验证线边仓
            this.addMessage(StringUtils.isBlank(materialLot.getWarehouseId()), "条码货位不存在上层仓库", context);
            if (StringUtils.isNotBlank(materialLot.getWarehouseId())) {
                MtModLocator warehouse = locatorRepository.selectByPrimaryKey(materialLot.getWarehouseId());
                this.addMessage(!"14".equals(warehouse.getLocatorType()), "条码所属仓库不在线边仓", context);
            }
            if(Objects.nonNull(workOrder)){
                List<MtBomComponent> bomList = hmeWorkOrderManagementRepository.bomMaterialListGet(details.getTenantId(), workOrder.getWorkOrderId(), materialLot.getMaterialId());
                this.addMessage(bomList.stream().filter(rec -> rec.getQty() > 0).count() != 1L, "改造工单中投料组件不唯一", context);
            }
        }

        // 验证转型物料
        MtMaterial material = materialRepository.selectOne(new MtMaterial() {{
            setMaterialCode(transform.getTransformMaterial());
            setEnableFlag(YES);
        }});
        this.addMessage(Objects.isNull(material), "转型物料不存在或无效", context);
        if (Objects.nonNull(material) && Objects.nonNull(workOrder) && !workOrder.getMaterialId().equals(material.getMaterialId())) {
            List<MtBomComponent> bomList = hmeWorkOrderManagementRepository.bomMaterialListGet(details.getTenantId(), workOrder.getWorkOrderId(), material.getMaterialId());
            this.addMessage(bomList.stream().filter(rec -> rec.getQty() < 0).count() != 1L, "转型物料不为工单物料，组件也不存在唯一联产品关系", context);
        }

        // 验证容器
        if (StringUtils.isNotBlank(transform.getContainerCode())) {
            this.addMessage(StringUtils.isNotBlank(materialLot.getCurrentContainerId()), "条码已存在上层容器", context);
            MtContainer container = containerRepository.selectOne(new MtContainer() {{
                setContainerCode(transform.getContainerCode());
            }});
            this.addMessage(Objects.isNull(container), "容器不存在", context);
            if (Objects.nonNull(container)) {
                this.addMessage(!StringCommonUtils.contains(container.getStatus(), CANRELEASE), "容器状态不为可下达", context);
                this.addMessage(!StringCommonUtils.equalsIgnoreBlank(container.getSiteId(), site.getSiteId()), "容器站点与导入站点不匹配", context);
                this.addMessage(!StringCommonUtils.equalsIgnoreBlank(container.getLocatorId(), materialLot.getLocatorId()), "条码货位与容器货位不一致", context);
            }
        }
    }

    private void addMessage(boolean invalidFlag, String message, ImportData context) {
        if (invalidFlag) {
            context.addErrorMsg(message);
        }
    }
}
