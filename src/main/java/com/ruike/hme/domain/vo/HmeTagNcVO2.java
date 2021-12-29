package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/22 20:23
 * @Description:
 */
public class HmeTagNcVO2 implements Serializable {
    private static final long serialVersionUID = -4289306568083526161L;

    private String tagGroupId;
    private String tagType;

    public HmeTagNcVO2() {}

    public HmeTagNcVO2(String tagGroupId, String tagType) {
        this.tagGroupId = tagGroupId;
        this.tagType = tagType;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeTagNcVO2 that = (HmeTagNcVO2) o;
        return Objects.equals(tagGroupId, that.tagGroupId) && Objects.equals(tagType, that.tagType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagGroupId, tagType);
    }
}
