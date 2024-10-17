package com.assessment.springboot.controller;

import com.assessment.springboot.dto.ItemDto;
import com.assessment.springboot.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private RestTemplateBuilder restTemplateBuilder; // Mock the RestTemplateBuilder

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Make the RestTemplateBuilder return the mock RestTemplate
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
    }

    @Test
    public void testGetItems() throws Exception {
        ItemDto item = new ItemDto();
        item.setName("itemName");
        when(itemService.getAllItems(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(item)));
        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").exists());
    }

    @Test
    public void testGetItemById() throws Exception {
        ItemDto item = new ItemDto();
        item.setName("Item 1");
        when(itemService.getByID(any())).thenReturn(item);

        mockMvc.perform(get("/api/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item 1"));
    }

    @Test
    public void testCreateItem() throws Exception {
        ItemDto item = new ItemDto();
        item.setName("Item 1");
        when(itemService.saveItem(any(ItemDto.class))).thenReturn(item);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Item 1\", \"description\": \"Desc\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Item 1"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        ItemDto item = new ItemDto();
        item.setName("Item 1");
        when(itemService.updateItem(any(), any())).thenReturn(item);

        mockMvc.perform(put("/api/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Item 1\", \"description\": \"Desc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Item 1"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(any());

        mockMvc.perform(delete("/api/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetItemsByCategoryName() throws Exception {
        ItemDto item = new ItemDto();
        item.setName("itemName");
        when(itemService.getItemsByCategory(any(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of(item)));
        mockMvc.perform(get("/api/items/search/byCategory/abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").exists());
    }

    @Test
    public void testGoogleResult() throws Exception {
        String googleResult = "ok google";
        when(itemService.callExternalAPI()).thenReturn(googleResult);

        mockMvc.perform(get("/api/items/google"))
                .andExpect(status().isOk())
                .andExpect(content().string(googleResult));
    }

}
