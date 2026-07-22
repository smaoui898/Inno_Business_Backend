package com.inno.business.management.beneficiaire.application;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.domain.model.Adresse;
import com.inno.business.management.beneficiaire.domain.model.AdresseType;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.model.StatutBeneficiaire;
import com.inno.business.management.beneficiaire.domain.model.TypeCourse;
import com.inno.business.management.beneficiaire.domain.port.in.ImportBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.sites.domain.exception.SiteNotFoundException;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;
public class ImportBeneficiairesUseCaseImpl implements ImportBeneficiairesUseCase {
    private final UserRepositoryPort         userRepository;
    private final SiteRepositoryPort         siteRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;
    private final SpringCompanyRepository    companyRepository;

    public ImportBeneficiairesUseCaseImpl(UserRepositoryPort userRepository,
                                          SiteRepositoryPort siteRepository,
                                          BeneficiaireRepositoryPort beneficiaireRepository,
                                          SpringCompanyRepository companyRepository) {
        this.userRepository         = userRepository;
        this.siteRepository         = siteRepository;
        this.beneficiaireRepository = beneficiaireRepository;
        this.companyRepository      = companyRepository;
    }

    @Override
    public ImportResult execute(ImportCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        var site = siteRepository.findById(cmd.siteId())
                .orElseThrow(() -> new SiteNotFoundException(cmd.siteId().toString()));

        if (!companyRepository.existsByIdAndUserId(site.getSocieteId(), owner.getId()))
            throw new UnauthorizedAccessException();

        int imported = 0;
        int skipped  = 0;
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(cmd.excelData()))) {
            Sheet sheet = workbook.getSheetAt(0);
            // Ligne 0 = en-têtes, on commence à 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String prenom    = getCellValue(row, 0);
                    String nom       = getCellValue(row, 1);
                    String email     = getCellValue(row, 2);
                    String telephone = getCellValue(row, 3);
                    String cin       = getCellValue(row, 4);
                    String matricule = getCellValue(row, 5);
                    String rue       = getCellValue(row, 6);
                    String ville     = getCellValue(row, 7);
                    String adresseTypeStr = getCellValue(row, 8);
                    String typeCourseStr  = getCellValue(row, 9);

                    if (prenom.isBlank() || nom.isBlank() || telephone.isBlank()) {
                        skipped++;
                        errors.add("Ligne " + (i + 1) + " : prénom, nom et téléphone sont obligatoires");
                        continue;
                    }

                    AdresseType adresseType = AdresseType.HOME;
                    try { adresseType = AdresseType.valueOf(adresseTypeStr.toUpperCase()); }
                    catch (Exception ignored) {}

                    List<Adresse> adresses = List.of(
                            new Adresse(UUID.randomUUID(), rue, ville, adresseType, true)
                    );

                    Beneficiaire ben = new Beneficiaire(
                            UUID.randomUUID(),
                            cmd.siteId(),
                            site.getSocieteId(),
                            prenom, nom, email, telephone, cin, matricule,
                            adresses,
                            TypeCourse.fromString(typeCourseStr),
                            StatutBeneficiaire.ACTIF,
                            null, null,
                            LocalDateTime.now()
                    );

                    beneficiaireRepository.save(ben);
                    imported++;
                } catch (Exception e) {
                    skipped++;
                    errors.add("Ligne " + (i + 1) + " : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Impossible de lire le fichier Excel : " + e.getMessage());
        }

        return new ImportResult(imported, skipped, errors);
    }

    private String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "";
        };
    }

}
