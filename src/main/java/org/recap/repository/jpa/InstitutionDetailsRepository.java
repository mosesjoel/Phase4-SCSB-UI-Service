package org.recap.repository.jpa;

import org.recap.model.jpa.InstitutionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author dinakar on 24/12/20
 */
public interface InstitutionDetailsRepository extends BaseRepository<InstitutionEntity> {

    /**
     *To get the institution entity for the given institution code.
     *
     * @param institutionCode the institution code
     * @return the institution entity
     */
    InstitutionEntity findByInstitutionCode(String institutionCode);

    /**
     *To get the institution entity for the given institution name.
     *
     * @param institutionName the institution name
     * @return the institution entity
     */
    InstitutionEntity findByInstitutionName(String institutionName);

    /**
     * To get the list of institution entities for the super admin role.
     *
     * @return the institution code for super admin
     */
    @Query(value="select inst from InstitutionEntity inst where inst.institutionCode not in (:supportInstitution)")
    List<InstitutionEntity> getInstitutionCodeForSuperAdmin(@Param("supportInstitution") String supportInstitution);

    /**
     * To get the list of institution entities for home page.
     *
     * @return the institutions
     */
    @Query(value="select inst from InstitutionEntity inst  where inst.institutionCode not in (:supportInstitution) ORDER BY inst.id")
    List<InstitutionEntity> getInstitutionCodes(@Param("supportInstitution") String supportInstitution);
}
