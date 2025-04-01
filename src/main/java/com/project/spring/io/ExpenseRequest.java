package com.project.spring.io;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseRequest {
    private String name;
    private String note;
    private String category;
    private Date date;
    private BigDecimal amount;
}
