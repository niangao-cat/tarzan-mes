package com.ruike.itf.api.dto;

import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.repository.HmeFreezeDocRepository;
import com.ruike.itf.domain.entity.ItfFreezeDocIface;
import com.ruike.itf.domain.repository.ItfFreezeDocIfaceRepository;
import com.ruike.wms.infra.util.StringCommonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.NO;
import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static com.ruike.hme.infra.constant.HmeConstants.FreezeApprovalType.APPROVALING;

/**
 * <p>
 * 冻结单接口返回 请求字段
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/3 10:32
 */
@Data
public class FreezeDocRequestDTO implements Serializable {
    private static final long serialVersionUID = -228261819542968190L;

    @ApiModelProperty(value = "冻结单号")
    private String freezeDocNum;
    @ApiModelProperty(value = "审批版本")
    private Long sequence;
    @ApiModelProperty(value = "OA返回审批状态")
    private String status;

    public ValidResult validation(HmeFreezeDocRepository docRepository, ItfFreezeDocIfaceRepository ifaceRepository) {
        ValidResult result = new ValidResult();
        String message = "";
        message = message.concat(StringUtils.isBlank(this.getFreezeDocNum()) ? "冻结单号不允许为空" : "");
        message = message.concat(Objects.isNull(this.getSequence()) ? "发起序号不允许为空" : "");
        message = message.concat(StringUtils.isBlank(this.getStatus()) ? "返回状态不允许为空" : "");
        if (message.length() != 0) {
            return result.error(message);
        }
        HmeFreezeDoc docCondition = new HmeFreezeDoc();
        docCondition.setFreezeDocNum(this.getFreezeDocNum());
        HmeFreezeDoc doc = docRepository.selectOne(docCondition);
        if (Objects.isNull(doc)) {
            return result.error("冻结单号无效");
        }
        if (!APPROVALING.equals(doc.getApprovalStatus())) {
            return result.error("冻结单状态不为审批中");
        }
        ItfFreezeDocIface iface = ifaceRepository.selectOne(new ItfFreezeDocIface(doc.getFreezeDocId(), this.getSequence()));
        if (Objects.isNull(iface)) {
            return result.error("冻结单号与发起序号在接口表中不存在");
        }
        if (StringUtils.isNotBlank(iface.getStatus())) {
            return result.error("不允许重复更新审批状态");
        }
        if (!StringCommonUtils.contains(this.getStatus(), YES, NO)) {
            return result.error("审批状态必须为Y或N");
        }
        // 验证成功
        result.setDoc(doc);
        result.setIface(iface);
        return result;
    }

    @Data
    public static class ValidResult {
        @ApiModelProperty(value = "冻结单")
        private HmeFreezeDoc doc;
        @ApiModelProperty(value = "冻结接口数据")
        private ItfFreezeDocIface iface;
        @ApiModelProperty(value = "错误消息")
        private String message;

        public ValidResult error(String message) {
            this.setMessage(message);
            return this;
        }
    }
}
