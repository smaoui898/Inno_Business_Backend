package com.inno.inno_project.auth.domain.port.out;

public interface FileStoragePort {

    // Retourne le chemin de stockage du fichier
    String store(byte[] data, String originalFileName, String category);
}
