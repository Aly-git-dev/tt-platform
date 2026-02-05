package com.upiiz.platform_api.controller;

import com.upiiz.platform_api.dto.AdminReportDto;
import com.upiiz.platform_api.dto.AdminUserSummaryDto;
import com.upiiz.platform_api.dto.ReportAdminActionDto;
import com.upiiz.platform_api.entities.User;
import com.upiiz.platform_api.repositories.UserRepository;
import com.upiiz.platform_api.services.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/upiiz/admin/v1/forums")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ForumAdminController {

    private final ForumService forumService;
    private final UserRepository userRepo;

    // Lista sólo pendientes (para el dashboard admin)
    @GetMapping("/reports")
    public List<AdminReportDto> getPendingReports() {
        return forumService.getPendingReportsForAdmin();
    }

    // Lista todos los reportes (histórico)
    @GetMapping("/reports/all")
    public List<AdminReportDto> getAllReports() {
        return forumService.getAllReportsForAdmin();
    }

    // Resolver un reporte
    @PostMapping("/reports/{id}/resolve")
    public void resolveReport(
            @PathVariable Long id,
            @RequestBody ReportAdminActionDto dto,
            Principal principal
    ) {
        // asumimos que principal.getName() = email_inst del admin (igual que en el resto del sistema)
        String adminEmail = principal.getName();
        forumService.resolveReport(id, dto, adminEmail);
    }

    // GET /upiiz/api/v1/admin/users/banned
    @GetMapping("/banned")
    public List<AdminUserSummaryDto> getBannedUsers() {
        // Aquí asumo que "baneado" = active = false
        List<User> users = userRepo.findByActiveFalseOrderByNombreAsc();

        return users.stream()
                .map(u -> AdminUserSummaryDto.builder()
                        .id(u.getId())
                        .emailInst(u.getEmailInst())
                        .fullName(u.getNombre())   // o getFull_name si así se llama
                        .carrera(u.getCarrera())
                        .active(u.isActive())
                        .build())
                .toList();
    }

    // POST /upiiz/api/v1/admin/users/{id}/unban
    @PostMapping("/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable UUID id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setActive(true);
        // opcional limpiar campos de ban:
        // user.setBannedAt(null);
        // user.setBannedBy(null);
        userRepo.save(user);

        return ResponseEntity.noContent().build();
    }
}
