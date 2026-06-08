package com.basis.adcampaignplatform;

import com.basis.adcampaignplatform.model.Campaign;
import com.basis.adcampaignplatform.service.CampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = com.basis.adcampaignplatform.controller.CampaignController.class)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void createCampaign_ShouldReturn200_WithCampaignData() throws Exception {
        when(campaignService.createCampaign(any(Campaign.class))).thenReturn(testCampaign);

        mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCampaign)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nike Summer Campaign"))
                .andExpect(jsonPath("$.advertiser").value("Nike"))
                .andExpect(jsonPath("$.budget").value(50000.00))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void getAllCampaigns_ShouldReturn200_WithListOfCampaigns() throws Exception {
        Campaign campaign2 = new Campaign();
        campaign2.setId(2L);
        campaign2.setName("Adidas Winter Campaign");
        campaign2.setAdvertiser("Adidas");
        campaign2.setBudget(30000.00);
        campaign2.setStatus("ACTIVE");

        List<Campaign> campaigns = Arrays.asList(testCampaign, campaign2);
        when(campaignService.getAllCampaigns()).thenReturn(campaigns);

        mockMvc.perform(get("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Nike Summer Campaign"))
                .andExpect(jsonPath("$[1].name").value("Adidas Winter Campaign"));
    }

    @Test
    void getCampaignById_ShouldReturn200_WhenExists() throws Exception {
        when(campaignService.getCampaignById(1L)).thenReturn(testCampaign);

        mockMvc.perform(get("/api/campaigns/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Nike Summer Campaign"));
    }

    @Test
    void getCampaignById_ShouldReturn500_WhenNotFound() throws Exception {
        when(campaignService.getCampaignById(99L))
                .thenThrow(new RuntimeException("Campaign not found"));

        getResultActions();
    }

    private @NonNull ResultActions getResultActions() throws Exception {
        return mockMvc.perform(get("/api/campaigns/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}