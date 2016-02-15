package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

public class RoleConfig
{
    @Valid
    private List<FeatureRoleTreeGridDto> featureRoleTreeGrid;

    public List<FeatureRoleTreeGridDto> getFeatureRoleTreeGrid()
    {
        if (featureRoleTreeGrid == null)
        {
            featureRoleTreeGrid = new ArrayList<FeatureRoleTreeGridDto>();
        }
        return featureRoleTreeGrid;
    }

    public void removeDeleted()
    {
        List<FeatureRoleTreeGridDto> rowsToRemove = new ArrayList<FeatureRoleTreeGridDto>();
        for (FeatureRoleTreeGridDto row : featureRoleTreeGrid)
        {
            if (row.getRole() != null)
            {
                if (row.getRole().isDelete())
                {
                    rowsToRemove.add(row);
                }
            }
        }
        featureRoleTreeGrid.removeAll(rowsToRemove);
    }

    public void setFeatureRoleTreeGrid(List<FeatureRoleTreeGridDto> featureRoleTreeGrid)
    {
        this.featureRoleTreeGrid = featureRoleTreeGrid;
    }
}
