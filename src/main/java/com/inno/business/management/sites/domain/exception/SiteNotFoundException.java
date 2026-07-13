package com.inno.business.management.sites.domain.exception;

public class SiteNotFoundException extends RuntimeException {

    public SiteNotFoundException(String id) {
        super("Site introuvable : " + id);
    }
}
