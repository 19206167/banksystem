package com.nus.team4.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryForm {
    String username;

    int pageNum;

    int pageSize;

    Date startTime;

    Date endTime;
}
