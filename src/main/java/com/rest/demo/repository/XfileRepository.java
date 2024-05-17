package com.rest.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XfileRepository extends JpaRepository<Xfile, String> {
    List<Xfile> findByfileGroup(String fileGroup);
}
