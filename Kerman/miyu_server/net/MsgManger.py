# @Author  :_kerman jt
# @Time    : 19-6-2 下午12:33


import json
from proto.LoginMsg import MsgLogin
from proto.SyncMsg import *
from proto.DropPropMsg import *
from logic.SyncMsgHandler import *
from logic.LoginMsgHandler import *
from logic.MsgBroadcastHandler import *


class MsgManger(object):
    def __init__(self):
        self.__protoNameToHandler = {}
        self._addMsgHandler()

    def Decode(self, msg):
        try:
            msgDict = json.loads(msg)
            msgObj = globals()[msgDict['protoName']]()
            msgObj.MsgRecive(msgDict)

            return msgObj
        except:
            pass

    def DistributeMsg(self, conn, msgObj, currCliConn, Weapon, Armor, Special, Lock):
        respMsgList = []
        returnVal = self.__protoNameToHandler[msgObj.GetProtoName()].MsgHandler(conn, msgObj,
                                                                                currCliConn, Weapon, Armor, Special,
                                                                                Lock)
        if returnVal is not None:
            for msg in returnVal:
                respMsgList.append(json.dumps(msg))

        return respMsgList

    def _addMsgHandler(self):
        self.__protoNameToHandler['MsgLogin'] = LoginMsgHandler()
        """ All Sync message has the same way, forward to all the other clients """
        self.__protoNameToHandler['MsgSyncMoveOperation'] = SyncMsgHandler()
        self.__protoNameToHandler['MsgSyncBulletHole'] = SyncMsgHandler()
        self.__protoNameToHandler['MsgSyncReload'] = SyncMsgHandler()
        self.__protoNameToHandler['MsgLogout'] = LogoutHandler()
        self.__protoNameToHandler['MsgSyncPlayerDie'] = SyncPlayerDieHandler()
        """ Update the corresponding data in server and broadcast it to all other clients  """
        self.__protoNameToHandler['MsgSyncRotation'] = SyncAndSaveRotationHandler()
        self.__protoNameToHandler['MsgSyncLocation'] = SyncAndSaveLocationHandler()
        self.__protoNameToHandler['MsgSyncAttack'] = SyncAttackHandler()
        """ 与玩家拾取装备相关的消息 """
        self.__protoNameToHandler['WeaponPickup'] = SyncPropPickUpHandler()
        self.__protoNameToHandler['MsgWeaponPickFinish'] = SyncPropPickUpFinishHandler()
        self.__protoNameToHandler['ArmorPickup'] = SyncPropPickUpHandler()
        self.__protoNameToHandler['MsgArmorPickFinish'] = SyncPropPickUpFinishHandler()
        self.__protoNameToHandler['SpecialPickup'] = SyncPropPickUpHandler()
        self.__protoNameToHandler['MsgSpecialPickFinish'] = SyncPropPickUpFinishHandler()
        """ server generate corresponding message and broadcast to all clients """
        self.__protoNameToHandler['MsgDropWeapon'] = MsgBroadcastHandler()
        self.__protoNameToHandler['MsgDropArmor'] = MsgBroadcastHandler()
        self.__protoNameToHandler['MsgDropSpecial'] = MsgBroadcastHandler()
        """ 敌人在服务端重新生成的时候会利用这个消息 """
        self.__protoNameToHandler['MsgSyncNew'] = MsgBroadcastHandler()
