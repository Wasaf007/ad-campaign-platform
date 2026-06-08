package com.basis.adcampaignplatform;

import com.basis.adcampaignplatform.model.Campaign;
import com.basis.adcampaignplatform.repository.CampaignRepository;
import com.basis.adcampaignplatform.service.CampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CampaignService campaignService;

    private Campaign testCampaign;

    @BeforeEach
    void setUp() {
        testCampaign = new Campaign();
        testCampaign.setId(1L);
        testCampaign.setName("Nike Summer Campaign");
        testCampaign.setAdvertiser("Nike");
        testCampaign.setBudget(50000.00);
        testCampaign.setStatus("ACTIVE");
    }

    @Test
    void createCampaign_ShouldSaveAndSendMessage() {
        when(campaignRepository.save(any(Campaign.class))).thenReturn(testCampaign);

        Campaign result = campaignService.createCampaign(testCampaign);

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        assertEquals("Nike Summer Campaign", result.getName());
        verify(campaignRepository, times(1)).save(any(Campaign.class));
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("campaign-exchange"),
                eq("campaign.created"),
                anyString()
        );
    }

    @Test
    void getAllCampaigns_ShouldReturnAllCampaigns() {
        Campaign campaign2 = new Campaign();
        campaign2.setId(2L);
        campaign2.setName("Adidas Winter Campaign");
        campaign2.setAdvertiser("Adidas");
        campaign2.setBudget(30000.00);

        when(campaignRepository.findAll()).thenReturn(Arrays.asList(testCampaign, campaign2));

        List<Campaign> result = campaignService.getAllCampaigns();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Nike Summer Campaign", result.get(0).getName());
        assertEquals("Adidas Winter Campaign", result.get(1).getName());
        verify(campaignRepository, times(1)).findAll();
    }

    @Test
    void getCampaignById_ShouldReturnCampaign_WhenExists() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(testCampaign));

        Campaign result = campaignService.getCampaignById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Nike Summer Campaign", result.getName());
    }

    @Test
    void getCampaignById_ShouldThrowException_WhenNotFound() {
        when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            campaignService.getCampaignById(99L);
        });

        assertEquals("Campaign not found", exception.getMessage());
    }
}