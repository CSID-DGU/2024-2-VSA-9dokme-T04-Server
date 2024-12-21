package org.vsa.server.inquiring.repository;

import org.vsa.server.inquiring.entity.Inquire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquireRepository extends JpaRepository<Inquire, Long> {
    Page<Inquire> findAll(Pageable pageable);
}
