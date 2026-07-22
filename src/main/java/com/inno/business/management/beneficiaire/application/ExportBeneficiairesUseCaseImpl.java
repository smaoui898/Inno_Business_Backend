package com.inno.business.management.beneficiaire.application;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.CompanyRepositoryPort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.port.in.ExportBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;

public class ExportBeneficiairesUseCaseImpl implements ExportBeneficiairesUseCase {
     private final UserRepositoryPort         userRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;
    private final SpringCompanyRepository      companyRepository;

    public ExportBeneficiairesUseCaseImpl(UserRepositoryPort userRepository,
                                          BeneficiaireRepositoryPort beneficiaireRepository,
                                          SpringCompanyRepository companyRepository) {
        this.userRepository         = userRepository;
        this.beneficiaireRepository = beneficiaireRepository;
        this.companyRepository      = companyRepository;
    }

    // ── Colonnes du fichier Excel ─────────────────────────────────────
    private static final String[] HEADERS = {
        "Prénom", "Nom", "Email", "Téléphone", "CIN", "Matricule",
        "Adresse", "Ville", "Type adresse", "Type course", "Statut"
    };

    @Override
    public byte[] execute(String ownerEmail, UUID siteId) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Beneficiaire> beneficiaires = siteId != null
                ? beneficiaireRepository.findAllBySiteId(siteId)
                : getAllForOwner(owner);

        return buildExcel(beneficiaires);
    }

    @Override
    public byte[] generateTemplate() {
        return buildExcel(List.of());  // fichier vide avec juste les en-têtes
    }

    private List<Beneficiaire> getAllForOwner(User owner) {
        var companies = companyRepository.findAllByUserId(owner.getId());
        if (companies.isEmpty()) return List.of();
        UUID societeId = companies.get(0).getId();
        return beneficiaireRepository.findAllBySiteId(null); // adaptation possible
    }

    private byte[] buildExcel(List<Beneficiaire> beneficiaires) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Bénéficiaires");

            // Style en-tête
            CellStyle headerStyle = workbook.createCellStyle();
            Font font;
            font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Ligne d'en-têtes
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 5000);
            }

            // Données
            int rowNum = 1;
            for (Beneficiaire b : beneficiaires) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(b.getPrenom());
                row.createCell(1).setCellValue(b.getNom());
                row.createCell(2).setCellValue(b.getEmail() != null ? b.getEmail() : "");
                row.createCell(3).setCellValue(b.getTelephone());
                row.createCell(4).setCellValue(b.getCin() != null ? b.getCin() : "");
                row.createCell(5).setCellValue(b.getMatricule() != null ? b.getMatricule() : "");

                // Première adresse par défaut
                var defaultAddr = b.getAdresses().stream()
                        .filter(a -> a.isDefault()).findFirst()
                        .orElse(b.getAdresses().isEmpty() ? null : b.getAdresses().get(0));

                row.createCell(6).setCellValue(defaultAddr != null ? defaultAddr.getRue() : "");
                row.createCell(7).setCellValue(defaultAddr != null ? defaultAddr.getVille() : "");
                row.createCell(8).setCellValue(defaultAddr != null ? defaultAddr.getType().name() : "HOME");
                row.createCell(9).setCellValue(b.getTypeCourse().name());
                row.createCell(10).setCellValue(b.getStatut().name());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération Excel : " + e.getMessage());
        }
    }

}
