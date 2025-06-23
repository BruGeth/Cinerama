package com.cinerama.backend.service.impl;

import com.cinerama.backend.entity.Showtime;
import com.cinerama.backend.repository.ShowtimeRepository;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ShowtimeServiceImplTest {
    @Test
    void getAllShowtimes_returnsAllShowtimes() {
        ShowtimeRepository mockRepo = mock(ShowtimeRepository.class);
        Showtime showtime1 = new Showtime();
        Showtime showtime2 = new Showtime();
        when(mockRepo.findAll()).thenReturn(Arrays.asList(showtime1, showtime2));

        ShowtimeServiceImpl service = new ShowtimeServiceImpl(mockRepo);

        List<Showtime> result = service.getAllShowtimes();

        assertEquals(2, result.size());
        verify(mockRepo, times(1)).findAll();
    }
}