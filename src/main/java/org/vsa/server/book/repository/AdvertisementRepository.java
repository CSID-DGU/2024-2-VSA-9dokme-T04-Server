package org.vsa.server.book.repository;

import org.vsa.server.book.entity.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement,Integer> {

    List<Advertisement> findAll();

}
