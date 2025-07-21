package com.eduardoxduardo.vlibrary.dto.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookItem {

    private String id;
    private VolumeInfo volumeInfo;

}