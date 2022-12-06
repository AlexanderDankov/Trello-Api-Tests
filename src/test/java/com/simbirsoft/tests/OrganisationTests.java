package com.simbirsoft.tests;

import com.simbirsoft.models.Organisation;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Добавление, изменение данных организации")
@Tag("Organisation")
public class OrganisationTests extends TestBase {

    @Test
    @DisplayName("Создание организации, изменение ее параметров, удаление организации")
    @Feature("Добавление организации, изменение данных")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
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
    @ParameterizedTest(name = "{0}")
    @DisplayName("Добавление участника к рабочему пространству: ")
    @Feature("Добавление организации, изменение данных")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.NORMAL)
    void addMemberToOrganisationAndGetItInList(String email, String fullName) {
        String companyName = faker.company().name();

        Organisation company = stepService.createOrganisation(companyName);

        stepService.addMemberToOrganisation(company, email, fullName);

        stepService.checkMemberInOrganisation(company, fullName);

        stepService.deleteOrganisation(company);
    }
}
