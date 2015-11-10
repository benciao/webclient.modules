package com.ecg.webclient.feature.administration.viewmodell;

/**
 * Implementierung eines von der Persistenz detachten RemoteLogins.
 * 
 * @author arndtmar
 */
public class RemoteLoginDto extends BaseObjectDto
{
	private String	userId;
	private String  remoteSystemId;
	private String	remoteSystemName;
	private String	remoteSystemDescription;
	private boolean	enabled;
	private String	remoteSystemLoginName;
	private String	remoteSystemPassword;

	public RemoteLoginDto()
	{
	}

	public RemoteLoginDto copy()
	{
	    RemoteLoginDto copy = new RemoteLoginDto();
	    copy.setDelete(this.isDelete());
	    copy.setEnabled(this.isEnabled());
	    copy.setId(this.getId());
	    copy.setRemoteSystemDescription(this.getRemoteSystemDescription());
	    copy.setRemoteSystemId(this.getRemoteSystemId());
	    copy.setRemoteSystemLoginName(this.getRemoteSystemLoginName());
	    copy.setRemoteSystemName(this.getRemoteSystemName());
	    copy.setRemoteSystemPassword(this.getRemoteSystemPassword());
	    copy.setUserId(this.getUserId());
	    
	    return copy;
	}

	public String getRemoteSystemDescription()
	{
		return remoteSystemDescription;
	}

	public String getRemoteSystemId()
	{
		return remoteSystemId;
	}

	public String getRemoteSystemLoginName()
	{
		return remoteSystemLoginName;
	}

	public String getRemoteSystemName()
	{
		return remoteSystemName;
	}

	public String getRemoteSystemPassword()
	{
		return remoteSystemPassword;
	}

	public String getUserId()
	{
		return userId;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	public void setRemoteSystemDescription(String remoteSystemDescription)
	{
		this.remoteSystemDescription = remoteSystemDescription;
	}

	public void setRemoteSystemId(String remoteSystemId)
	{
		this.remoteSystemId = remoteSystemId;
	}

	public void setRemoteSystemLoginName(String remoteSystemLoginName)
	{
		this.remoteSystemLoginName = remoteSystemLoginName;
	}

	public void setRemoteSystemName(String remoteSystemName)
	{
		this.remoteSystemName = remoteSystemName;
	}

	public void setRemoteSystemPassword(String remoteSystemPassword)
	{
		this.remoteSystemPassword = remoteSystemPassword;
	}

    public void setUserId(String userId)
	{
		this.userId = userId;
	}
}
