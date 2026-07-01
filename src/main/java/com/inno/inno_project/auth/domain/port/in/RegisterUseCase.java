package com.inno.inno_project.auth.domain.port.in;

public interface RegisterUseCase {

    record RegisterCommand(
        String prenom,
        String nom,
        String telephone,
        String email,
        String password,
        String confirmPassword,

        String nomGroupe,
        String raisonSociale,
        String identifiantUnique,
        String adresse,
        String ville,
        String emailSociete,
        String telephoneSociete,

        byte[] rneData,
        String rneFileName,
        byte[] patenteData,
        String patenteFileName,
        byte[] cinGerantData,
        String cinGerantFileName
    ) {}

    record RegisterResult(String message, String email) {}

    RegisterResult execute(RegisterCommand command);
}