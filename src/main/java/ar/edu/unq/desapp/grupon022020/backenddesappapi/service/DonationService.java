package ar.edu.unq.desapp.grupon022020.backenddesappapi.service;

import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.Donation;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.DonorUser;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.Project;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.exceptions.DataNotFoundException;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.model.exceptions.InvalidDonationException;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.persistence.DonationRepository;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.persistence.DonorUserRepository;
import ar.edu.unq.desapp.grupon022020.backenddesappapi.persistence.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonorUserRepository donorUserRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public Donation save(Donation donation) {
        return this.donationRepository.save(donation);
    }

    public Donation findById(Integer id) {
        return this.donationRepository.findById(id).get();
    }

    public List<Donation> findAll() {
        return this.donationRepository.findAll();
    }

    public Donation donate(String nickname,
                           String projectName,
                           String comment,
                           int amount) throws DataNotFoundException, InvalidDonationException {
        Optional<DonorUser> donorUser = donorUserRepository.findById(nickname);
        Optional<Project> project = projectRepository.findById(projectName);
        if(!donorUser.isPresent()){
            throw new DataNotFoundException("User " + nickname + " does not exist");
        }
        if(!project.isPresent()){
            throw new DataNotFoundException("Project " + projectName + " does not exist");
        }
        Donation donation;
        try {
            donation = donorUser.get().donate(BigDecimal.valueOf(amount), comment, project.get());
            save(donation);
        } catch (InvalidDonationException e) {
            throw new InvalidDonationException(e.getMessage());
        }
        return donation;
    }
}