package com.api.challenge.apichallenge.unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.api.challenge.apichallenge.response.v2.ClienteResponseV2;
import com.api.challenge.apichallenge.util.dateutil.BirthdayToDateOfBirth;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BirthdayToDateOfBirthConverterTest {

    @Test
    @Order(1)
    public void convertTestNoBirthdayYet() {
        int dia = 28;
        int mes = 12;
        int idade = 23;

        String formattedDay = String.format("%02d", dia);
        String formattedMonth = String.format("%02d", mes);

        ClienteResponseV2 clienteResponseV2 = new ClienteResponseV2();
        clienteResponseV2.setId(1);
        clienteResponseV2.setDataNascimento(formattedDay + "-" + formattedMonth);
        clienteResponseV2.setSexo("M");
        clienteResponseV2.setIdade(idade);

        clienteResponseV2 = BirthdayToDateOfBirth.convertBirthdayToDateOfBirthV2(clienteResponseV2);

        LocalDate hoje = LocalDate.now();
        LocalDate dataAniversario = LocalDate.of(hoje.getYear(), mes, dia);

        LocalDate dataNascimento = hoje.minusYears(idade).withDayOfYear(LocalDate.of(1, mes, dia).getDayOfYear());

        // caso não tenha feito aniversário nesse ano:
        if (hoje.isBefore(dataAniversario)) {
            dataNascimento = dataNascimento.minusYears(1);
            dataNascimento = dataNascimento.plusDays(1);
        }

        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        System.out.println(idade);
        assertEquals(clienteResponseV2.getDataNascimento(), formatter.format(dataNascimento));
    }

    @Test
    @Order(2)
    public void convertTestAfterBirthdayFromActualYear() {
        int dia = 1;
        int mes = 1;
        int idade = 23;


        LocalDate hoje = LocalDate.now();
        LocalDate dataAniversario = LocalDate.of(hoje.getYear(), mes, dia);
        LocalDate dataNascimento = hoje.minusYears(idade).withDayOfYear(LocalDate.of(1, mes, dia).getDayOfYear());

        String formattedDay = String.format("%02d", dia);
        String formattedMonth = String.format("%02d", mes);

        ClienteResponseV2 clienteResponseV2 = new ClienteResponseV2();
        clienteResponseV2.setId(1);
        clienteResponseV2.setDataNascimento(formattedDay + "-" + formattedMonth);
        clienteResponseV2.setSexo("M");
        clienteResponseV2.setIdade(idade);

        // caso não tenha feito aniversário nesse ano:
        if (hoje.isBefore(dataAniversario)) {
            dataNascimento = dataNascimento.minusYears(1);
            dataNascimento = dataNascimento.plusDays(1);
        }

        Locale LOCALE_BRAZIL = new Locale("pt", "BR");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", LOCALE_BRAZIL);
        clienteResponseV2 = BirthdayToDateOfBirth.convertBirthdayToDateOfBirthV2(clienteResponseV2);
        assertEquals(clienteResponseV2.getDataNascimento(), formatter.format(dataNascimento));
    }
}
