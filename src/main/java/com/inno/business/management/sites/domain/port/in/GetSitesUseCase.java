package com.inno.business.management.sites.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.inno.business.management.sites.domain.model.Site;

public interface  GetSitesUseCase {
    List<Site> execute(String ownerEmail,UUID societeId) ;

}
