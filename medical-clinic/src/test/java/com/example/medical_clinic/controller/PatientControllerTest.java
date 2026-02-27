package com.example.medical_clinic.controller;

import com.example.medical_clinic.DTO.ChangePasswordCommand;
import com.example.medical_clinic.DTO.patient.PatientCreateRequest;
import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import com.example.medical_clinic.exception.patient.PatientNotFoundException;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    PatientService patientService;
    @MockitoBean
    PatientMapper patientMapper;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getAll_PatientsExist_ReturnsListAndStatusOk() throws Exception {
        when(patientService.getAll(any())).thenReturn(List.of(
                new PatientDto(1L, "abc@gmail.com", "123", "Kamil", "Bulanda", "111222333", null),
                new PatientDto(2L, "abc@gmail.com", "123", "Maciek", "Nowak", "111222333", null)
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Kamil"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Maciek"));
    }

    @Test
    void add_ValidPatientRequest_ReturnsCreatedPatientAndStatusCreated() throws Exception {
        PatientCreateRequest request = new PatientCreateRequest("abc@gmail.com", "passw", "123", "Kamil", "Bulanda", "111222333", null);
        Patient savedPatient = new Patient(1L, request.email(), request.password(), request.idCardNo(), request.phoneNumber(), request.birthday(), null, null, null);
        PatientDto expectedDto = new PatientDto(1L, "abc@gmail.com", "123", "Kamil", "Bulanda", "111222333", null);

        when(patientService.add(any(PatientCreateRequest.class))).thenReturn(savedPatient);
        when(patientMapper.patientToDto(any(Patient.class))).thenReturn(expectedDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/patients")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("abc@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Kamil"));
    }

    @Test
    void getByEmail_PatientExists_ReturnsPatientDtoAndStatusOk() throws Exception {
        String email = "abc@gmail.com";
        PatientDto dto = new PatientDto(1L, email, "123", "Kamil", "Bulanda", "111222333", null);
        Patient patientModel = new Patient();
        patientModel.setEmail(email);

        when(patientService.getByEmail(email)).thenReturn(patientModel);
        when(patientMapper.patientToDto(patientModel)).thenReturn(dto);

        mockMvc.perform(MockMvcRequestBuilders.get("/patients/" + email))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Kamil"));
    }

    @Test
    void getByEmail_PatientNotExists_ReturnExceptionAndStatus404() throws Exception {
        String email = "nonexisting@mail.com";
        when(patientService.getByEmail(email)).thenThrow(new PatientNotFoundException("Patient with email: " + email + "not found"));
        mockMvc.perform(MockMvcRequestBuilders.get("/patients/" + email))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeByEmail_EmailExists_ReturnsStatusNoContent() throws Exception {
        String email = "abc@gmail.com";

        mockMvc.perform(MockMvcRequestBuilders.delete("/patients/" + email))
                .andExpect(status().isNoContent());

        verify(patientService).removeByEmail(email);
    }

    @Test
    void updateByEmail_ValidRequest_ReturnsUpdatedPatientDtoAndStatusOk() throws Exception {
        String email = "abc@gmail.com";
        PatientUpdateRequest updateRequest = new PatientUpdateRequest(email, "NoweImie", "NoweNazwisko", "999888777", null);
        Patient patient = new Patient();
        PatientDto expectedDto = new PatientDto(1L, email, "123", "NoweImie", "NoweNazwisko", "999888777", null);

        when(patientService.updateByEmail(eq(email), any())).thenReturn(patient);
        when(patientMapper.patientToDto(patient)).thenReturn(expectedDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/patients/" + email)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("NoweImie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("999888777"));
    }

    @Test
    void updatePassword_ValidCommand_ReturnsStatusOk() throws Exception {
        String email = "abc@gmail.com";
        ChangePasswordCommand command = new ChangePasswordCommand("noweHaslo123");

        mockMvc.perform(MockMvcRequestBuilders.patch("/patients/" + email)
                        .content(objectMapper.writeValueAsString(command))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService).updatePassword(email, "noweHaslo123");
    }
}