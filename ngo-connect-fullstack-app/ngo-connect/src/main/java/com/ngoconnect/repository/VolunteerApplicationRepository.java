package com.ngoconnect.repository;

import com.ngoconnect.model.VolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerApplicationRepository extends JpaRepository<VolunteerApplication, Long> {
    
    List<VolunteerApplication> findByVolunteerId(Long volunteerId);
    
    List<VolunteerApplication> findByEventId(Long eventId);
    
    List<VolunteerApplication> findByStatus(VolunteerApplication.ApplicationStatus status);
    
    Optional<VolunteerApplication> findByEventIdAndVolunteerId(Long eventId, Long volunteerId);
    
    @Query("SELECT va FROM VolunteerApplication va WHERE va.event.ngo.id = :ngoId")
    List<VolunteerApplication> findApplicationsByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT va FROM VolunteerApplication va WHERE va.event.ngo.id = :ngoId AND va.status = :status")
    List<VolunteerApplication> findApplicationsByNGOAndStatus(@Param("ngoId") Long ngoId, 
                                                              @Param("status") VolunteerApplication.ApplicationStatus status);
    
    @Query("SELECT va FROM VolunteerApplication va WHERE va.volunteer.id = :volunteerId AND va.status = 'APPROVED'")
    List<VolunteerApplication> findApprovedApplicationsByVolunteer(@Param("volunteerId") Long volunteerId);
    
    @Query("SELECT COUNT(va) FROM VolunteerApplication va WHERE va.event.id = :eventId AND va.status = 'APPROVED'")
    long countApprovedApplicationsByEvent(@Param("eventId") Long eventId);
    
    @Query("SELECT COUNT(va) FROM VolunteerApplication va WHERE va.volunteer.id = :volunteerId")
    long countApplicationsByVolunteer(@Param("volunteerId") Long volunteerId);
    
    boolean existsByEventIdAndVolunteerId(Long eventId, Long volunteerId);
}

