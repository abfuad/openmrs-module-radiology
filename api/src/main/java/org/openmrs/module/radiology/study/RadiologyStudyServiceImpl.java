/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.radiology.study;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.radiology.RadiologyProperties;
import org.openmrs.module.radiology.dicom.DicomUidGenerator;
import org.openmrs.module.radiology.dicom.code.PerformedProcedureStepStatus;
import org.openmrs.module.radiology.dicom.code.ScheduledProcedureStepStatus;
import org.openmrs.module.radiology.order.RadiologyOrder;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
class RadiologyStudyServiceImpl extends BaseOpenmrsService implements RadiologyStudyService {
    
    
    private static final Log log = LogFactory.getLog(RadiologyStudyServiceImpl.class);
    
    private RadiologyStudyDAO radiologyStudyDAO;
    
    private RadiologyProperties radiologyProperties;
    
    private DicomUidGenerator dicomUidGenerator;
    
    public void setRadiologyStudyDAO(RadiologyStudyDAO radiologyStudyDAO) {
        this.radiologyStudyDAO = radiologyStudyDAO;
    }
    
    public void setRadiologyProperties(RadiologyProperties radiologyProperties) {
        this.radiologyProperties = radiologyProperties;
    }
    
    public void setDicomUidGenerator(DicomUidGenerator dicomUidGenerator) {
        this.dicomUidGenerator = dicomUidGenerator;
    }
    
    /**
     * @see RadiologyStudyService#saveRadiologyStudy(RadiologyStudy)
     */
    @Override
    @Transactional
    public RadiologyStudy saveRadiologyStudy(RadiologyStudy radiologyStudy) {
        
        if (radiologyStudy == null) {
            throw new IllegalArgumentException("radiologyStudy cannot be null");
        }
        
        final RadiologyOrder order = radiologyStudy.getRadiologyOrder();
        
        if (radiologyStudy.getScheduledStatus() == null && order.getScheduledDate() != null) {
            radiologyStudy.setScheduledStatus(ScheduledProcedureStepStatus.SCHEDULED);
        }
        
        setStudyInstanceUidIfBlank(radiologyStudy);
        
        return radiologyStudyDAO.saveRadiologyStudy(radiologyStudy);
    }
    
    /**
     * Sets {@code studyInstanceUid} of given {@code radiologyStudy} if blank.
     * 
     * @param radiologyStudy RadiologyStudy of which studyInstanceUid shall be set
     * @throws IllegalArgumentException if global property DICOM UID org root cannot be found
     * @throws IllegalArgumentException if global property DICOM UID org root is empty
     * @throws IllegalArgumentException if global property DICOM UID org root is not a valid UID
     * @throws IllegalArgumentException if global property DICOM UID org root exceeds the maximum length
     * @should set the study instance uid of given radiology study to a valid dicom uid if null
     * @should set the study instance uid of given radiology study to a valid dicom uid if only containing whitespaces
     * @should not set the study instance uid of given radiology study if contains non whitespace characters
     */
    private void setStudyInstanceUidIfBlank(RadiologyStudy radiologyStudy) {
        
        if (StringUtils.isBlank(radiologyStudy.getStudyInstanceUid())) {
            final String uuid = dicomUidGenerator.getNewDicomUid(radiologyProperties.getDicomUIDOrgRoot());
            radiologyStudy.setStudyInstanceUid(uuid);
        }
    }
    
    /**
     * @see RadiologyStudyService#updateStudyPerformedStatus(String, PerformedProcedureStepStatus)
     */
    @Override
    @Transactional
    public RadiologyStudy updateStudyPerformedStatus(String studyInstanceUid, PerformedProcedureStepStatus performedStatus) {
        
        if (studyInstanceUid == null) {
            throw new IllegalArgumentException("studyInstanceUid cannot be null");
        }
        
        if (performedStatus == null) {
            throw new IllegalArgumentException("performedStatus cannot be null");
        }
        
        final RadiologyStudy studyToBeUpdated = radiologyStudyDAO.getRadiologyStudyByStudyInstanceUid(studyInstanceUid);
        studyToBeUpdated.setPerformedStatus(performedStatus);
        return radiologyStudyDAO.saveRadiologyStudy(studyToBeUpdated);
    }
    
    /**
     * @see RadiologyStudyService#getRadiologyStudy(Integer)
     */
    @Override
    public RadiologyStudy getRadiologyStudy(Integer studyId) {
        
        if (studyId == null) {
            throw new IllegalArgumentException("studyId cannot be null");
        }
        
        return radiologyStudyDAO.getRadiologyStudy(studyId);
    }
    
    /**
     * @see RadiologyStudyService#getRadiologyStudyByUuid(String)
     */
    @Override
    public RadiologyStudy getRadiologyStudyByUuid(String uuid) {
        
        if (uuid == null) {
            throw new IllegalArgumentException("uuid cannot be null");
        }
        
        return radiologyStudyDAO.getRadiologyStudyByUuid(uuid);
    }
    
    /**
     * @see RadiologyStudyService#getRadiologyStudyByOrderId(Integer)
     */
    @Override
    public RadiologyStudy getRadiologyStudyByOrderId(Integer orderId) {
        
        if (orderId == null) {
            throw new IllegalArgumentException("orderId cannot be null");
        }
        
        return radiologyStudyDAO.getRadiologyStudyByOrderId(orderId);
    }
    
    /**
     * @see RadiologyStudyService#getRadiologyStudyByStudyInstanceUid(String)
     */
    public RadiologyStudy getRadiologyStudyByStudyInstanceUid(String studyInstanceUid) {
        
        if (studyInstanceUid == null) {
            throw new IllegalArgumentException("studyInstanceUid cannot be null");
        }
        return radiologyStudyDAO.getRadiologyStudyByStudyInstanceUid(studyInstanceUid);
    }
    
    /**
     * @see RadiologyStudyService#getStudiesByRadiologyOrders
     */
    @Override
    public List<RadiologyStudy> getRadiologyStudiesByRadiologyOrders(List<RadiologyOrder> radiologyOrders) {
        
        if (radiologyOrders == null) {
            throw new IllegalArgumentException("radiologyOrders cannot be null");
        }
        
        final List<RadiologyStudy> result = radiologyStudyDAO.getRadiologyStudiesByRadiologyOrders(radiologyOrders);
        return result;
    }
}
