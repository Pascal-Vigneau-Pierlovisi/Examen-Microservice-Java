package com.example.practitionerservice.service;

import com.example.practitionerservice.entity.Practitioner;
import com.example.practitionerservice.repository.PractitionerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PractitionerService {

    private final PractitionerRepository repository;

    public PractitionerService(PractitionerRepository repository) {
        this.repository = repository;
    }

    public List<Practitioner> getAllPractitioners() {
        return repository.findAll();
    }

    public Optional<Practitioner> getPractitionerById(Long id) {
        return repository.findById(id);
    }

    public Practitioner savePractitioner(Practitioner practitioner) {
        return repository.save(practitioner);
    }

    public void deletePractitioner(Long id) {
        repository.deleteById(id);
    }
}
