package com.ecg.webclient.feature.administration.viewmodell;

import java.util.List;

import javax.validation.Valid;

import org.springframework.util.AutoPopulatingList;

public class FeatureConfig
{
    @Valid
    private List<FeatureDto> features;

    public List<FeatureDto> getFeatures()
    {
        if (features == null)
        {
            features = new AutoPopulatingList<FeatureDto>(FeatureDto.class);
        }
        return features;
    }

    public void setFeatures(List<FeatureDto> features)
    {
        this.features = features;
    }
}
