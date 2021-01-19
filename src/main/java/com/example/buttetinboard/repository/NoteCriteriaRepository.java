package com.example.buttetinboard.repository;

import com.example.buttetinboard.dto.NoteResponse;
import com.example.buttetinboard.model.Note;
import com.example.buttetinboard.model.NotePage;
import com.example.buttetinboard.model.NoteSearchCriteria;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NoteCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public NoteCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Note> findByWithFilters(NotePage notePage,
                                        NoteSearchCriteria noteSearchCriteria) {
        CriteriaQuery<Note> criteriaQuery = criteriaBuilder.createQuery(Note.class);
        Root<Note> noteRoot = criteriaQuery.from(Note.class);
        Predicate predicate = getPredicate(noteSearchCriteria, noteRoot);
        criteriaQuery.where(predicate);
        setOrder(notePage, criteriaQuery, noteRoot);

        TypedQuery<Note> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(notePage.getPageNumber() * notePage.getPageSize());
        typedQuery.setMaxResults(notePage.getPageSize());

        Pageable pageable = getPageable(notePage);

        long notesCount = getNotesCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(),
                pageable, notesCount);
    }

    private Predicate getPredicate(NoteSearchCriteria noteSearchCriteria,
                                   Root<Note> noteRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(noteSearchCriteria.getNoteName())) {
            predicates.add(
                    criteriaBuilder.like(noteRoot.get("noteName"),
                            "%" + noteSearchCriteria.getNoteName() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(NotePage notePage,
                          CriteriaQuery<Note> criteriaQuery,
                          Root<Note> noteRoot) {
        if (notePage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(noteRoot.get(notePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(noteRoot.get(notePage.getSortBy())));
        }
    }

    private Pageable getPageable(NotePage notePage) {
        Sort sort = Sort.by(notePage.getSortDirection(),
                notePage.getSortBy());
        return PageRequest.of(notePage.getPageNumber(),
                notePage.getPageSize(), sort);
    }

    private long getNotesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Note> countRoot = countQuery.from(Note.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
