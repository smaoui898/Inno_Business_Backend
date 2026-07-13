package com.inno.business.management.companie.domain.ports.in;

import java.util.List;

import com.inno.business.management.companie.domain.model.Societe;

public interface GetSocietesUseCase {
    List<Societe> execute(String ownerEmail);
}


