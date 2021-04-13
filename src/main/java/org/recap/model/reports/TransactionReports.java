package org.recap.model.reports;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dinakar on 11/04/21
 */
@Setter
@Getter
public class TransactionReports {
   private String totalRecordsCount = "0";
   private Integer pageNumber = 0;
   private Integer pageSize = 10;
   private Integer totalPageCount = 0;
   private String message;
   private List<TransactionReport> transactionReportList = new ArrayList<>();
   String owningInsts;
   String requestingInsts;
   String typeOfUses;
   String fromDate;
   String toDate;
   String trasactionCallType;
   String cgdType;

}
