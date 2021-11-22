//package com.apzakharov.telegrammBot.bot;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//public enum  BotStateBPMN {
//
//    REG(0, "Зарегистрирован"){
//        @Override
//        public String getProcessURL() {
//            return null;
//        }
//    },
//    WAIT(1, "Отправить в обработку"){
//        @Override
//        public String getProcessURL() {
//            return null;
//        }
//    },
//    PROCESS(2, "В обработке") {
//        @Override
//        public String getProcessURL() {
//            return null;
//        }
//    },
//    COMPLETE(3, "Обработка завешена") {
//        @Override
//        public String getProcessURL() {
//            return null;
//        }
//    },
//    ERROR(4, "Ошибка обработки") {
//        @Override
//        public String getProcessURL() {
//            return null;
//        }
//    };
//
//    private final Integer BotStateBPMNID;
//    private final String NameBotStateBPMN;
//
//    BotStateBPMN(Integer BotStateBPMNID, String NameBotStateBPMN) {
//        this.BotStateBPMNID = BotStateBPMNID;
//        this.NameBotStateBPMN = NameBotStateBPMN;
//    }
//
//    public Integer getBotStateBPMNID() {
//        return BotStateBPMNID;
//    }
//
//    public String getNameBotStateBPMN() {
//        return NameBotStateBPMN;
//    }
//
//    private static final Map<Integer, BotStateBPMN> idMap;
//    static {
//        idMap = new HashMap<>(values().length);
//        for (BotStateBPMN value: values()) {
//            idMap.put(value.BotStateBPMNID, value);
//        }
//    }
//
//    public static Optional<BotStateBPMN> getByBotStateBPMNID(Integer BotStateBPMNID) {
//        return Optional.ofNullable(idMap.get(BotStateBPMNID));
//    }
//
////  заглушка, переопределяемая в инстансах состояния.
//    public abstract String getProcessURL();
//
//
//
//
//}
