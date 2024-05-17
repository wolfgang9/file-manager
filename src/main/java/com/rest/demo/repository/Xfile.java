package com.rest.demo.repository;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "xfile")
public class Xfile {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "FILEGROUP")
    private String fileGroup;

    @Column(name = "FILENAME")
    private String fileName;

    @Column(name = "FILES")
    private byte [] files;
}
