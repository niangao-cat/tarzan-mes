package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Leeloing
 * @date 2019-09-29 10:11
 */
public class MtExtendVO8 implements Serializable {
    private static final long serialVersionUID = -2257480586498322917L;
    @ApiModelProperty(value = "扩展表名")
    private String tableName;
    @ApiModelProperty(value = "扩展字段属性列表")
    private List<MtExtendAttrVO> attrs =new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<MtExtendAttrVO> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendAttrVO> attrs) {
        this.attrs = attrs;
    }
}
