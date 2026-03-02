package com.example.medical_clinic.service;

import com.example.medical_clinic.DTO.patient.PatientCreateRequest;
import com.example.medical_clinic.DTO.patient.PatientDto;
import com.example.medical_clinic.DTO.patient.PatientUpdateRequest;
import com.example.medical_clinic.exception.patient.PatientAlreadyExistsException;
import com.example.medical_clinic.exception.patient.PatientNotFoundException;
import com.example.medical_clinic.mapper.PatientMapper;
import com.example.medical_clinic.model.Patient;
import com.example.medical_clinic.model.User;
import com.example.medical_clinic.repository.PatientRepository;
import com.example.medical_clinic.services.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatientServiceTest {
    PatientService patientService;
    PatientRepository patientRepository;
    PatientMapper patientMapper;

    @BeforeEach
    void setup() {
        this.patientMapper = Mappers.getMapper(PatientMapper.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.patientService = new PatientService(patientRepository, patientMapper);
    }

    @Test
    void getAll_dataCorrect_ReturnListOfPatientDto() {
        //given
        Patient patient1 = new Patient(1L, "email@com", "strongPassword", "123qwer", "999", LocalDate.of(2002, 11, 23),
                null, null, Collections.emptySet());
        Patient patient2 = new Patient(2L, "gmail@com", "strongPassword", "456zaq", "666555444", LocalDate.of(2001, 8, 21),
                null, null, Collections.emptySet());

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Patient> patients = List.of(patient1, patient2);
        Page<Patient> patientPage = new PageImpl<>(patients);
        when(patientRepository.findAll(pageRequest)).thenReturn(patientPage);

        //when
        List<PatientDto> result = patientService.getAll(pageRequest);

        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals(2, result.size()),
                () -> Assertions.assertEquals("email@com", result.get(0).email()),
                () -> Assertions.assertEquals("123qwer", result.get(0).idCardNo()),
                () -> Assertions.assertEquals("999", result.get(0).phoneNumber()),
                () -> Assertions.assertNull(result.get(0).firstName()),
                () -> Assertions.assertNull(result.get(0).lastName())
        );
    }

    @Test
    void getAll_NoData_ReturnEmptyList() {
        //given
        PageRequest pageable = PageRequest.of(0, 10);
        when(patientRepository.findAll(pageable)).thenReturn(Page.empty());
        //when
        List<PatientDto> result = patientService.getAll(pageable);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result, "list should not be null"),
                () -> Assertions.assertTrue(result.isEmpty(), "list should be empty"),
                () -> Assertions.assertEquals(0, result.size())
        );

    }

    @Test
    void getByEmail_DataCorrect_ReturnPatient() {
        //given
        String email = "jan@gmail.com";
        Patient patient1 = new Patient(1L, email, "strongPassword", "123qwer", "999", LocalDate.of(2002, 11, 23),
                null, null, Collections.emptySet());
        when(patientRepository.getByEmail(email)).thenReturn(Optional.of(patient1));
        //when
        Patient result = patientService.getByEmail(email);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result, "should return one patient"),
                () -> Assertions.assertEquals(1L, result.getId()),
                () -> Assertions.assertEquals("123qwer", result.getIdCardNo())
        );
    }

    @Test
    void getByEmail_NonExistingPatient_ThrowException() {
        //given
        String email = "nonexist@gmail.com";
        when(patientRepository.getByEmail(email)).thenReturn(Optional.empty());
        //when then
        Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.getByEmail(email);
        });
    }

    @Test
    void add_DataCorrect_ReturnPatient() {
        //given
        PatientCreateRequest request = new PatientCreateRequest("jan@email.com", "password", "zaq1", "Jan", "Kowalski", "123456789",
                LocalDate.of(2003, 2, 22));
        when(patientRepository.existsByEmail(request.email())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> {
            Patient p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });
        //when
        Patient result = patientService.add(request);
        //then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertEquals("jan@email.com", result.getEmail()),
                () -> Assertions.assertEquals(1L, result.getId()),
                () -> Assertions.assertEquals(request.email(), result.getEmail())
        );
    }

    @Test
    void add_AlreadyExists_ThrowError() {
        //given
        PatientCreateRequest request = new PatientCreateRequest("jan@email.com", "password", "zaq1", "Jan", "Kowalski", "123456789",
                LocalDate.of(2003, 2, 22)
        );
        when(patientRepository.existsByEmail(request.email())).thenReturn(true);
        Assertions.assertThrows(PatientAlreadyExistsException.class, () -> {
            patientService.add(request);
        });
    }

    @Test
    void removeByEmail_WhenDataCorrect_thenPatientIsDeleted() {
        //given
        String email = "jan@email.com";
        when(patientRepository.existsByEmail(email)).thenReturn(true);
        //when
        patientService.removeByEmail(email);
        //then
        verify(patientRepository, times(1)).deleteByEmail(email);
    }

    @Test
    void removeByEmail_NonExistingMail_thenThrowException() {
        //given
        String email = "notexisting@email.com";
        when(patientRepository.existsByEmail(email)).thenReturn(false);
        //when then
        PatientNotFoundException patientNotFoundException = Assertions.assertThrows(PatientNotFoundException.class, () -> {
            patientService.removeByEmail(email);
        });

        verify(patientRepository, never()).deleteByEmail(anyString());
        Assertions.assertEquals("not found patient with: " + email, patientNotFoundException.getMessage());
//        verifyNoMoreInteractions(patientRepository);
    }

    @Test
    void updateByEmail_WhenEmailExists_ThenReturnPatient() {
        // given
        String email = "jan@email.com";
        PatientUpdateRequest request = new PatientUpdateRequest(
                email, "Adam", "Nowak", "555666777", LocalDate.of(1990, 5, 10)
        );

        User existingUser = new User();
        existingUser.setFirstName("Jan");
        existingUser.setLastName("Kowalski");

        Patient existingPatient = new Patient();
        existingPatient.setEmail(email);
        existingPatient.setUser(existingUser);
        existingPatient.setPhoneNumber("111222333");

        when(patientRepository.getByEmail(email)).thenReturn(Optional.of(existingPatient));
        // when
        Patient result = patientService.updateByEmail(email, request);
        // then
        Assertions.assertAll(
                () -> Assertions.assertNotNull(result),
                () -> Assertions.assertNotNull(result.getUser(), "User should be visible"),
                () -> Assertions.assertEquals(request.phoneNumber(), result.getPhoneNumber()),
                () -> Assertions.assertEquals(request.birthday(), result.getBirthday()),
                () -> Assertions.assertEquals(request.firstName(), result.getUser().getFirstName()),
                () -> Assertions.assertEquals(request.lastName(), result.getUser().getLastName())
        );
    }

    @Test
    void updatePassword_WhenPatientExists() {
        // given
        String email = "jan@email.com";
        String newPassword = "newStrongPassword123";

        Patient existingPatient = new Patient();
        existingPatient.setEmail(email);
        existingPatient.setPassword("oldPassword");

        when(patientRepository.getByEmail(email)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(existingPatient);

        // when
        patientService.updatePassword(email, newPassword);
        // then
        verify(patientRepository).save(argThat(new PatientPasswordMatcher(email, newPassword)));
    }

    public static class PatientPasswordMatcher implements ArgumentMatcher<Patient> {
        private final String expectedEmail;
        private final String expectedPassword;

        public PatientPasswordMatcher(String expectedEmail, String expectedPassword) {
            this.expectedEmail = expectedEmail;
            this.expectedPassword = expectedPassword;
        }

        @Override
        public boolean matches(Patient patient) {
            if (patient == null) return false;

            return expectedEmail.equals(patient.getEmail()) &&
                    expectedPassword.equals(patient.getPassword());
        }
    }
}
