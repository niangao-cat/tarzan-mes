package io.tarzan.common.domain.sys;

import java.io.Serializable;

/**
 * @author : MrZ
 * @date : 2019-12-31 16:40
 **/
public class MtUserInfo implements Serializable {
    private static final long serialVersionUID = 1921542553577372468L;
    private Long id;
    private String loginName;
    private String realName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
