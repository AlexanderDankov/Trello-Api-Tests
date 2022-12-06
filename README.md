<img src="images/Trello-logo.png" width=300 height=200>



# Trello-Api-Tests
## Autotest for https://api.trello.com using Rest-Assured

### Used tech stack:
| Java | JUnit5 | Rest-assured | Gradle | Intelij IDEA | Allure Report | Allure Testops | Jenkins | Telegram |
|------|--------|--------------|--------|--------------|---------------|----------------|---------|----------|
|![](images/JAVA.svg)|![](images/Junit5.svg)|<img src="images/Rest-assured.png" width=70 height=70>|![](images/Gradle.svg)|![](images/IDEA.svg)|![](images/AllureReport.svg)|![](images/AllureTestops.svg)|![](images/Jenkins.svg)|![](images/Telegram.svg)|

### Launch Parameters:
```
test - all tests
---------------
board_test - only tests for board
---------------
card_test - only tests for card
---------------
organisation_test - only tests for organisation
```
![image](https://user-images.githubusercontent.com/49765744/205925415-b65996bc-5add-428f-b38f-29be9ddfc3a7.png)

### To run tests You need to specify credential data to credential.properties file in resource directory like this:
```
apiKey={your api key}
token={your token}
organisationId={your workspace id}
```

### Job overview:
![image](https://user-images.githubusercontent.com/49765744/205926735-5b7b3625-6ec5-4848-987d-7138f305634e.png)

### Allure Report:
![image](https://user-images.githubusercontent.com/49765744/205926851-45e7e4ea-53e8-494e-8f45-86b9b0ad47e4.png)

### Tests with steps:
![image](https://user-images.githubusercontent.com/49765744/205927025-3817a61f-e5b8-4c63-aa07-7e7468591c39.png)

### Launch in Allure TestOps:
![image](https://user-images.githubusercontent.com/49765744/205927101-36a864c5-9f2b-43cc-b341-287e42a31549.png)

### Test cases:
![image](https://user-images.githubusercontent.com/49765744/205927200-47c5bd71-adf8-44e7-8dfe-4ec882ebfb9b.png)

### Telegram notification:
![image](https://user-images.githubusercontent.com/49765744/205927247-d4f3335c-953b-474b-b2bb-d45ab912c7ee.png)

