package com.vargas.leo.gerenciadorassembleia.service;

import com.vargas.leo.gerenciadorassembleia.controller.request.FinishVotingRequest;
import com.vargas.leo.gerenciadorassembleia.controller.response.FinishVotingResponse;
import com.vargas.leo.gerenciadorassembleia.domain.*;
import com.vargas.leo.gerenciadorassembleia.exception.NotFoundException;
import com.vargas.leo.gerenciadorassembleia.repository.UserRepository;
import com.vargas.leo.gerenciadorassembleia.repository.VotingSessionRepository;
import com.vargas.leo.gerenciadorassembleia.validator.VotingSessionValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinishVotingService {

    private final UserRepository userRepository;
    private final VotingSessionRepository votingSessionRepository;
    private final ModelMapper mapper;
    private final VotingSessionValidator votingSessionValidator;

    public FinishVotingResponse finishVoting(FinishVotingRequest finishVotingRequest) {
        VotingSession votingSession = this.finishVotingAndCountVotes(finishVotingRequest);
        return mapper.map(votingSession, FinishVotingResponse.class);
    }

    protected VotingSession finishVotingAndCountVotes(FinishVotingRequest finishVotingRequest) {
        User user = userRepository.findById(finishVotingRequest.getUserId());
        if (user == null) {
            throw new NotFoundException("user.not.found");
        }

        VotingSession votingSession = votingSessionRepository.findById(finishVotingRequest.getVotingSessionId());
        if (votingSession == null) {
            throw new NotFoundException("voting.session.not.found");
        }

        votingSessionValidator.validateVotingSessionStatus(votingSession);

        this.defineVotingSessionResult(votingSession);
        this.closeVotingSession(votingSession);

        votingSessionRepository.save(votingSession);

        return votingSession;
    }

    private void closeVotingSession(VotingSession votingSession) {
        votingSession.setStatus(VotingSessionStatus.closed);
        this.closeAgenda(votingSession.getAgenda());
    }

    private void closeAgenda(Agenda agenda) {
        agenda.setStatus(AgendaStatus.closed);
    }

    private void defineVotingSessionResult(VotingSession votingSession) {
        if (votingSession.getYesVotes() > votingSession.getNoVotes()) {
            votingSession.setWinnerOption(VotingResult.yes);
            votingSession.setLooserOption(VotingResult.no);
        } else if (votingSession.getNoVotes() > votingSession.getYesVotes()) {
            votingSession.setWinnerOption(VotingResult.no);
            votingSession.setLooserOption(VotingResult.yes);
        } else {
            votingSession.setWinnerOption(VotingResult.draw);
            votingSession.setLooserOption(VotingResult.draw);
        }
    }

}
