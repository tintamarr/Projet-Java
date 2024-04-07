package com.ensta.rentmanager;

import com.epf.rentmanager.dao.ClientDao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import exception.DaoException;
import exception.ServiceException;
import com.epf.rentmanager.service.ClientService;
import model.Client;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;


@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientDao clientDao;

    @Test
    void findAll_should_fail_when_dao_throws_exception() throws DaoException {
        when(this.clientDao.findAll()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> clientService.findAll());
    }
    @Test
    void clientMajeur_should_return_false_when_client_is_below_18() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(17);
        boolean result = clientService.clientMajeur(dateOfBirth);
        assertFalse("Le client n'est pas majeur.", result);
    }
    @Test
    void clientMajeur_should_return_true_when_client_is_18_or_above() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(18);
        boolean result = clientService.clientMajeur(dateOfBirth);
        assertTrue("The client should be considered major",result);
    }

    @Test
    public void testCreateWithShortNameOrSurname() {
        Client client = new Client();
        client.setNom("Ab");
        client.setPrenom("Cd");
        client.setNaissance(LocalDate.of(1990, 1, 1));
        client.setEmail("abc@example.com");
        assertThrows(ServiceException.class, () -> clientService.create(client));
    }

}