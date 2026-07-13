package com.inno.business.management.sites.domain.port.in;

import java.util.UUID;

public interface DeleteSiteUseCase {
    void execute(String ownerEmail, UUID siteId);
}