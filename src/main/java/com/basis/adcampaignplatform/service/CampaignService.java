package com.basis.adcampaignplatform.service;

import com.basis.adcampaignplatform.model.Campaign;
import com.basis.adcampaignplatform.repository.CampaignRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Campaign createCampaign(Campaign campaign) {
        campaign.setStatus("ACTIVE");
        Campaign saved = campaignRepository.save(campaign);
        rabbitTemplate.convertAndSend("campaign-exchange",
                "campaign.created",
                "New campaign created: " + saved.getName());
        return saved;
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Campaign getCampaignById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }
}