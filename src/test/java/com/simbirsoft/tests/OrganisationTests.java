package com.simbirsoft.tests;

import com.simbirsoft.models.Organisation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganisationTests extends TestBase {

    @Test
    void createUpdateAndDeleteOrganisation() {
        String companyName = faker.company().name();

        Organisation company = stepService.createOrganisation(companyName);

        assertThat(company.getDisplayName()).isEqualTo(companyName);

        String webSite = faker.internet().url();

        company = stepService.updateOrganisation(company, "website", webSite);

        assertThat(company.getWebsite()).isEqualTo("http://" + webSite);

        stepService.deleteOrganisation(company);

        stepService.getOrganisationNotFound(company);

    }

    @CsvSource({
            "firstUser@mail.com, First User",
            "secondUser@mail.com, Second User",
            "thirdUser@mail.com, Third User"
    })
    @ParameterizedTest(name = "Добавление участника к рабочему пространству: {0}")
    void addMemberToOrganisationAndGetItInList(String email, String fullName) {
        String companyName = faker.company().name();

        Organisation company = stepService.createOrganisation(companyName);

        stepService.addMemberToOrganisation(company, email, fullName);

        stepService.checkMemberInOrganisation(company, fullName);

        stepService.deleteOrganisation(company);
    }
}
