package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.PageResponse;
import com.example.medical_clinic.DTO.doctor.DoctorCreateRequest;
import com.example.medical_clinic.DTO.doctor.DoctorDto;
import com.example.medical_clinic.DTO.doctor.DoctorUpdateRequest;
import com.example.medical_clinic.mapper.DoctorMapper;
import com.example.medical_clinic.model.Doctor;
import com.example.medical_clinic.services.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private DoctorMapper doctorMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAll_ShouldReturnPageResponse() throws Exception {
        DoctorDto dto = new DoctorDto(1L, "Caridology", "abc@gmail.com", "Jan", "Kowalski");
        PageResponse<DoctorDto> pageResponse = new PageResponse<>(
                List.of(dto), 0, 10, 1L, 1
        );

        when(doctorService.getAll(any())).thenReturn(pageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value("abc@gmail.com"))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void testGetByEmail_ShouldReturnDoctor() throws Exception {
        String email = "doctor@med.com";
        Doctor doctor = new Doctor();
        doctor.setEmail(email);
        DoctorDto dto = new DoctorDto(1L, "Neurology",email, "Adam", "Nowak");

        when(doctorService.getByEmail(email)).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.specialization").value("Neurology"));
    }

    @Test
    void testPost_ShouldCreateDoctor() throws Exception {
        DoctorCreateRequest request = new DoctorCreateRequest(
                "Kamil", "Bulanda", "abc@gmail.com", "passwd", "Neurology"
        );
        Doctor savedDoctor = new Doctor();
        DoctorDto expectedDto = new DoctorDto(1L, "Neurology", "abc@gmail.com", "Kamil", "Bulanda");

        when(doctorService.add(any(DoctorCreateRequest.class))).thenReturn(savedDoctor);
        when(doctorMapper.toDto(savedDoctor)).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("abc@gmail.com"))
                .andExpect(jsonPath("$.specialization").value("Neurology"));
    }

    @Test
    void testDelete_ShouldReturnNoContent() throws Exception {
        String email = "delete@med.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/" + email))
                .andExpect(status().isNoContent());

        verify(doctorService).removeByEmail(email);
    }

    @Test
    void testUpdate_ShouldReturnUpdatedDoctor() throws Exception {
        String email = "abc@gmail.com";
        DoctorUpdateRequest updateRequest = new DoctorUpdateRequest(
                "Proctology", "abc@gmail.com", "Kamil", "Bulanda"
        );
        Doctor updatedDoctor = new Doctor();
        DoctorDto expectedDto = new DoctorDto(1L, "Neurology", "abc@gmail.com", "Marek", "Witkowski");

        when(doctorService.updateByEmail(eq(email), any())).thenReturn(updatedDoctor);
        when(doctorMapper.toDto(updatedDoctor)).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/" + email)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.specialization").value("Neurology"))
                .andExpect(jsonPath("$.email").value("abc@gmail.com"));
    }
}