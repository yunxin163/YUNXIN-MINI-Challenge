# @Author  :_kerman jt
# @Time    : 19-5-28 下午7:13

from socket import *
from select import *
from threading import Lock

from net.connection import Connection


class ServerNet(object):
    def __init__(self):
        self.__port = 0  # init value
        self.__maxBackLog = 15  # max backlog
        self.__maxConn = 50  # the max number of connections of client
        self.__inUseConnList = []  # save in use Conn
        self.__noUseConnList = []  # save no use Conn
        self.__cliSockToConn = {}  # dict to maintain the relationship between client socket with connection
        self.__idToConn = {}  # dict to maintain the relationship between id and the conn for online client
        """ The instance of MsgManger, including the analysis and distribute of message """
        # self.__msgManger = MsgManger()
        """ the instance of ServerEvent, including all server active message """
        # self.__serverEvent = ServerEvent()
        """武器"""
        self.__weapon = {}
        """防具"""
        self.__armor = {}
        """特殊道具"""
        self.__special = {}

        # 初始化一个锁，用于保证更新道具时的一致性
        self.__lock = Lock()

    """ Start the Game Server """

    def StartServer(self) -> None:
        """ init the link pool """
        self._initLinkPool()

        """ 由客户端的地形读取信息 """
        # self.__HeapAstar.read_map('GameServer/PathFinding.txt')

        """ create a new socket """
        listenfd = socket(AF_INET, SOCK_STREAM)
        """ no blocking """
        listenfd.setblocking(False)

        """ bind socket with addr """
        # 1. '' represents any available ip address in local
        # 2. 0 represents any available port in local
        # listenfd.bind(('', 0))
        listenfd.bind(('', 55445))
        self.__port = listenfd.getsockname()[1]

        """ make the  socket to be listen socket  and specify the backlog of this socket """
        listenfd.listen(self.__maxBackLog)

        readList = [listenfd, ]
        writeList = []

        print('Start server, waiting for connection...')
        print('The port in server is ', self.__port)

        # """ Timing task launch for all client, it will create a new thread """
        # self.__serverEvent.CreateTimingTaskThread(self.__msgManger, self.__idToConn,
        #                                           self.__EnemyDict, self.__weapon, self.__ammo, self.__firstAid,
        #                                           self.__lock)
        #
        # """ 机器人AI使用另一个线程实现 """
        # self.__RebotAi.CreateRobotAiTaskThread(self.__msgManger, self.__idToConn,
        #                                        self.__EnemyDict, self.__weapon, self.__ammo, self.__firstAid,
        #                                        self.__lock, self.__HeapAstar)

        while True:
            """
            # In every loop:
            # 1.Timing task launch
            # 2. If socketfd in readList can be readed (data from another end,
            # as for local, they are inputs), select function move it to rAvailList,
            # preparing to read it into local buffer
            # 3. If the write operation of socketfd (in local, so actually they are outputs)
            # in writeList has been done, select function move it to wAvailList, preparing
            # to send to another end
            """
            """ select """
            rAvailList, wAvailList, xAvailList = select(readList, writeList, [])

            """
            # the socket is ready to read means curr process can read the socket:
            # 1. the data in socket's receive buff reach a specified value
            # 2. this read half of this socket close, the another end never send data to this socket (this socket get FIN)
            # 3. for listenfd, there is a new connect to server
            # 4. error
            """
            for sockReadAvailable in rAvailList:
                """
                # The listenfd can be readed means there is a new connection
                # Actually, the listenfd is ready to accept a connection
                """
                if sockReadAvailable == listenfd:
                    if len(self.__noUseConnList) == 0:
                        """
                        # the server have no way to accept more client connection
                        # but here, no matter block or no-block of client's connect
                        # the three times handshake has finished and the connection
                        # has been in accept queue, so i need to call accept to take
                        # it from queue and process
                        """
                        """ take the connection from finished listen queue """
                        cliSock, cliAddr = listenfd.accept()

                        """ the sned buffer of this cliSock must be empty, so block send is totally OK """
                        cliSock.setblocking(True)

                        """ message back to client """
                        errMesg = 'The server refuse your connection request!'
                        # cliSock.send(errMesg.encode('utf-8'))

                        """
                        # server close the socket initiatively
                        # there is no need to get data response from client
                        # so just close is OK
                        # Besides, the server never put cliSock to any monitoring list
                        # (readList, writeList), just process here
                        """
                        cliSock.close()

                        """ server log """
                        print(
                            'The Server refuse a connection, because the server only supports 50 clients in one time, the connection from: ',
                            cliAddr)
                    else:
                        """ take a conn in link pool, the conns in link pool are all in default state """
                        cliSock, cliAddr = listenfd.accept()
                        print('...connected from: ', cliAddr)
                        """ no blocking """
                        cliSock.setblocking(False)

                        """ take the last Conn in self.__noUseConnList to deal with current client connection """
                        newConn = self.__noUseConnList.pop()
                        """ append newConn to self.__inUseConnList """
                        self.__inUseConnList.append(newConn)
                        """ it is easy to get the Conn with cliSock """
                        self.__cliSockToConn[cliSock] = newConn
                        """ init the new Conn """
                        newConn.init_connection(cliAddr, self.__msgManger)
                        """
                        # add cliSock to readlist, when corresponding client send message to this cliSock
                        # select function will trigger to read the data from client
                        """
                        readList.append(cliSock)
                else:
                    """
                    # firstly, i need to judge if the read available message means the client is close
                    # if the data is empty, the client send a FIN to server
                    """
                    dataRead = sockReadAvailable.recv(1024)
                    dataRead = dataRead.decode('utf-8')

                    if dataRead == '':
                        print('client close, IP: ' + str(self.__cliSockToConn[sockReadAvailable].GetAddr()[0]) +
                              '  PORT: ' + str(self.__cliSockToConn[sockReadAvailable].GetAddr()[1]))

                        """ the client close, clear corresponding socket in writeList and readList """
                        if sockReadAvailable in writeList:
                            writeList.remove(sockReadAvailable)

                        readList.remove(sockReadAvailable)

                        """ delete player in self.__idToConn """
                        # self._DeletePlayer(sockReadAvailable)

                        """
                        # close a conn and clear it
                        # save the state of player to database
                        """
                        self.__cliSockToConn[sockReadAvailable].closeConnection()

                        """ back Conn to link pool, it is just a refer """
                        self.__noUseConnList.append(self.__cliSockToConn[sockReadAvailable])

                        """ remove the refer in self.__inUseConnList """
                        self.__inUseConnList.remove(self.__cliSockToConn[sockReadAvailable])

                        """ clear dict """
                        del self.__cliSockToConn[sockReadAvailable]

                        """
                        # Although the socket has closed, the socket still in rAvailList and wAvailList
                        # and i will check wAvailList to send data, so here i need to clear it
                        # if client never send data to server, so the sockReadAvailable is not in wAvailList
                        """
                        if sockReadAvailable in wAvailList:
                            wAvailList.remove(sockReadAvailable)

                        """ close is totally ok here """
                        sockReadAvailable.close()
                    else:
                        """
                        # the client send a normal message
                        # the put of queue is a block method, so the message must be added into queue
                        """
                        self.__cliSockToConn[sockReadAvailable].GetReadBuffer().put(dataRead)

                        """
                        # when server recieve data from client, the corresponding Connection need to process the data
                        # if the server just need to send the data to corresponding client, the data will be in write queue
                        # in function DataStreamProcess
                        """
                        self.__cliSockToConn[sockReadAvailable].DataStreamProcess(self.__idToConn, self.__weapon,
                                                                                  self.__armor, self.__special,
                                                                                  self.__lock)

                        """ as for a new socket, it should be added in writeList """
                        if sockReadAvailable not in writeList:
                            writeList.append(sockReadAvailable)

            """
            # the socket is ready to write means curr process can write data to the socket:
            # 1. the data in socket's send buff lower than a specified value
            # 2. the write half of this socket close (server close active)
            # 3. client call connect
            # 4. error
            """
            for sockWriteAvailable in wAvailList:
                """
                # when a socket is ready to send data to another end
                # but the corresponding writeBuffer is empty
                # i need to process it, otherwise the server crashes
                """
                while not self.__cliSockToConn[sockWriteAvailable].GetWriteBuffer().empty():
                    """ there is data in write buffer need to be sended """
                    sendData = self.__cliSockToConn[sockWriteAvailable].GetWriteBuffer().get_nowait()
                    """
                    # when the send back means the data has been in the buffer of socket
                    # if the send is block, so when the sendBUffer of socket is full,
                    # the server will blocked here, so the data is complete
                    """
                    sockWriteAvailable.send(bytes(sendData, 'utf-8'))

    """ Private Function start here """

    """ Init the link pool """

    def _initLinkPool(self):
        entries = 0
        while True:
            if entries == self.__maxConn:
                """ The max connections of clients is 50 """
                break
            else:
                self.__noUseConnList.append(Connection())

            entries = entries + 1

#	""" delete player in self.__idToConn """
#	def _DeletePlayer(self, sockReadAvailable):
#		""" get the player """
#		player = self.__cliSockToConn[sockReadAvailable].GetPlayer()
#		"""  """
#		playerId = player.GetPlayerId()
#		"""  """
#		del self.__idToConn[playerId]
#		""" broadcast the message to all clients """
#		self.__serverEvent.DdeleteId(playerId, self.__msgManger, self.__idToConn)
