package com.ozay.backend.web.rest.form;

/**
 * Created by naofumiezaki on 11/24/15.
 */
public class NotificationRoleFormDTO{
    private Long roleId;
    private boolean checked;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "NotificationRoleFormDTO{" +
            "roleId='" + roleId + '\'' +
            "checked'" + checked + '\'' +
            "}";
    }
}
