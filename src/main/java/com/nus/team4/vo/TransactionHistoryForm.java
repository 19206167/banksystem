package com.nus.team4.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryForm {
    String username;

    int pageNum;

    int pageSize;
}
