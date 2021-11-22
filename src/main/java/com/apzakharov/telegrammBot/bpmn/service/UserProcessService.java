//package com.apzakharov.telegrammBot.bpmn.service;
//
//import com.apzakharov.telegrammBot.bot.BotContext;
//import com.apzakharov.telegrammBot.bpmn.dto.ProcessStartRequestBody;
//import com.apzakharov.telegrammBot.bpmn.dto.ProcessVariable;
//import com.apzakharov.telegrammBot.model.User;
//import com.apzakharov.telegrammBot.repo.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Nonnull;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class UserProcessService {
//    private static final Logger LOGGER = LogManager.getLogger(UserProcessService.class);
//
//    private static final String JSON_TYPE_STRING = "String";
//    private static final String ProcessURL = "http://telegramm-bot-bpm.herokuapp.com/engine-rest/process-definition/key/process-incoming-message/start";
//
////    private final ObjectMapper objectMapper;
//    private final CamundaClient camundaClient;
//    private final UserRepository userRepository;
//
//    public void processStart(@Nonnull BotContext contex) throws Exception {
//
//        LOGGER.info("Start New UserProcessAnswer for contex: \n"+ contex.toString());
//
//        User user = contex.getUser();
//        Long chatID = user.getChatId();
//        String input = contex.getInput();
//
//        LOGGER.info("Context for process: \n"+ contex.toString());
////        Integer stateID = user.getStateId();
//
////        BotStateBPMN usersBotState = BotStateBPMN.getByBotStateBPMNID(stateID)
////                .orElseThrow(()-> new Exception(MessageFormat.format("For UserID = {0} not defined BotStateBPMN.", user.getId())));
////        if (!BotStateBPMN.WAIT.equals(usersBotState)) {
////            return;
////        }
//        try {
//
////          Получаем инстанс состояния по ID состояния, записанного у пользователя,
////          после чего у этого инстанса получаем URL соотвествующего bpmn-процесса.
//
//
//            Map<String, ProcessVariable> variables =  new HashMap<>();
//
//            variables.put("User",
//                    new ProcessVariable(JSON_TYPE_STRING,user.toString()));
//            variables.put("ChatID",
//                    new ProcessVariable(JSON_TYPE_STRING,chatID.toString()));
//            variables.put("Input",
//                    new ProcessVariable(JSON_TYPE_STRING,input));
//
//            LOGGER.info("Variabls for process: "+ variables.toString());
//
//            ProcessStartRequestBody processBody = new ProcessStartRequestBody();
//            processBody.setVariables(variables);
//
//            LOGGER.info("StartProcess for Url: \n"+ ProcessURL+"\n"+" and body: \n"+processBody.toString());
//
//            camundaClient.processStart(ProcessURL, processBody);
//
////            user.setStateId(BotStateBPMN.PROCESS.getBotStateBPMNID());
//        }
//        catch(Exception ex) {
//
//         ex.getLocalizedMessage();
////            user.setStateId(BotStateBPMN.ERROR.getBotStateBPMNID());
//
//        }
////        user.setUserTimeStart(ZonedDateTime.now());
////        user.setUserTimeStart(ZonedDateTime.now());
////        QIncidentEntity qIncidentEntity = qIncidentEntityMapper.qIncidentToQIncidentEntity(qIncident);
//        userRepository.save(user);
//    }
//
//
//}
