package com.inno.business.auth.infrastructure.adapter.in.web.dto;

// Infos non sensibles renvoyées au web (le token, lui, est dans le cookie)
public record UserInfoDto(String email, String role, String prenom, String nom) {
}