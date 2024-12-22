// BorrowerDTO.java
package com.aeon.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerDto {
    private Long id;
    private String email;
    private String name;
}