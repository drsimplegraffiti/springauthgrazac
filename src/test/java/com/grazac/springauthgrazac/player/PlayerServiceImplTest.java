package com.grazac.springauthgrazac.player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("player test")
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerServiceImpl playerService;



    @Nested
    @DisplayName("grouped create player test")
    class CreatePlayerTest{


        @Test
        @DisplayName("should create a new player")
        void shouldCreatePlayerSuccessfully(){

            // arrange or given  : setup
//             setting object from client
            PlayerRequest request = new PlayerRequest();
            request.setEmail("e@b.c");
            request.setName("yomi");


            // mapping dto to entity
            Player mappedPlayer = Player.builder()
                    .name("yomi")
                    .email("e@b.c")
                    .build();

            Player savedPlayer = Player.builder()
                    .id(1L)
                    .name("yomi")
                    .email("e@b.c")
                    .build();

            PlayerResponse expectedResponse = PlayerResponse.builder()
                    .id(1L)
                    .name("yomi")
                    .email("e@b.c")
                    .build();
//
            // act or when
            // MOCK MAPPER BEHEVOIR
            when(playerMapper.toEntity(request)).thenReturn(mappedPlayer);  // maunual test , mamual conversion
            // MOCK REPOSITORY
            when(playerRepository.save(mappedPlayer)).thenReturn(savedPlayer);
            // mock back to response
            when(playerMapper.toResponse(savedPlayer)).thenReturn(expectedResponse);

            PlayerResponse actualResponse = playerService.createPlayer(request);
            // assert or verify

            assertNotNull(actualResponse);
            assertEquals(1L, actualResponse.getId());
            assertEquals(request.getName(), actualResponse.getName());

            // todo: speaking to db
            assertTrue(playerRepository.findById(actualResponse.getId()).isEmpty());


            verify(playerMapper, times(1)).toEntity(request
            );

        }

    }





}