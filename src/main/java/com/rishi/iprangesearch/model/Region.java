package com.rishi.iprangesearch.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "REGION_MASTER")
@Data
public class Region {

    @Column(name = "id")
    @Id
    Integer id;

    @Column(name = "name")
    String name;
}

