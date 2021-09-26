package com.apzakharov.telegrammBot.bpmn.service;

import com.apzakharov.telegrammBot.bot.BotContext;
import com.apzakharov.telegrammBot.bot.BotStateBPMN;
import com.apzakharov.telegrammBot.bpmn.bpmProcessImpl.botIncomingMessageProcess.ProcessCommand;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
import com.apzakharov.telegrammBot.model.User;
import com.apzakharov.telegrammBot.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserProcessServiceImpl implements UserProcessService {

    private UserRepository userRepository;
    static final String JSON_TYPE_STRING = "String";
    static final String ProcessURL = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key/process-incoming-message/start";
    private ObjectMapper objectMapper;
    private CamundaProcessService camundaProcessService;

    private static final Logger LOGGER = LogManager.getLogger(ProcessCommand.class);

    @Override
    public void processStart(@Nonnull BotContext contex) throws Exception {

        LOGGER.info("Start New UserProcessAnswer for contex: \n"+ objectMapper.writeValueAsString(contex));

        User user = contex.getUser();
        Long chatID = user.getChatId();
        String input = contex.getInput();

        LOGGER.info("Context for process: \n"+ objectMapper.writeValueAsString(contex));
//        Integer stateID = user.getStateId();

//        BotStateBPMN usersBotState = BotStateBPMN.getByBotStateBPMNID(stateID)
//                .orElseThrow(()-> new Exception(MessageFormat.format("For UserID = {0} not defined BotStateBPMN.", user.getId())));
//        if (!BotStateBPMN.WAIT.equals(usersBotState)) {
//            return;
//        }
        try {

//          Получаем инстанс состояния по ID состояния, записанного у пользователя,
//          после чего у этого инстанса получаем URL соотвествующего bpmn-процесса.


            Map<String, Object> variables =  new HashMap<>();

            variables.put("User", objectMapper.writeValueAsString(user));
            variables.put("ChatID", chatID);
            variables.put("Input", input);

            LOGGER.info("Variabls for process: "+ objectMapper.writeValueAsString(variables));

            ProcessStartRequestBody processBody = new ProcessStartRequestBody();
            processBody.setVariables(variables);

            LOGGER.info("StartProcess for Url: \n"+ ProcessURL+"\n"+" and body: \n"+objectMapper.writeValueAsString(processBody));

            camundaProcessService.processStart(ProcessURL, processBody);

//            user.setStateId(BotStateBPMN.PROCESS.getBotStateBPMNID());
        }
        catch(Exception ex) {

         ex.printStackTrace();
            user.setStateId(BotStateBPMN.ERROR.getBotStateBPMNID());

        }
        user.setUserTimeStart(ZonedDateTime.now());
//        user.setUserTimeStart(ZonedDateTime.now());
//        QIncidentEntity qIncidentEntity = qIncidentEntityMapper.qIncidentToQIncidentEntity(qIncident);
        userRepository.save(user);
    }
}
