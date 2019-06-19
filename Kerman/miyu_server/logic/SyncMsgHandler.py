# @Author  :_kerman jt
# @Time    : 19-5-29 下午5:54


import json

from logic.prop import Weapon, Armor, Special


class SyncMsgHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        self._broadCast(msgObj, currCliConn)

    def _broadCast(self, msgObj, currCliConn):
        playerId = msgObj.GetId()
        for personId in currCliConn.keys():
            if playerId != personId:
                msg = json.dumps(msgObj.JsonSerialize())
                currCliConn['personId'].GetWriteBuffer.put(msg)


class LogoutHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        Lock.acquire()
        del currCliConn[conn.GetPlayer().GetPlayerId()]
        Lock.release()
        conn.closeConnection()
        broadcastHandler = SyncMsgHandler()
        broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                    weapon, armor, special, Lock)


class SyncPlayerDieHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock) -> list:
        respMsgList = []
        conn.closeConnection()
        broadcastHandler = SyncMsgHandler()
        broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                    weapon, armor, special, Lock)
        respMsgList.append(msgObj.JsonSerialize())
        return respMsgList


class SyncAndSaveLocationHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        """ get the sync location or rotation """
        value = msgObj.GetValue()
        """ Update the info in server """
        conn.GetPlayer().SetLocation(value)
        """ Sync to all clients """
        broadcastHandler = SyncMsgHandler()
        broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                    weapon, armor, special, Lock)


class SyncAndSaveRotationHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        """ get the sync location or rotation """
        value = msgObj.GetValue()
        """ Update the info in server """
        conn.GetPlayer().SetRotation(value)
        """ Sync to all clients """
        broadcastHandler = SyncMsgHandler()
        broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                    weapon, armor, special, Lock)


class SyncAttackHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        damage = msgObj.GetValue()
        try:
            attackedConn = currCliConn[msgObj.GetAttacked()]
        except:
            return

        attackedConn.GetPlayer().UpdateLifeBar(damage)
        broadcastHandler = SyncMsgHandler()
        broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                    weapon, armor, special, Lock)
        if attackedConn.GetPlayer().GetLifeBar() <= 0:
            Lock.acquire()
            del currCliConn[attackedConn.GetPlayer().GetPlayerId()]
            Lock.release()


class SyncImHandler(object):
    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        spend = msgObj.GetValue()
        ImConn = currCliConn[msgObj.GetIm()]
        ImConn.GetPlayer().UpdateEnergyBar(spend)
        broadcastHandler = SyncMsgHandler()
        broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                    weapon, armor, special, Lock)
        if ImConn.GetPlayer().GetLifeBar() <= 0:
            Lock.acquire()
            # 不能再发消息
            Lock.release()


class SyncPropPickUpHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        if msgObj.GetProtoName is 'WeaponPickup':
            returnVal = self._MsgPropPickupHandler(conn, msgObj, currCliConn,
                                                   weapon, Lock)
            if returnVal[1] is True:
                """ 在成功的情况下需要更新服务端的属性，玩家当前持有武器的属性发生了变化 """
                currPlayer = conn.GetPlayer()
                currPlayer.ChangeWeapon(msgObj.GetType())  # 表示玩家拾取新武器的类型标号

            return returnVal[0]

        if msgObj.GetProtoName() is 'ArmorPickup':
            returnVal = self._MsgPropPickupHandler(conn, msgObj, currCliConn,
                                                   armor, Lock)

            if returnVal[1] is True:
                """ 在成功的情况下需要更新服务端的属性，玩家当前持有防具的属性发生了变化 """
                currPlayer = conn.GetPlayer()
                currPlayer.ChangeArmor(msgObj.GetType())

            return returnVal[0]

        if msgObj.GetProtoName() is 'SpecialPickup':
            returnVal = self._MsgPropPickupHandler(conn, msgObj, currCliConn,
                                                   special, Lock)

            if returnVal[1] is True:
                """ 在成功的情况下需要更新服务端的属性，玩家当前持有的特殊道具发生了变化 """
                currPlayer = conn.GetPlayer()
                currPlayer.AddSpecial(msgObj.GetType())

            return returnVal[0]

    def _MsgPropPickupHandler(self, conn, msgObj,
                              currCliConn, Prop, Lock):
        respMsgList = []
        flag = None
        Lock.acquire()
        key = msgObj.GetKey()
        if Prop[key]:
            Prop[key] = None
            broadcastHandler = SyncMsgHandler()
            broadcastHandler.MsgHandler(conn, msgObj, currCliConn,
                                        None, None, None, None)
            respMsgList.append(msgObj.JsonSerialize())

            flag = True
        else:
            msgObj.SetErr()
            respMsgList.append(msgObj.JsonSerialize())
            flag = False
        Lock.release()

        return respMsgList, flag


class SyncPropPickUpFinishHandler(object):

    def MsgHandler(self, conn, msgObj, currCliConn,
                   weapon, armor, special, Lock):
        if msgObj.GetProtoName() is 'WeaponPickup':
            weaponInst = Weapon()
            self._MsgHandler(msgObj, weaponInst, weapon, Lock)
        if msgObj.GetProtoName() is 'ArmorPickup':
            armorInst = Armor()
            self._MsgHandler(msgObj, armorInst, armor, Lock)
        if msgObj.GetProtoName() is 'SpecialPickup':
            specialInst = Special()
            self._MsgHandler(msgObj, specialInst, special, Lock)

    def _MsgHandler(self, msgObj, Inst, PropDict, Lock):
        """ 利用接收到的消息实例化道具类 """
        Inst.SetLoc(msgObj.GetLoc())
        Inst.SetType(msgObj.GetType())
        key = msgObj.GetKey()
        Inst.SetKey(key)

        Lock.acquire()
        PropDict[key] = Inst
        Lock.release()
