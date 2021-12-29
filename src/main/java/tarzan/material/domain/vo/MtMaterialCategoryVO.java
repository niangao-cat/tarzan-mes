package tarzan.material.domain.vo;

import java.io.Serializable;

import javax.persistence.Transient;

import tarzan.material.domain.entity.MtMaterialCategory;

/**
 * @program: MtFrame
 * @description: 前台界面展示用视图
 * @author: Mr.Zxl
 * @create: 2018-11-29 14:29
 **/
public class MtMaterialCategoryVO extends MtMaterialCategory implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4467612287554516594L;

    @Transient
    private String categorySetCode;

    @Transient
    private String categorySetDesc;

    public String getCategorySetCode() {
        return categorySetCode;
    }

    public void setCategorySetCode(String categorySetCode) {
        this.categorySetCode = categorySetCode;
    }

    public String getCategorySetDesc() {
        return categorySetDesc;
    }

    public void setCategorySetDesc(String categorySetDesc) {
        this.categorySetDesc = categorySetDesc;
    }
}
