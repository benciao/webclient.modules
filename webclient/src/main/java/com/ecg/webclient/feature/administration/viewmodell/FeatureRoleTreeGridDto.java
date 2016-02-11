package com.ecg.webclient.feature.administration.viewmodell;

public class FeatureRoleTreeGridDto
{
    private String  featureName;
    private long    rowId;
    private long    parentRowId;
    private RoleDto role;

    public FeatureRoleTreeGridDto()
    {

    }

    public String getFeatureName()
    {
        return featureName;
    }

    public long getParentRowId()
    {
        return parentRowId;
    }

    public RoleDto getRole()
    {
        return role;
    }

    public long getRowId()
    {
        return rowId;
    }

    public void setFeatureName(String featureName)
    {
        this.featureName = featureName;
    }

    public void setParentRowId(long parentRowId)
    {
        this.parentRowId = parentRowId;
    }

    public void setRole(RoleDto role)
    {
        this.role = role;
    }

    public void setRowId(long rowId)
    {
        this.rowId = rowId;
    }
}
