# @Author  :_kerman jt
# @Time    : 19-5-29 下午12:02


import random
import datetime
import socket
import threading


class TimingTaskThread(threading.Thread):

    def __init__(self, msgManger, currCliConn,
                 Weapon, Armor, Special, syncLock):
        threading.Thread.__init__(self)
        self.__serverEvent = ServerEvent()
        self.__currCliConn = currCliConn
        self.__msgManger = msgManger
        """"""
        self.__weapon = Weapon
        self.__armor = Armor
        self.__special = Special
        self.__syncLock = syncLock

    def timingTask(self):
        numPerson = len(self.__currCliConn)
        self.__serverEvent.TimingTask(numPerson, self.__currCliConn, self.__msgManger,
                                      self.__weapon, self.__armor, self.__special, self.__syncLock)

    """ Re-write the Execution of threads """

    def run(self):
        scheduler = BackgroundScheduler()
        # 间隔3秒钟执行一次
        scheduler.add_job(self.timingTask, 'interval', seconds=30)
        # 这里的调度任务是独立的一个线程
        scheduler.start()


class ServerEvent(object):

    def __init__(self):
        self.__xMin = -300
        self.__xMax = -260
        # self.__yMin = 10
        # self.__yMax = 100
        self.__zMin = 20
        self.__zMax = 70

    def CreateTimingTaskThread(self, msgManger, currCliConn, Weapon,
                               Armor, Special, SyncLock):
        TimingThread = TimingTaskThread(msgManger, currCliConn, Weapon,
                                        Armor, Special, SyncLock)
        TimingThread.start()

    def TimingTask(self, numPerson, curCliConn, msgManger, Weapon,
                   Armor, Special, SyncLock):
        msgObj = self._weaponDrop(numPerson, Weapon, SyncLock)
        if msgObj is not None:
            msgManger.DistributeMsg(None, msgObj, curCliConn, None, None, None, None, None)

        msgObj = self._armorDrop(numPerson, Armor, SyncLock)
        if msgObj is not None:
            msgManger.DistributeMsg(None, msgObj, curCliConn, None, None, None, None, None)

        msgObj = self._SpecialDrop(numPerson, Special, SyncLock)
        if msgObj is not None:
            msgManger.DistributeMsg(None, msgObj, curCliConn, None, None, None, None, None)

    def _weaponDrop(self, numPerson, _Weapon, SyncLock):
        # 1.剑
        # 2.刀
        # ...
        maxweapon = random.randint(numPerson, 2 * numPerson)
        if len(_Weapon) < maxweapon:
            SyncLock.acquire()
            weaponType = random.randint(1, 10)
            locationX = random.uniform(self.__xMin, self.__xMax)
            # locationY = random.uniform(self.__yMin, self.__yMax)
            locationZ = random.uniform(self.__zMin, self.__zMax)

    def _armorDrop(self, numPerson, _Armor, SyncLock):
        # 1.布衣
        # 2.铠甲
        # ...
        pass

    def _SpecialDrop(self, numPerson, _Special, SyncLock):
        # 1.陷阱解除
        # 2.免死
        # ...
        pass
