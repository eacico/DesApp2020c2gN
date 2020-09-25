package ar.edu.unq.desapp.grupon022020.backenddesappapi.model;

import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.builder.DonationBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DonationTest {

    @Test
    public void testDonationDonorNickname() {
        String donorNickname = "Juan2001";
        Donation donation = DonationBuilder.aDonation().withDonorNickname(donorNickname).build();

        assertEquals(donorNickname, donation.getDonorNickname());
    }

    @Test
    public void testDonationProjectName() {
        String projectName = "Conectando San Cristobal";
        Donation donation = DonationBuilder.aDonation().withProjectName(projectName).build();

        assertEquals(projectName, donation.getProjectName());
    }

    @Test
    public void testDonationAmount() {
        int amount = 2530;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        assertEquals(amount, donation.getAmount());
    }

    @Test
    public void testDonationComment() {
        String comment = "This is a donation";
        Donation donation = DonationBuilder.aDonation().withComment(comment).build();

        assertEquals(comment, donation.getComment());

    }

    @Test
    public void testDonationDate() {
        LocalDate date = LocalDate.now();
        Donation donation = DonationBuilder.aDonation().withDate(date).build();

        assertEquals(date, donation.getDate());
    }

    @Test
    public void testDonationWithNoPoints() {
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.empty());
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(3000);
        int amount = 500;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals(0, points);
    }

    @Test
    public void testDonationWithAmountPoints() {
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.empty());
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(3000);
        int amount = 1500;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals(amount, points);
    }

    @Test
    public void testDonationWithPopulationPoints() {
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.empty());
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(1200);
        int amount = 500;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals(amount * 2, points);
    }

    @Test
    public void testDonationWithLastDonationPoints() {
        Donation lastDonation = mock(Donation.class);
        when(lastDonation.getDate()).thenReturn(LocalDate.now());
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.of(lastDonation));
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(3000);
        int amount = 500;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals(donation.pointsFromLastDonationOnSameMonth(), points);
    }

    @Test
    public void testDonationWithAmountAndLastDonationPoints() {
        Donation lastDonation = mock(Donation.class);
        when(lastDonation.getDate()).thenReturn(LocalDate.now());
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.of(lastDonation));
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(3000);
        int amount = 4000;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals(amount + donation.pointsFromLastDonationOnSameMonth(), points);
    }

    @Test
    public void testDonationWithPopulationAndLastDonationPoints() {
        Donation lastDonation = mock(Donation.class);
        when(lastDonation.getDate()).thenReturn(LocalDate.now());
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.of(lastDonation));
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(900);
        int amount = 700;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals((amount * 2) + donation.pointsFromLastDonationOnSameMonth(), points);
    }

    @Test
    public void testDonationWithAmountAndPopulationAndLastDonationPoints() {
        Donation lastDonation = mock(Donation.class);
        when(lastDonation.getDate()).thenReturn(LocalDate.now());
        DonorUser donorUser = mock(DonorUser.class);
        when(donorUser.getLastDonation()).thenReturn(Optional.of(lastDonation));
        Project project = mock(Project.class);
        when(project.getLocationPopulation()).thenReturn(900);
        int amount = 5000;
        Donation donation = DonationBuilder.aDonation().withAmount(amount).build();

        int points = donation.calculatePoints(donorUser, project);
        assertEquals((amount * 2) + donation.pointsFromLastDonationOnSameMonth(), points);
    }

}