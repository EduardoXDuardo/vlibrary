package com.eduardoxduardo.vlibrary.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchCriteria {
    private String username;
    private String email;
}
