package com.example.takapay;


import com.example.takapay.entity.User;
import com.example.takapay.repository.TransectionRepository;
import com.example.takapay.repository.UserRepository;
import com.example.takapay.service.impl.TransectionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TransectionRepository transectionRepository;

    @InjectMocks
    private TransectionServiceImpl transectionService;

    private User createUser(Long id, String name, BigDecimal balance) {
        return User.builder().id(id).username(name).balance(balance).build();
    }

    @Test
    public void transferMoney_Success() {
        User sender = createUser(1L, "Hasan", new BigDecimal("1000"));
        User receiver = createUser(2L, "Mahmudul", new BigDecimal("500"));

        when(userRepository.findUserByUsername("Hasan")).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(receiver));

        transectionService.transferMoney(sender.getUsername(), receiver.getId(), new BigDecimal("100"));

        verify(userRepository, times(1)).save(sender);
        verify(userRepository, times(1)).save(receiver);


    }

    @Test
    public void transferMoney_Fails_InsufficientBalance() {

        User sender = createUser(1L, "Hasan", new BigDecimal("50"));
        User receiver = createUser(2L, "Mahmudul", new BigDecimal("500"));


        when(userRepository.findUserByUsername("Hasan")).thenReturn(Optional.of(sender));
        when(userRepository.findById(2L)).thenReturn(Optional.of(sender));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transectionService.transferMoney(sender.getUsername(), receiver.getId(), new BigDecimal("100"));
        });
        assertEquals("Insufficient balance", exception.getMessage());

        assertEquals(new BigDecimal("50"), sender.getBalance());
        verify(userRepository, never()).save(any());

    }


    @Test
    public void transferMoney_Fails_AmountMustBePositive() {
        User sender = createUser(1L, "Hasan", new BigDecimal("50"));
        User receiver = createUser(2L, "Mahmudul", new BigDecimal("500"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transectionService.transferMoney(sender.getUsername(), receiver.getId(), new BigDecimal("-100"));
        });

        assertEquals("Amount must be positive", exception.getMessage());
        assertEquals((new BigDecimal("50")), sender.getBalance());

        verify(userRepository, never()).save(any());
    }
}
