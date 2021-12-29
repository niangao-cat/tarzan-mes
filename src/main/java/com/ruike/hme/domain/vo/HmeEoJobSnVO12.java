package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/21 15:11
 * @Description:
 */
public class HmeEoJobSnVO12 implements Serializable {
    private static final long serialVersionUID = 8963647693704590216L;

    private String tagGroupId;
    private String tagId;

    public HmeEoJobSnVO12() {

    }

    public HmeEoJobSnVO12(String tagGroupId, String tagId) {
        this.tagGroupId = tagGroupId;
        this.tagId = tagId;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeEoJobSnVO12 that = (HmeEoJobSnVO12) o;
        return Objects.equals(tagGroupId, that.tagGroupId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagGroupId, tagId);
    }
}
