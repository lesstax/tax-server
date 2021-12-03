package com.lesstax.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.lesstax.model.Client;
import com.lesstax.request.model.ClientPage;
import com.lesstax.request.model.ClientSearchCriteria;

@Repository
public class ClientCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ClientCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Client> findAllWithFilters(ClientPage clientPage,
                                             ClientSearchCriteria ClientSearchCriteria){
        CriteriaQuery<Client> criteriaQuery = criteriaBuilder.createQuery(Client.class);
        Root<Client> ClientRoot = criteriaQuery.from(Client.class);
        Predicate predicate = getPredicate(ClientSearchCriteria, ClientRoot);
        criteriaQuery.where(predicate);
        setOrder(clientPage, criteriaQuery, ClientRoot);

        TypedQuery<Client> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(clientPage.getPageNumber() * clientPage.getPageSize());
        typedQuery.setMaxResults(clientPage.getPageSize());

        Pageable pageable = getPageable(clientPage);

        long ClientsCount = getClientsCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, ClientsCount);
    }

    private Predicate getPredicate(ClientSearchCriteria ClientSearchCriteria,
                                   Root<Client> ClientRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(ClientSearchCriteria.getFirstName())){
            predicates.add(
                    criteriaBuilder.like(ClientRoot.get("firstName"),
                            "%" + ClientSearchCriteria.getFirstName() + "%")
            );
        }
        if(Objects.nonNull(ClientSearchCriteria.getLastName())){
            predicates.add(
                    criteriaBuilder.like(ClientRoot.get("lastName"),
                            "%" + ClientSearchCriteria.getLastName() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(ClientPage clientPage,
                          CriteriaQuery<Client> criteriaQuery,
                          Root<Client> ClientRoot) {
        if(clientPage.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(ClientRoot.get(clientPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(ClientRoot.get(clientPage.getSortBy())));
        }
    }

    private Pageable getPageable(ClientPage ClientPage) {
        Sort sort = Sort.by(ClientPage.getSortDirection(), ClientPage.getSortBy());
        return PageRequest.of(ClientPage.getPageNumber(),ClientPage.getPageSize(), sort);
    }

    private long getClientsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Client> countRoot = countQuery.from(Client.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}