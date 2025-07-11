package org.example.currency_exchanger.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {

    private Long id;
    private String code;
    private String name;
    private String sign;

}
