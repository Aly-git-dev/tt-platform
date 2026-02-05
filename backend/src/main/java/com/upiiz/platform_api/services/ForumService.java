package com.upiiz.platform_api.services;

import com.upiiz.platform_api.entities.User;
import com.upiiz.platform_api.dto.*;
import com.upiiz.platform_api.entities.*;
import com.upiiz.platform_api.models.ForumStatus;
import com.upiiz.platform_api.models.PostStatus;
import com.upiiz.platform_api.models.ReportStatus;
import com.upiiz.platform_api.models.ThreadType;
import com.upiiz.platform_api.repositories.*;
import com.upiiz.platform_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumCategoryRepository categoryRepo;
    private final ForumSubareaRepository subareaRepo;
    private final ForumThreadRepository threadRepo;
    private final ForumPostRepository postRepo;
    private final ForumAttachmentRepository attachmentRepo;
    private final ForumReportRepository reportRepo;
    private final UserRepository userRepo;
    private final UserInterestTagRepository interestRepo;


    // ==========================
    // Resumen
    // ==========================
    @Transactional
    public ForumUserSummaryDto getUserSummary(UUID userId) {
        long threads = threadRepo.countByAuthorId(userId);
        long posts   = postRepo.countByAuthorId(userId);
        long follows = interestRepo != null ? interestRepo.countByUserId(userId) : 0L;

        return new ForumUserSummaryDto(threads, posts, follows);
    }
    // ==========================
    // Crear hilo
    // ==========================
    @Transactional
    public ThreadDetailDto createThread(ThreadCreateDto dto, String userEmail) {
        User author = userRepo.findByEmailInst(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        ForumCategory category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría inválida"));

        ForumSubarea subarea = null;
        if (dto.getSubareaId() != null) {
            subarea = subareaRepo.findById(dto.getSubareaId())
                    .orElseThrow(() -> new IllegalArgumentException("Subárea inválida"));
        }

        ThreadType type = ThreadType.valueOf(dto.getType().toUpperCase());

        ForumThread thread = ForumThread.builder()
                .author(author)
                .category(category)
                .subarea(subarea)
                .title(dto.getTitle())
                .body(dto.getBody())
                .type(type)
                .status(ForumStatus.ABIERTO)
                .score(0)
                .answersCount(0)
                .views(0)
                .build();

        ForumThread saved = threadRepo.save(thread);
        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<ForumAttachment> attachments = dto.getAttachments().stream()
                    .map(a -> ForumAttachment.builder()
                            .thread(saved)
                            .post(null)
                            .kind(a.getKind())
                            .url(a.getUrl())
                            .metadata(null)
                            .build())
                    .toList();
            attachmentRepo.saveAll(attachments);
        }
        return mapThreadToDetail(saved, List.of());
    }

    // ==========================
    // Obtener detalle de hilo
    // ==========================
    @Transactional(readOnly = true)
    public ThreadDetailDto getThread(Long id) {
        ForumThread thread = threadRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hilo no encontrado"));

        List<ForumPost> posts = postRepo.findByThreadIdOrderByCreatedAtAsc(id);
        List<PostDto> postDtos = posts.stream()
                .map(this::mapPostToDto)
                .toList();

        return mapThreadToDetail(thread, postDtos);
    }

    // ==========================
    // Crear respuesta
    // ==========================
    @Transactional
    public PostDto createPost(Long threadId, PostCreateDto dto, String userEmail) {
        User author = userRepo.findByEmailInst(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        ForumThread thread = threadRepo.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("Hilo no encontrado"));

        if (thread.getStatus() != ForumStatus.ABIERTO) {
            throw new IllegalStateException("Hilo cerrado");
        }

        ForumPost parent = null;
        if (dto.getParentPostId() != null) {
            parent = postRepo.findById(dto.getParentPostId())
                    .orElseThrow(() -> new IllegalArgumentException("Post padre no encontrado"));
        }

        ForumPost post = ForumPost.builder()
                .thread(thread)
                .author(author)
                .parent(parent)
                .body(dto.getBody())
                .score(0)
                .acceptedAnswer(false)
                .build();
        post.setStatus(PostStatus.VISIBLE);
        ForumPost savedPost = postRepo.save(post);

        // guardar adjuntos si vienen
        if (dto.getAttachments() != null && !dto.getAttachments().isEmpty()) {
            List<ForumAttachment> attachments = dto.getAttachments().stream()
                    .map(a -> ForumAttachment.builder()
                            .post(savedPost)
                            .kind(a.getKind())
                            .url(a.getUrl())
                            .metadata(null)
                            .build())
                    .toList();
            attachmentRepo.saveAll(attachments);
        }

        // actualizar contador de respuestas del hilo
        thread.setAnswersCount(thread.getAnswersCount() + 1);
        threadRepo.save(thread);

        return mapPostToDto(savedPost);
    }

    // ==========================
    // Reportar contenido
    // ==========================
    @Transactional
    public void createReport(ReportCreateDto dto, String userEmail) {
        User reporter = userRepo.findByEmailInst(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        ForumThread thread = null;
        ForumPost post = null;

        if (dto.getThreadId() != null) {
            thread = threadRepo.findById(dto.getThreadId())
                    .orElseThrow(() -> new IllegalArgumentException("Hilo no encontrado"));
        }

        if (dto.getPostId() != null) {
            post = postRepo.findById(dto.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("Post no encontrado"));
        }

        ForumReport report = ForumReport.builder()
                .reporter(reporter)
                .thread(thread)
                .post(post)
                .reasonCode(dto.getReasonCode())
                .description(dto.getDescription())
                .build();

        reportRepo.save(report);
    }

    @Transactional(readOnly = true)
    public List<ThreadSummaryDto> getRecommendedThreads(String userEmail) {
        // Por ahora: top 5 ABIERTO por score/fecha
        List<ForumThread> threads = threadRepo
                .findTop5ByStatusOrderByScoreDescCreatedAtDesc(ForumStatus.ABIERTO);

        return threads.stream()
                .map(this::mapThreadToSummary)
                .toList();
    }
    @Transactional(readOnly = true)
    // ==========================
    // Mappers
    // ==========================
    protected ThreadDetailDto mapThreadToDetail(ForumThread t, List<PostDto> posts) {

        var threadAttachments = attachmentRepo.findByThreadId(t.getId())
                .stream()
                .map(a -> AttachmentDto.builder()
                        .id(a.getId())
                        .kind(a.getKind())
                        .url(a.getUrl())
                        .build())
                .toList();

        return ThreadDetailDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .body(t.getBody())
                .type(t.getType().name())
                .status(t.getStatus().name())
                .score(t.getScore())
                .answersCount(t.getAnswersCount())
                .views(t.getViews())
                .categoryId(t.getCategory().getId())
                .categoryName(t.getCategory().getName())
                .subareaId(t.getSubarea() != null ? t.getSubarea().getId() : null)
                .subareaName(t.getSubarea() != null ? t.getSubarea().getName() : null)
                .authorId(t.getAuthor().getId().toString())
                .authorName(t.getAuthor().getNombre())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .attachments(threadAttachments)
                .posts(posts)
                .build();
    }

    private ThreadSummaryDto mapThreadToSummary(ForumThread t) {
        return ThreadSummaryDto.builder()
                .id(t.getId())
                .title(t.getTitle())
                .categoryName(t.getCategory().getName())
                .subareaName(t.getSubarea() != null ? t.getSubarea().getName() : null)
                .type(t.getType().name())
                .score(t.getScore())
                .answersCount(t.getAnswersCount())
                .views(t.getViews())
                .status(t.getStatus().name())
                .createdAt(t.getCreatedAt())
                .build();
    }

    private PostDto mapPostToDto(ForumPost p) {

        // 1) Cargar adjuntos desde la tabla forum_attachment
        var attachments = attachmentRepo.findByPostId(p.getId())
                .stream()
                .map(a -> AttachmentDto.builder()
                        .id(a.getId())
                        .kind(a.getKind())
                        .url(a.getUrl())
                        .build()
                )
                .toList();

        // 2) Construir el DTO del post con la lista de adjuntos
        return PostDto.builder()
                .id(p.getId())
                .body(p.getBody())
                .status(p.getStatus().name())
                .score(p.getScore())
                .acceptedAnswer(p.isAcceptedAnswer())
                .authorId(p.getAuthor().getId().toString())
                .authorName(p.getAuthor().getNombre())
                .parentPostId(p.getParent() != null ? p.getParent().getId() : null)
                .createdAt(p.getCreatedAt())
                .attachments(attachments)
                .build();
    }
    // ==========================
    // ADMIN: listar reportes
    // ==========================
    @Transactional(readOnly = true)
    public List<AdminReportDto> getPendingReportsForAdmin() {
        List<ForumReport> reports =
                reportRepo.findByStatusOrderByCreatedAtAsc(ReportStatus.PENDIENTE);

        return reports.stream()
                .map(this::mapReportToAdminDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminReportDto> getAllReportsForAdmin() {
        List<ForumReport> reports = reportRepo.findAllByOrderByCreatedAtDesc();
        return reports.stream()
                .map(this::mapReportToAdminDto)
                .toList();
    }

    // ==========================
    // ADMIN: resolver reporte
    // ==========================
    @Transactional
    public void resolveReport(Long reportId, ReportAdminActionDto actionDto, String adminEmail) {

        User admin = userRepo.findByEmailInst(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin no encontrado"));

        ForumReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Reporte no encontrado"));

        // 1) El contenido reportado
        ForumPost post = report.getPost();
        ForumThread thread = report.getThread();

        // 2) Baneamos/activamos usuario si se indicó
        if (actionDto.isBanUser()) {
            User toBan;
            if (post != null) {
                toBan = post.getAuthor();
            } else if (thread != null) {
                toBan = thread.getAuthor();
            } else {
                throw new IllegalStateException("Reporte sin post ni thread asociado");
            }
            toBan.setActive(false);
            userRepo.save(toBan);
        }

        // 3) Eliminar/ocultar contenido si se indicó
        if (actionDto.isDeleteContent()) {
            if (post != null) {
                post.setStatus(PostStatus.OCULTO); // o el enum que uses
                postRepo.save(post);
            } else if (thread != null) {
                thread.setStatus(ForumStatus.CERRADO); // o un estado OCULTO si lo tienes
                threadRepo.save(thread);
            }
        }

        // 4) Marcar el reporte como resuelto
        report.setStatus(ReportStatus.RESUELTO);
        report.setHandledBy(admin);
        report.setHandledAt(Instant.now());
        // si quieres guardar nota:
        report.setDescription(actionDto.getAdminNote());
        reportRepo.save(report);
    }

    // ==========================
    // Mapper para admin
    // ==========================
    private AdminReportDto mapReportToAdminDto(ForumReport r) {
        ForumThread t = r.getThread();
        ForumPost p = r.getPost();

        User reportedUser = null;
        if (p != null) {
            reportedUser = p.getAuthor();
        } else if (t != null) {
            reportedUser = t.getAuthor();
        }

        return AdminReportDto.builder()
                .id(r.getId())
                .reporterId(r.getReporter().getId().toString())
                .reporterName(r.getReporter().getNombre())
                .threadId(t != null ? t.getId() : null)
                .threadTitle(t != null ? t.getTitle() : null)
                .postId(p != null ? p.getId() : null)
                .reportedUserId(reportedUser != null ? reportedUser.getId().toString() : null)
                .reportedUserName(reportedUser != null ? reportedUser.getNombre() : null)
                .reasonCode(r.getReasonCode())
                .description(r.getDescription())
                .status(r.getStatus().name())
                .createdAt(r.getCreatedAt())
                .handledAt(r.getHandledAt())
                .handledByName(r.getHandledBy() != null ? r.getHandledBy().getNombre() : null)
                .build();
    }
}
