package com.grazac.springauthgrazac.teacher;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WalletSpecification {

    public static Specification<Wallet> filter(WalletFilter filter) {
        return (root, query, cb) ->{
            List<Predicate> predicates = new ArrayList<>();
            if(filter.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%"+ filter.getName().toLowerCase() + "%"));
            }

            if(filter.getSubject() != null) {
                predicates.add(cb.like(cb.lower(root.get("subject")), "%"+ filter.getSubject().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

}

