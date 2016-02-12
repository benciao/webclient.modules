package com.ecg.webclient.feature.administration.viewmodell;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class GroupConfig
{
    @Valid
    private List<GroupDto>               groups;
    private List<FeatureRoleTreeGridDto> featureRoleTreeGrid;
    @Valid
    private GroupDto                     copyGroup;
    private long                         clientId;
    private boolean                      doCopy = false;
    private Long                         groupToCopyId;

    public long getClientId()
    {
        return clientId;
    }

    public GroupDto getCopyGroup()
    {
        return copyGroup;
    }

    public List<FeatureRoleTreeGridDto> getFeatureRoleTreeGrid()
    {
        if (featureRoleTreeGrid == null)
        {
            featureRoleTreeGrid = new ArrayList<FeatureRoleTreeGridDto>();
        }
        return featureRoleTreeGrid;
    }

    public List<GroupDto> getGroups()
    {
        if (groups == null)
        {
            groups = new AutoPopulatingList<GroupDto>(GroupDto.class);
        }
        return groups;
    }

    public Long getGroupToCopyId()
    {
        return groupToCopyId;
    }

    public boolean isDoCopy()
    {
        return doCopy;
    }

    public void removeDeleted()
    {
        List<GroupDto> groupsToRemove = new ArrayList<GroupDto>();
        for (GroupDto group : groups)
        {
            if (group.isDelete())
            {
                groupsToRemove.add(group);
            }
        }
        groups.removeAll(groupsToRemove);
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public void setCopyGroup(GroupDto group)
    {
        this.copyGroup = group;
    }

    public void setDoCopy(boolean doCopy)
    {
        this.doCopy = doCopy;
    }

    public void setFeatureRoleTreeGrid(List<FeatureRoleTreeGridDto> featureRoleTreeGrid)
    {
        this.featureRoleTreeGrid = featureRoleTreeGrid;
    }

    public void setGroups(List<GroupDto> groups)
    {
        this.groups = groups;
    }

    public void setGroupToCopyId(Long groupToCopyId)
    {
        this.groupToCopyId = groupToCopyId;
    }
}
