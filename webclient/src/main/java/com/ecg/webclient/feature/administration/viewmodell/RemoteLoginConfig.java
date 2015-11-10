package com.ecg.webclient.feature.administration.viewmodell;

import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class RemoteLoginConfig
{
	@Valid
    private List<RemoteLoginDto> remoteLogins;

	public List<RemoteLoginDto> getRemoteLogins()
	{
		if (remoteLogins == null)
		{
			remoteLogins = new AutoPopulatingList<RemoteLoginDto>(RemoteLoginDto.class);
		}
		return remoteLogins;
	}

	public void setRemoteLogins(List<RemoteLoginDto> remoteLogins)
	{
		this.remoteLogins = remoteLogins;
	}
}
