package com.ngoconnect.repository;

import com.ngoconnect.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByNgoId(Long ngoId);
    
    List<Event> findByStatus(Event.EventStatus status);
    
    @Query("SELECT e FROM Event e WHERE e.ngo.isApproved = true AND e.status = 'OPEN'")
    List<Event> findOpenEventsFromApprovedNGOs();
    
    @Query("SELECT e FROM Event e WHERE e.ngo.isApproved = true AND " +
           "(e.title LIKE %:search% OR e.description LIKE %:search% OR e.location LIKE %:search%)")
    List<Event> searchEvents(@Param("search") String search);
    
    @Query("SELECT e FROM Event e WHERE e.ngo.isApproved = true AND e.ngo.category = :category")
    List<Event> findEventsByCategory(@Param("category") String category);
    
    @Query("SELECT e FROM Event e WHERE e.eventDate >= :startDate AND e.eventDate <= :endDate")
    List<Event> findEventsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e FROM Event e WHERE e.eventDate >= :currentDate AND e.status = 'OPEN' ORDER BY e.eventDate ASC")
    List<Event> findUpcomingOpenEvents(@Param("currentDate") LocalDateTime currentDate);
    
    @Query("SELECT e FROM Event e WHERE e.ngo.id = :ngoId AND e.status = 'OPEN'")
    List<Event> findOpenEventsByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT COUNT(e) FROM Event e WHERE e.ngo.id = :ngoId")
    long countEventsByNGO(@Param("ngoId") Long ngoId);
}

