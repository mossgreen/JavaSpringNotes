package com.ihobb.gm.admin.service;

import com.ihobb.gm.admin.domain.Organization;
import com.ihobb.gm.admin.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization fetchOrganizationByCode(String orgCode) {
        return organizationRepository.findByCode(orgCode).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Organization> fetchAll() {
        return organizationRepository.findAll();
    }
}
