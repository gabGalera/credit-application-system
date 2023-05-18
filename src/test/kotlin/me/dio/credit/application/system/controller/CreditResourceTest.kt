package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension

import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.dto.request.CustomerDto

import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.enummeration.Status
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {
    @Autowired private lateinit var creditRepository: CreditRepository
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL: String = "/api/credits"
    }

    @BeforeEach fun setup() = creditRepository.deleteAll()
    @AfterEach fun tearDown() = creditRepository.deleteAll()
    @Test
    fun `should create a credit and return status 201`() {
        //given
        val creditDto: CreditDto = builderCreditDto()
        val customerDto: CustomerDto = builderCustomerDto()
        val valueAsStringCustomer: String = objectMapper.writeValueAsString(customerDto)
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)
        mockMvc.perform(MockMvcRequestBuilders
            .post(CustomerResourceTest.URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsStringCustomer))
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
                .post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsStringCredit))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `should find all customers by id and return 200 status`() {
        //given
        val creditDto: CreditDto = builderCreditDto()
        val customerDto: CustomerDto = builderCustomerDto()
        val valueAsStringCustomer: String = objectMapper.writeValueAsString(customerDto)
        val valueAsStringCredit: String = objectMapper.writeValueAsString(creditDto)
        mockMvc.perform(MockMvcRequestBuilders
            .post(CustomerResourceTest.URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsStringCustomer))
        mockMvc.perform(MockMvcRequestBuilders
            .post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsStringCredit))
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders
            .get("$URL?customerId=1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun builderCreditDto(
        creditValue: BigDecimal = BigDecimal.valueOf(1000000.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(1),
        numberOfInstallments: Int = 3,
        customerId: Long = 1L,
    ) = CreditDto(
        creditValue = creditValue,
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId,
    )

    private fun buildCredit(
        creditCode: UUID = UUID.randomUUID(),
        creditValue: BigDecimal = BigDecimal.valueOf(1000000.0),
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusMonths(1),
        numberOfInstallments: Int = 3,
        customerId: Long = 1L,
        status: Status = Status.IN_PROGRESS,
        firstName: String = "Gabriel",
        lastName: String = "Galera",
        cpf: String = "44444444433",
        email: String = "gabgalera@hotmail.com",
        password: String = "minhaSenha",
        zipCode: String = "88888333",
        street: String = "Rua dos Galeras",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long? = 1L
    ) = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        status = status,
        customer = Customer(
            id = customerId,
            firstName = firstName,
            lastName = lastName,
            cpf = cpf,
            email = email,
            password = password,
            address = Address(
                zipCode = zipCode,
                street = street,
            ),
            income = income,

            ),
        id = id
    )
    private fun buildCustomer(
        firstName: String = "Gabriel",
        lastName: String = "Galera",
        cpf: String = "44444444433",
        email: String = "gabgalera@hotmail.com",
        password: String = "minhaSenha",
        zipCode: String = "88888333",
        street: String = "Rua dos Galeras",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        id: Long = 1L
    ) = Customer(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        address = Address(
            zipCode = zipCode,
            street = street,
        ),
        income = income,
        id = id
    )
    private fun builderCustomerDto(
        firstName: String = "Gabriel",
        lastName: String = "Galera",
        cpf: String = "371.923.854-76",
        income: BigDecimal = BigDecimal.valueOf(1000.0),
        email: String = "gabgalera@hotmail.com",
        password: String = "minhaSenha",
        zipCode: String = "88888333",
        street: String = "Rua dos Galeras",
    ) = CustomerDto(
        firstName = firstName,
        lastName = lastName,
        cpf = cpf,
        email = email,
        password = password,
        zipCode = zipCode,
        street = street,
        income = income,
    )
}