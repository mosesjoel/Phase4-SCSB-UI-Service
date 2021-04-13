package org.recap.util;

import org.apache.commons.lang3.StringUtils;
import org.recap.RecapConstants;
import org.recap.model.jpa.CollectionGroupEntity;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.RequestItemEntity;
import org.recap.model.reports.TransactionReport;
import org.recap.model.search.RequestForm;
import org.recap.repository.jpa.CollectionGroupDetailsRepository;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.repository.jpa.RequestItemDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rajeshbabuk on 29/10/16.
 */
@Service
public class RequestServiceUtil {

    @Autowired
    private RequestItemDetailsRepository requestItemDetailsRepository;

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    @Autowired
    private CollectionGroupDetailsRepository collectionGroupDetailsRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    /**
     * Based on the given search criteria in the request search UI page, this method builds the request search results to show them as rows in the request search UI page.
     *
     * @param requestForm the request form
     * @return the page
     */
    public Page<RequestItemEntity> searchRequests(RequestForm requestForm) {
        String patronBarcode = StringUtils.isNotBlank(requestForm.getPatronBarcode()) ? requestForm.getPatronBarcode().trim() : requestForm.getPatronBarcode();
        String itemBarcode = StringUtils.isNotBlank(requestForm.getItemBarcode()) ? requestForm.getItemBarcode().trim() : requestForm.getItemBarcode();
        String status = StringUtils.isNotBlank(requestForm.getStatus()) ? requestForm.getStatus().trim() : requestForm.getStatus();
        String institution = StringUtils.isNotBlank(requestForm.getInstitution()) ? requestForm.getInstitution().trim() : requestForm.getInstitution();
        InstitutionEntity institutionEntity = institutionDetailsRepository.findByInstitutionCode(institution);
        Optional<InstitutionEntity> institutionEntityOptional = Optional.ofNullable(institutionEntity);
        if (!institutionEntityOptional.isPresent()) {
            institutionEntity = new InstitutionEntity();
            institutionEntity.setId(0);
        }
        Pageable pageable = PageRequest.of(requestForm.getPageNumber(), requestForm.getPageSize(), Sort.Direction.DESC, "id");

        Page<RequestItemEntity> requestItemEntities = null;
        if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isNotBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndActiveAndInstitution(pageable, patronBarcode, itemBarcode, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndStatusAndInstitution(pageable, patronBarcode, itemBarcode, status, institutionEntity.getId());
            }
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndActive(pageable, patronBarcode, itemBarcode, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndStatus(pageable, patronBarcode, itemBarcode, status, institutionEntity.getId());
            }
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcode(pageable, patronBarcode, itemBarcode, institutionEntity.getId());
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isNotBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndItemBarcodeAndInstitution(pageable, patronBarcode, itemBarcode, institutionEntity.getId());
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndActive(pageable, patronBarcode, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndStatus(pageable, patronBarcode, status, institutionEntity.getId());
            }
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isNotBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndActiveAndInstitution(pageable, patronBarcode, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndStatusAndInstitution(pageable, patronBarcode, status, institutionEntity.getId());
            }
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndActive(pageable, itemBarcode, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndStatus(pageable, itemBarcode, status, institutionEntity.getId());
            }
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isNotBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndActiveAndInstitution(pageable, itemBarcode, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndStatusAndInstitution(pageable, itemBarcode, status, institutionEntity.getId());
            }
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findByPatronBarcode(pageable, patronBarcode);
        } else if (StringUtils.isNotBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isNotBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findByPatronBarcodeAndInstitution(pageable, patronBarcode, institutionEntity.getId());
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findByItemBarcode(pageable, itemBarcode, institutionEntity.getId());
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isNotBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isNotBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findByItemBarcodeAndInstitution(pageable, itemBarcode, institutionEntity.getId());
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findAllActive(pageable);
            } else {
                requestItemEntities = requestItemDetailsRepository.findByStatus(pageable, status);
            }
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isNotBlank(status) && StringUtils.isNotBlank(institution)) {
            if (status.equals(RecapConstants.SEARCH_REQUEST_ACTIVE)) {
                requestItemEntities = requestItemDetailsRepository.findAllActiveAndInstitution(pageable, institutionEntity.getId());
            } else {
                requestItemEntities = requestItemDetailsRepository.findByStatusAndInstitution(pageable, status, institutionEntity.getId());
            }
        } else if (StringUtils.isBlank(patronBarcode) && StringUtils.isBlank(itemBarcode) && StringUtils.isBlank(status) && StringUtils.isNotBlank(institution)) {
            requestItemEntities = requestItemDetailsRepository.findAllActiveAndInstitution(pageable, institutionEntity.getId());
        } else {
            requestItemEntities = requestItemDetailsRepository.findAllActive(pageable);
        }
        return requestItemEntities;
    }

    /**
     *
     * @param institution
     * @param fromDate
     * @param toDate
     * @return page requestItemEntities
     */
    public List<RequestItemEntity> exportExceptionReports(String institution, Date fromDate, Date toDate) {
        InstitutionEntity institutionEntity = institutionDetailsRepository.findByInstitutionCode(institution);
        List<RequestItemEntity> requestItemEntities = requestItemDetailsRepository.findByStatusAndInstitutionAndAll(RecapConstants.REPORTS_EXCEPTION, institutionEntity.getId(),fromDate,toDate);
        return requestItemEntities;
    }

    /**
     *
     * @param institution
     * @param fromDate
     * @param toDate
     * @return page requestItemEntities
     */
    public Page<RequestItemEntity> exportExceptionReportsWithDate(String institution, Date fromDate, Date toDate,Integer pageNumber,Integer size) throws ParseException {
        Pageable pageable = PageRequest.of( pageNumber, size, Sort.Direction.DESC, RecapConstants.ID);
        InstitutionEntity institutionEntity = institutionDetailsRepository.findByInstitutionCode(institution);
        Page<RequestItemEntity> requestItemEntities = requestItemDetailsRepository.findByStatusAndInstitutionAndDateRange(pageable,RecapConstants.REPORTS_EXCEPTION, institutionEntity.getId(),fromDate,toDate);
        return requestItemEntities;
    }
    public List<TransactionReport> getTransactionReportCount(String owningInsts,String requestingInsts,String typeOfUses,Date fromDate,Date toDate) {
        List<TransactionReport> transactionReportsList = new ArrayList<>();
        Map<Integer,String> institutionList = mappingInstitution();
        List<Object[]> list = requestItemDetailsRepository.pullTransactionReportCount(convertToListFromString(owningInsts),convertToListFromString(requestingInsts),convertToListFromString(typeOfUses),fromDate,toDate);
        for (Object[] o: list) {
            transactionReportsList.add(new TransactionReport(o[0].toString(),institutionList.get(Integer.parseInt(o[1].toString())),institutionList.get(Integer.parseInt(o[2].toString())),o[3].toString(),Long.parseLong(o[4].toString())));
        }
        return transactionReportsList;
    }

    public List<TransactionReport> getTransactionReports(String owningInsts , String requestingInsts,String typeOfUses,Date fromDate,Date toDate, String cgdType) {
        List<TransactionReport> transactionReportsList = new ArrayList<>();
        Map<Integer,String> institutionList = mappingInstitution();
        List<String> cgdList = new ArrayList<>();
        if(cgdType.isEmpty())
            cgdList = pullCGDList();
        else
            cgdList.add(cgdType);
        List<Object[]> reportsList = requestItemDetailsRepository.findByOwnAndReqInstWithStatus(convertToListFromString(owningInsts),convertToListFromString(requestingInsts),convertToListFromString(typeOfUses),fromDate,toDate,cgdList);
        for (Object[] o: reportsList) {
            transactionReportsList.add(new TransactionReport(o[0].toString(),institutionList.get(Integer.parseInt(o[1].toString())),institutionList.get(Integer.parseInt(o[2].toString())),o[3].toString(),o[4].toString(),o[5].toString(),o[6].toString()));
        }
        return transactionReportsList;
    }

    private Map<Integer,String> mappingInstitution(){
        Map<Integer,String> institutionList = new HashMap<>();
        List<InstitutionEntity> institutionEntities = institutionDetailsRepository.getInstitutionCodes();
        institutionEntities.stream().forEach(inst->institutionList.put(inst.getId(),inst.getInstitutionCode()));
        return institutionList;
    }

    private List<String> pullCGDList(){
        List<String> cgdList = new ArrayList<>();
        List<CollectionGroupEntity> collectionGroupEntities = collectionGroupDetailsRepository.findAll();
        collectionGroupEntities.stream().forEach(cgd->cgdList.add(cgd.getCollectionGroupCode()));
        return cgdList;
    }

    private List<String> convertToListFromString(String stringContent){
        return Stream.of(stringContent.split(","))
                .collect(Collectors.toList());
    }
}
