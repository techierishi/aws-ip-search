package com.rishi.iprangesearch.repository;

import com.rishi.iprangesearch.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Region save(Region region);

    List<Region> findAll();
}