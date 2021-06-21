package com.vargas.leo.gerenciadorassembleia.controller;

import com.vargas.leo.gerenciadorassembleia.controller.request.CreateVotingSessionRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateAgendaResponse;
import com.vargas.leo.gerenciadorassembleia.controller.request.CreateAgendaRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.CreateVotingSessionResponse;
import com.vargas.leo.gerenciadorassembleia.service.CreateAgendaService;
import com.vargas.leo.gerenciadorassembleia.service.CreateVotingSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final CreateAgendaService createAgendaService;
    private final CreateVotingSessionService createVotingSessionService;

    @PostMapping("create/agenda")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateAgendaResponse createPauta(@RequestBody @Valid CreateAgendaRequest agendaRequest) {
        return createAgendaService.create(agendaRequest);
    }

    @PostMapping("create/voting-session")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CreateVotingSessionResponse createVotingSession(@RequestBody @Valid CreateVotingSessionRequest votingSessionRequest) {
        return createVotingSessionService.createVotingSession(votingSessionRequest);
    }
}