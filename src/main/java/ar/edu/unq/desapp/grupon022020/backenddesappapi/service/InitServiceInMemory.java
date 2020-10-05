package ar.edu.unq.desapp.grupon022020.backenddesappapi.service;

import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.Donation;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.DonorUser;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.Project;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.builder.DonationBuilder;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.builder.DonorUserBuilder;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.builder.ProjectBuilder;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.exceptions.InvalidDonationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.Location;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.builder.LocationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
@Transactional
public class InitServiceInMemory {

    protected final Log logger = LogFactory.getLog(getClass());

    @Value("${spring.datasource.driverClassName:NONE}")
    private String className;

    @Autowired
    private LocationService locationService;
    @Autowired
    private DonationService donationService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DonorUserService donorUserService;

    @PostConstruct
    public void initialize() {
        if (className.equals("org.h2.Driver")) {
            logger.warn("Init Data Using H2 DB");
            fireInitialData();
        }
    }

    private void fireInitialData() {
        Location location_1 = LocationBuilder.aLocation().withName("Santa Rita").build();
        Location location_2 = LocationBuilder.aLocation().withName("Rio Tercero").build();
        Location location_3 = LocationBuilder.aLocation().withName("Puerto Iguazu").build();
        Location location_4 = LocationBuilder.aLocation().withName("Cruz Azul").build();
        locationService.save(location_1);
        locationService.save(location_4);

        DonorUser donorUser_1 = DonorUserBuilder.aDonorUser().withNickname("juan123").withMoney(BigDecimal.valueOf(9000)).build();
        donorUserService.save(donorUser_1);
        DonorUser donorUser_2 = DonorUserBuilder.aDonorUser().withNickname("maria456").withMoney(BigDecimal.valueOf(8000)).build();
        donorUserService.save(donorUser_2);
        DonorUser donorUser_3 = DonorUserBuilder.aDonorUser().withNickname("nick000").withMoney(BigDecimal.valueOf(7500)).build();
        donorUserService.save(donorUser_3);

        Project project_1 = ProjectBuilder.aProject().withName("Conectando Santa Rita").withLocation(location_1).build();
        projectService.save(project_1);
        Project project_2 = ProjectBuilder.aProject().withName("Conectando Rio Tercero").withLocation(location_2).build();
        projectService.save(project_2);
        Project project_3 = ProjectBuilder.aProject().withName("Conectando Puerto Iguazu").withLocation(location_3).build();
        projectService.save(project_3);

        try {
            donorUser_1.donate(BigDecimal.valueOf(1200), "This is my first donation!", project_1);
            donorUser_1.donate(BigDecimal.valueOf(123), "This is my third donation!", project_1);
            donorUser_3.donate(BigDecimal.valueOf(666), "Whatever", project_3);
            donorUser_1.donate(BigDecimal.valueOf(300), "This is my fourth donation!", project_2);
            donorUser_1.donate(BigDecimal.valueOf(400), "This is my fifth donation!", project_2);
            donorUser_2.donate(BigDecimal.valueOf(500), "Cool!", project_1);
            donorUser_2.donate(BigDecimal.valueOf(2000), "Awesome!", project_1);
            donorUser_1.donate(BigDecimal.valueOf(200), "This is my second donation!", project_1);
            donorUserService.save(donorUser_1);
            donorUserService.save(donorUser_2);
            donorUserService.save(donorUser_3);
        } catch (InvalidDonationException e) {
            e.printStackTrace();
        }


    }
}
