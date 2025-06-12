package org.example.currency_exchanger.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private Long id;
    private String code;
    private String name;
    private String sign;

}
