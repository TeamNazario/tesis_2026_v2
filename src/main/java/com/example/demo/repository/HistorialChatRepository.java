package com.example.demo.repository;

import com.example.demo.model.HistorialChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HistorialChatRepository extends JpaRepository<HistorialChat, Long> {

    List<HistorialChat> findByLlaveSesionOrderByFechaInicioDesc(String llaveSesion);

    List<HistorialChat> findByProcesoOrderByFechaInicioDesc(String proceso);

    @Query("SELECT h FROM HistorialChat h WHERE " +
           "(:llaveSesion IS NULL OR h.llaveSesion = :llaveSesion) AND " +
           "(:proceso IS NULL OR h.proceso = :proceso) AND " +
           "(:fechaDesde IS NULL OR h.fechaInicio >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR h.fechaInicio <= :fechaHasta) " +
           "ORDER BY h.fechaInicio DESC")
    List<HistorialChat> buscar(
            @Param("llaveSesion") String llaveSesion,
            @Param("proceso") String proceso,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta
    );
}
