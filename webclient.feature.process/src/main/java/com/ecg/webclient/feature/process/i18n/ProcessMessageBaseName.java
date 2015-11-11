package com.ecg.webclient.feature.process.i18n;

import org.springframework.stereotype.Component;

import com.ecg.webclient.feature.i18n.WebClientResourceLocation;

@Component
public class ProcessMessageBaseName extends WebClientResourceLocation
{
    public ProcessMessageBaseName()
    {
        super("classpath:/i18n/process_messages");
    }
}
