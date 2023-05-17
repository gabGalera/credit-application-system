package me.dio.credit.application.system.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jakarta.persistence.*
import me.dio.credit.application.system.entity.Address
import me.dio.credit.application.system.entity.Credit
import me.dio.credit.application.system.entity.Customer
import me.dio.credit.application.system.enummeration.Status
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.service.impl.CreditService
import me.dio.credit.application.system.service.impl.CustomerService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.collections.List

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceTest {
    @MockK lateinit var creditRepository: CreditRepository
    @MockK lateinit var customerService: CustomerService
    @InjectMockKs lateinit var creditService: CreditService

    @Test
    fun `should create new credit`() {
        //given
        val fakeCustomer: Customer = buildCustomer()
        val fakeCredit: Credit = buildCredit()
        every { customerService.findById(any()) } returns fakeCustomer
        every { creditRepository.save(any()) } returns fakeCredit
        //when
        val actual: Credit = creditService.save(fakeCredit)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify (exactly = 1) { customerService.findById(any()) }
        verify (exactly = 1) { creditRepository.save(fakeCredit) }
    }

    @Test
    fun `should find all customers by id` () {
        //given
        val fakeId: Long = Random().nextLong()
        val fakeCredits: List<Credit> = List(size=1, init = { buildCredit(id = fakeId) })
        every { creditRepository.findAllByCustomerId(fakeId) } returns Optional.of(fakeCredits)
        //when
        val actual: List<Credit> = creditService.findAllByCustomer(fakeId)
        //then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(fakeCredits)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeId) }
    }

    private fun buildCredit(
       creditCode: UUID = UUID.randomUUID(),
       creditValue: BigDecimal = BigDecimal.valueOf(1000000.0),
       dayFirstInstallment: LocalDate = LocalDate.of(2023, 5,17),
       numberOfInstallments: Int = 0,
       status: Status = Status.IN_PROGRESS,
       firstName: String = "Gabriel",
       lastName: String = "Galera",
       cpf: String = "44444444433",
       email: String = "gabgalera@hotmail.com",
       password: String = "minhaSenha",
       zipCode: String = "88888333",
       street: String = "Rua dos Galeras",
       income: BigDecimal = BigDecimal.valueOf(1000.0),
       customerId: Long = 1L,
       id: Long? = 1L
    ) = Credit(
        creditCode = creditCode,
        creditValue = creditValue,
        dayFirstInstallment = dayFirstInstallment,
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
}