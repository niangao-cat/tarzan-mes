package com.ruike.hme.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Author: chuang.yang
 * @Date: 2020/10/22 20:21
 * @Description:
 */
public class HmeTagNcVO1 implements Serializable {
    private static final long serialVersionUID = 3518839662003510643L;

    private String tagId;
    private String tagGroupId;

    public HmeTagNcVO1() {}

    public HmeTagNcVO1(String tagId, String tagGroupId) {
        this.tagId = tagId;
        this.tagGroupId = tagGroupId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagGroupId() {
        return tagGroupId;
    }

    public void setTagGroupId(String tagGroupId) {
        this.tagGroupId = tagGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HmeTagNcVO1 that = (HmeTagNcVO1) o;
        return Objects.equals(tagId, that.tagId) && Objects.equals(tagGroupId, that.tagGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tagGroupId);
    }
}
