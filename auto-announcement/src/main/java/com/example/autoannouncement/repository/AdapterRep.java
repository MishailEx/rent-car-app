package com.example.autoannouncement.repository;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class AdapterRep {

    private CategoryRepository categoryRepository;
    private AnnRepository annRepository;
    private AutoRepository autoRepository;
    private MarkRepository markRepository;
    private ModelRepository modelRepository;

    @Autowired
    public AdapterRep(CategoryRepository categoryRepository, AnnRepository annRepository, AutoRepository autoRepository, MarkRepository markRepository, ModelRepository modelRepository) {
        this.categoryRepository = categoryRepository;
        this.annRepository = annRepository;
        this.autoRepository = autoRepository;
        this.markRepository = markRepository;
        this.modelRepository = modelRepository;
    }
}
