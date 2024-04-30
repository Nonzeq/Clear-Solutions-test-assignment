package com.kobylchak.clearsolutions.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kobylchak.clearsolutions.dto.CreateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UpdateUserRequestDto;
import com.kobylchak.clearsolutions.dto.UserResponseDto;
import com.kobylchak.clearsolutions.exception.handler.ErrorData;
import com.kobylchak.clearsolutions.exception.handler.FieldErrorData;
import java.time.LocalDate;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.now().minusYears(20);
    private static final LocalDate INVALID_BIRTH_DATE = LocalDate.now().minusYears(16);
    protected static MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper mapper;
    
    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                          .webAppContextSetup(webApplicationContext)
                          .build();
    }
    
    @Test
    @SneakyThrows
    @Sql(
            scripts = "classpath:database/users/delete-user-after-creating.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void testCreateUser_WithValidData_ShouldReturn_Created() {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setEmail("test@test.com");
        createUserRequestDto.setFirstName("Test");
        createUserRequestDto.setLastName("Test");
        createUserRequestDto.setBirthDate(VALID_BIRTH_DATE);
        
        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setEmail(createUserRequestDto.getEmail());
        expectedResponse.setFirstName(createUserRequestDto.getFirstName());
        expectedResponse.setLastName(createUserRequestDto.getLastName());
        expectedResponse.setBirthDate(createUserRequestDto.getBirthDate());
        
        String jsonRequest = mapper.writeValueAsString(createUserRequestDto);
        
        MvcResult mvcResult = mockMvc.perform(
                                             post("/api/v1/users")
                                                     .content(jsonRequest)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                             )
                                     .andExpect(status().isCreated())
                                     .andReturn();
        
        JsonNode jsonNode = mapper.readTree(mvcResult.getResponse()
                                                     .getContentAsString());
        UserResponseDto actualResponse = mapper.readValue(jsonNode.get("data").get(0).toString(),
                                                       UserResponseDto.class);
        assertNotNull(actualResponse);
        assertNotNull(actualResponse.getId());
        EqualsBuilder.reflectionEquals(expectedResponse, actualResponse, "id");
    }
    
    @Test
    @SneakyThrows
    void testCreateUser_WithInvalidBirthDate_ShouldReturn_BadRequest() {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setEmail("test@test.com");
        createUserRequestDto.setFirstName("Test");
        createUserRequestDto.setLastName("Test");
        createUserRequestDto.setBirthDate(INVALID_BIRTH_DATE);
        
        FieldErrorData expectedResponse = new FieldErrorData();
        expectedResponse.setField("birthDate");
        expectedResponse.setMessage("You should be an adult");
        expectedResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        
        String jsonRequest = mapper.writeValueAsString(createUserRequestDto);
        
        MvcResult mvcResult = mockMvc.perform(
                                             post("/api/v1/users")
                                                     .content(jsonRequest)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                             )
                                     .andExpect(status().isBadRequest())
                                     .andReturn();
        
        JsonNode jsonNode = mapper.readTree(mvcResult.getResponse()
                                                     .getContentAsString());
        FieldErrorData actualResponse = mapper.readValue(jsonNode.get("errors")
                                                       .get(0)
                                                       .toString(), FieldErrorData.class);
        assertNotNull(actualResponse);
        EqualsBuilder.reflectionEquals(
                expectedResponse,
                actualResponse,
                "cause", "timestamp");
    }
    
    @Test
    @SneakyThrows
    @Sql(
            scripts = "classpath:database/users/delete-three-users-after-creating.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/insert-three-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testSearchUsersByBirth_WithValidParams_ShouldReturn_OK() {
        int expectedSize = 2;
        MvcResult mvcResult = mockMvc.perform(
                                             get("/api/v1/users/search-by-birth")
                                                     .param("fromDate", "2000-01-01")
                                                     .param("toDate", "2003-01-01")
                                                     
                                             )
                                     .andExpect(status().isOk())
                                     .andReturn();
        
        JsonNode jsonNode = mapper.readTree(mvcResult.getResponse()
                                                     .getContentAsString());
        List<UserResponseDto> actualResponse = mapper.readValue(
                jsonNode.get("data").toString(),
                new TypeReference<>() {});
        
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertEquals(expectedSize, actualResponse.size());
    }
    
    @Test
    @SneakyThrows
    void testSearchUsersByBirth_WithInValidParams_ShouldReturn_BadRequest() {
        MvcResult mvcResult = mockMvc.perform(
                                             get("/api/v1/users/search-by-birth")
                                                     .param("fromDate", "2005-01-01")
                                                     .param("toDate", "2003-01-01")
                                             )
                                     .andExpect(status().isBadRequest())
                                     .andReturn();
        
        JsonNode jsonNode = mapper.readTree(mvcResult.getResponse()
                                                     .getContentAsString());
        List<ErrorData> actualResponse = mapper.readValue(
                jsonNode.get("errors").toString(),
                new TypeReference<>() {});
        String expectedMessage = "Invalid date range";
        assertEquals(1, actualResponse.size());
        assertNotNull(actualResponse);
        assertEquals(expectedMessage, actualResponse.get(0).getMessage());
    }
    
    @Test
    @SneakyThrows
    @Sql(
            scripts = "classpath:database/users/delete-three-users-after-creating.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/users/insert-three-users.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void testUpdateUserFields_WithValidData_ShouldReturn_OK() {
        MvcResult mvcResultGetAll = mockMvc.perform(
                                             get("/api/v1/users"))
                                     .andExpect(status().isOk())
                                     .andReturn();
        JsonNode node = mapper.readTree(mvcResultGetAll.getResponse()
                                                     .getContentAsString());
        UserResponseDto availableUser = mapper.readValue(node.get("data").get(0).toString(),
                                                          UserResponseDto.class);
        
        Long userId = availableUser.getId();
        
        UpdateUserRequestDto createUserRequestDto = new UpdateUserRequestDto();
        createUserRequestDto.setFirstName("UpdatedFirstName");
        createUserRequestDto.setLastName("UpdatedLastName");
        
        String expectedFirstName = createUserRequestDto.getFirstName();
        String expectedLastName = createUserRequestDto.getLastName();
        
        String jsonRequest = mapper.writeValueAsString(createUserRequestDto);
        
        MvcResult mvcResult = mockMvc.perform(
                                             patch("/api/v1/users/" + userId)
                                                     .content(jsonRequest)
                                                     .contentType(MediaType.APPLICATION_JSON)
                                             )
                                     .andExpect(status().isOk())
                                     .andReturn();
        
        JsonNode jsonNode = mapper.readTree(mvcResult.getResponse()
                                                     .getContentAsString());
        UserResponseDto actualResponse = mapper.readValue(jsonNode.get("data").get(0).toString(),
                                                          UserResponseDto.class);
        assertNotNull(actualResponse);
        assertNotNull(actualResponse.getId());
        assertEquals(userId, actualResponse.getId());
        assertEquals(expectedFirstName, actualResponse.getFirstName());
        assertEquals(expectedLastName, actualResponse.getLastName());
    }
}