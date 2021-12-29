package io.tarzan.common.domain.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leeloing
 * @date 2019/7/22 16:30
 */
public class MtExtendVO1 implements Serializable {

    private static final long serialVersionUID = -7793932653361696623L;
    private String tableName;
    private List<String> keyIdList;
    private List<MtExtendVO5> attrs;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getKeyIdList() {
        return keyIdList;
    }

    public void setKeyIdList(List<String> keyIdList) {
        this.keyIdList = keyIdList;
    }

    public List<MtExtendVO5> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<MtExtendVO5> attrs) {
        this.attrs = attrs;
    }

    public MtExtendVO1() {
    }

    public MtExtendVO1(String tableName, List<String> keyIdList, String attr, String... attrs) {
        this.tableName = tableName;
        this.keyIdList = keyIdList;
        this.attrs = Collections.singletonList(new MtExtendVO5(attr));
        List<MtExtendVO5> attrList = new ArrayList<>();
        attrList.add(new MtExtendVO5(attr));
        if (attrs.length > 0) {
            attrList.addAll(Arrays.stream(attrs).map(MtExtendVO5::new).collect(Collectors.toList()));
        }
        this.attrs = attrList;
    }
}
