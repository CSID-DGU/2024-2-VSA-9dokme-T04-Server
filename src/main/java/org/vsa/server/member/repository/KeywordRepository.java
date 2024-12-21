package org.vsa.server.member.repository;

import org.vsa.server.member.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword,Integer> {

    List<Keyword> findAllByKeywordContaining(String keyword);

    @Query("SELECT k FROM Keyword k WHERE :title LIKE CONCAT('%', k.keyword, '%') OR k.keyword LIKE CONCAT('%', :title, '%')")
    List<Keyword> findMatchingKeywords(@Param("title") String title);


}
