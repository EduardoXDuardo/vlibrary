package com.eduardoxduardo.vlibrary.dto.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VolumeInfo {

    private String title;
    private List<String> authors;
    private String description;
    private List<String> categories;
}