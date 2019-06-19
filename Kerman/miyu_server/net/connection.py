# @Author  :_kerman jt
# @Time    : 19-5-28 下午7:36

from queue import *


class Connection(object):
    def __init__(self):
        self.__cliaddr = None
        self.__dataFromCliBuffer = Queue()
        self.__dataToCliBuffer = Queue()
        self.__linkPlayer = None
        self.__preRemain = None
        self.__msgManger = None

    def init_connection(self, cliAddr, msgManger):
        self.__cliAddr = cliAddr
        self.__msgManger = msgManger

    # """ create a player through the data from the database, login call """
    #
    # def CreatePlayer(self, playerId, dataFromRedis):
    #     """ init a player """
    #     self.__linkPlayer = Player()
    #     self.__linkPlayer.SetId(playerId)
    #     """ set the attr with database """
    #     self.__linkPlayer.SetPlayerData(dataFromRedis)

    def DataStreamProcess(self, currCliConn, Weapon,
                          Armor, Special, Lock):
        while not self.__dataFromCliBuffer.empty():
            data = self.__dataFromCliBuffer.get()
            index = 0
            if self._preRemain is not None:
                index = data.find('}')
                remainMsg = self._preRemain + data[:index + 1]
                replyMsg = self._msgProcess(remainMsg, currCliConn,
                                            Weapon, Armor, Special, Lock)
                for msg in replyMsg:
                    self.__dataToCliBuffer.put(msg)
                self._preRemain = None
            msgList = data[index + 1:].split('{')
            for msg in msgList:
                if msg is '':
                    continue
                index = msg.find('}')
                if index is -1:
                    self._preRemain = '{' + msg
                else:
                    replyMsg = self._msgProcess('{' + msg[:index + 1], currCliConn,
                                                Weapon, Armor, Special, Lock)
                    for msg in replyMsg:
                        self.__dataToCliBuffer.put(msg)

    def GetAddr(self):
        return self.__cliAddr

    def GetPlayer(self):
        return self._linkPlayer

    def GetReadBuffer(self):
        return self.__dataFromCliBuffer

    def GetWriteBuffer(self):
        return self.__dataToCliBuffer

    def closeConnection(self):
        if self._linkPlayer is not None:
            DataBaseMgrInit = DataBaseMgr()
            DataBaseMgrInit.SaveStateToReids(self._linkPlayer)
        self.__dataFromCliBuffer.queue.clear()
        self.__dataToCliBuffer.queue.clear()
        self._linkPlayer = None
        self._preRemain = None

    def _msgProcess(self, msg, currCliConn, Weapon,
                    Armor, Special, Lock):
        msgObj = self.__msgManger.Decode(msg)
        if msgObj is None:
            return []
        return self.__msgManger.DistributeMsg(self, msgObj, currCliConn,
                                              Weapon, Armor, Special, Lock)
