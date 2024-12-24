package org.vsa.server.gpt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vsa.server.gpt.entity.QnA;

public interface QnARepository extends JpaRepository<QnA, Long> {
}